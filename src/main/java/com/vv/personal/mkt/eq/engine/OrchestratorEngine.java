package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.modes.Mode;
import com.vv.personal.mkt.eq.responses.PnL;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vivek
 * @since 28/07/21
 */
@Slf4j
public class OrchestratorEngine {

    private final NetworkEngine networkEngine;
    private final ComputeEngine computeEngine;
    private final Integer resolution;
    private final Integer orchestrationExecutionIntervalSeconds;
    private final Integer networkPullIntervalSeconds;

    private final EquitiesMarketProto.Holdings holdings;
    private final ScheduledExecutorService orchestrationScheduledEngine;
    private final ScheduledExecutorService networkAndWriteScheduledEngine;

    public OrchestratorEngine(EquitiesMarketProto.Holdings holdings, int orchestrationExecutionIntervalInSeconds, Integer resolution,
                              NetworkEngine networkEngine, int networkPullIntervalSeconds, ComputeEngine computeEngine) {
        this.holdings = holdings;
        this.resolution = resolution;
        this.orchestrationExecutionIntervalSeconds = orchestrationExecutionIntervalInSeconds;
        this.networkPullIntervalSeconds = networkPullIntervalSeconds;
        this.networkEngine = networkEngine;
        this.computeEngine = computeEngine;

        log.info("Firing up scheduled-orchestration-engine with execution interval of {} seconds", orchestrationExecutionIntervalInSeconds);
        orchestrationScheduledEngine = Executors.newSingleThreadScheduledExecutor();
        orchestrationScheduledEngine.scheduleWithFixedDelay(this::invokeEngine, orchestrationExecutionIntervalInSeconds, orchestrationExecutionIntervalInSeconds, TimeUnit.SECONDS);
        log.info("Initiating scheduled-network-engine with execution interval of {} seconds", networkPullIntervalSeconds);
        networkAndWriteScheduledEngine = Executors.newSingleThreadScheduledExecutor();
        networkAndWriteScheduledEngine.scheduleWithFixedDelay(this::invokeEngine, networkPullIntervalSeconds, networkPullIntervalSeconds, TimeUnit.SECONDS);
        //initial delay as Starter calls invokeEngine() directly on startup
    }

    public void invokeEngine() {
        long endTime = System.currentTimeMillis() / 1000 - 10;
        long startTime = endTime - orchestrationExecutionIntervalSeconds;

        Mode mode;
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int time = hourOfDay * 100 + minute;
        if (time >= 930 && time <= 1530) mode = Mode.INTRA;
        else mode = Mode.HISTORIC;

        mode = Mode.INTRA; //TODO -- forcing to INTRA for now, as it seems uncertain for HISTORIC

        List<PnL> pnLS = networkEngine.invokeEngine(holdings, resolution, startTime, endTime, mode);
        if (!pnLS.isEmpty()) {
            EquitiesMarketProto.LivePnLs.Builder livePnLs = EquitiesMarketProto.LivePnLs.newBuilder();
            pnLS.forEach(intraPnL -> livePnLs.addLivePnLs(computeEngine.invokeLivePnLComputeEngine(intraPnL.getHolding(), intraPnL)));

            AtomicInteger maxSymbolLength = new AtomicInteger(0);
            livePnLs.getLivePnLsList().forEach(livePnL -> maxSymbolLength.set(Math.max(maxSymbolLength.get(), livePnL.getHolding().getSymbol().length())));

            EquitiesMarketProto.PnLOutput pnLOutput = computeEngine.invokePnLOutputComputeEngine(livePnLs.build());
            pnLOutput.getLivePnLs().getLivePnLsList().forEach(livePnL -> {
                String data = String.format("%s\t%s\t%s\t%s\t%s\t%s",
                        inflateWithSpace(livePnL.getHolding().getSymbol(), maxSymbolLength.get()),
                        livePnL.getHolding().getBuyRate(),
                        livePnL.getHolding().getQty(),
                        livePnL.getCurrentRate(),
                        livePnL.getDiff(),
                        livePnL.getDiffPercent());
                System.out.println(data); //direct output to prevent logger clutter
            });
            System.out.printf("Total PnL: %.3f  ::  Current rate of return: %.3f\n", pnLOutput.getPnLSubTotal(), pnLOutput.getCurrentRateOfReturn()); //direct output to prevent logger clutter
            System.out.printf("Time: %s\n*********\n\n", Calendar.getInstance().getTime()); //direct output to prevent logger clutter
        } else {
            log.warn("!!! NO PNL COMPUTED in this iteration !!!");
        }
    }

    public void invokeNetworkEngine() {
        long endTime = System.currentTimeMillis() / 1000 - 10;
        long startTime = endTime - networkPullIntervalSeconds;
        Mode mode;
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int time = hourOfDay * 100 + minute;
        if (time >= 930 && time <= 1530) mode = Mode.INTRA;
        else mode = Mode.HISTORIC;

        mode = Mode.INTRA; //TODO -- forcing to INTRA for now, as it seems uncertain for HISTORIC

        List<PnL> pnLS = networkEngine.invokeEngine(holdings, resolution, startTime, endTime, mode);

    }

    static String inflateWithSpace(String symbol, int maxSymbolLength) {
        StringBuilder correctedSymbol = new StringBuilder(symbol);
        for (int i = 1; i <= maxSymbolLength - symbol.length(); i++) correctedSymbol.append(" ");
        return correctedSymbol.toString();
    }

    public void destroyExecutor() {
        if (networkAndWriteScheduledEngine != null && !networkAndWriteScheduledEngine.isShutdown()) {
            log.info("Shutting down scheduled orchestrator engine");
            networkAndWriteScheduledEngine.shutdown();
        }
    }

}
