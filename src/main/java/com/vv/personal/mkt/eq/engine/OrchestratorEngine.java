package com.vv.personal.mkt.eq.engine;

import com.google.common.util.concurrent.AtomicDouble;
import com.vv.personal.mkt.eq.responses.IntraPnL;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import lombok.extern.slf4j.Slf4j;

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
    private final Integer executionIntervalInSeconds;

    private final EquitiesMarketProto.Holdings holdings;
    private final ScheduledExecutorService scheduledEngine;

    public OrchestratorEngine(EquitiesMarketProto.Holdings holdings, int workerThreadCount, int executionIntervalInSeconds, Integer resolution,
                              NetworkEngine networkEngine, ComputeEngine computeEngine) {
        this.holdings = holdings;
        this.resolution = resolution;
        this.executionIntervalInSeconds = executionIntervalInSeconds;
        this.networkEngine = networkEngine;
        this.computeEngine = computeEngine;

        scheduledEngine = Executors.newScheduledThreadPool(workerThreadCount);
        scheduledEngine.scheduleWithFixedDelay(this::invokeEngine, executionIntervalInSeconds, executionIntervalInSeconds, TimeUnit.SECONDS);
        //initial delay as Starter calls invokeEngine() directly on startup
    }

    public void invokeEngine() {
        long endTime = System.currentTimeMillis() / 1000 - 10;
        long startTime = endTime - executionIntervalInSeconds;
        List<IntraPnL> intraPnLS = networkEngine.invokeEngine(holdings, resolution, startTime, endTime);
        if (!intraPnLS.isEmpty()) {
            EquitiesMarketProto.LivePnLs.Builder livePnLs = EquitiesMarketProto.LivePnLs.newBuilder();
            intraPnLS.forEach(intraPnL ->
                    livePnLs.addLivePnLs(
                            computeEngine.invokeLivePnLComputeEngine(intraPnL.getHolding(), intraPnL)
                    ));

            AtomicDouble subTotal = new AtomicDouble(0.0);
            AtomicInteger maxSymbolLength = new AtomicInteger(0);
            livePnLs.getLivePnLsList().forEach(livePnL ->
                    maxSymbolLength.set(Math.max(maxSymbolLength.get(), livePnL.getHolding().getSymbol().length()))
            );

            livePnLs.getLivePnLsList().forEach(livePnL -> {
                String data = String.format("%s\t%s\t%s\t%s\t%s\t%s",
                        inflateWithSpace(livePnL.getHolding().getSymbol(), maxSymbolLength.get()),
                        livePnL.getCurrentRate(),
                        livePnL.getHolding().getQty(),
                        livePnL.getHolding().getBuyRate(),
                        livePnL.getDiff(),
                        livePnL.getDiffPercent());
                System.out.println(data);
                subTotal.addAndGet(livePnL.getDiff());
            });
            System.out.printf("Total diff subtotal: %.2f\n*********\n", subTotal.get());
        } else {
            log.warn("!!! NO LIVE PNL COMPUTED in this iteration !!!");
        }
    }

    static String inflateWithSpace(String symbol, int maxSymbolLength) {
        StringBuilder correctedSymbol = new StringBuilder(symbol);
        for (int i = 1; i <= maxSymbolLength - symbol.length(); i++) correctedSymbol.append(" ");
        return correctedSymbol.toString();
    }

    public void destroyExecutor() {
        if (scheduledEngine != null && !scheduledEngine.isShutdown()) {
            log.info("Shutting down scheduled orchestrator engine");
            scheduledEngine.shutdown();
        }
    }

}
