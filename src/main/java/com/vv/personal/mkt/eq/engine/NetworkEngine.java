package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.feign.PnLClient;
import com.vv.personal.mkt.eq.modes.Mode;
import com.vv.personal.mkt.eq.responses.PnL;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import feign.Feign;
import feign.gson.GsonDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Vivek
 * @since 28/07/21
 */
@Slf4j
public class NetworkEngine {

    private final ExecutorService networkExecutor;
    private final String targetUrlBase;

    public NetworkEngine(String targetUrlBase, int networkWorkerThreadCount) {
        this.targetUrlBase = targetUrlBase;
        this.networkExecutor = Executors.newFixedThreadPool(networkWorkerThreadCount);
    }

    public List<PnL> invokeEngine(EquitiesMarketProto.Holdings holdings, Integer resolution, Long start, Long end, Mode mode) {
        log.debug("Network engine invoked from {} to {}", start, end);
        List<PnL> pnLS = new ArrayList<>(holdings.getHoldingsCount());
        holdings.getHoldingsList().forEach(holding -> {
            Future<PnL> pnLFuture = networkExecutor.submit(generatePnLData(holding, resolution, start, end, mode));
            try {
                PnL pnL = pnLFuture.get();
                if (StringUtils.isNoneEmpty(pnL.getS())) {
                    pnL.setHolding(holding);
                    pnLS.add(pnL);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error("Failed to get response from remote source for symbol '{}'.", holding.getSymbol(), e);
            }
        });
        return pnLS;
    }

    private Callable<PnL> generatePnLData(EquitiesMarketProto.Holding holding, Integer resolution, Long start, Long end, Mode mode) {
        return () -> {
            PnLClient pnLClient = Feign.builder()
                    .decoder(new GsonDecoder())
                    .target(PnLClient.class, targetUrlBase);
            log.debug("Generated PnLClient: {}", pnLClient);

            PnL pnL = null;
            if (mode == Mode.INTRA)
                pnL = pnLClient.getIntraPnL(holding.getSymbol(), resolution, start, end);
            else if (mode == Mode.HISTORIC)
                pnL = pnLClient.getHistoricPnL(holding.getSymbol(), resolution, start, end);
            log.debug("Received PNL data: {}", pnL);
            if (!pnL.getData().isEmpty()) {
                return pnL;
            }
            return new PnL();
        };
        // sometimes this maybe? priceapi.moneycontrol.com/techCharts/techChartController/history?symbol=SAIL&resolution=1D&from=1593436980&to=1627565040
    }

    public void destroyExecutor() {
        if (networkExecutor != null && !networkExecutor.isShutdown()) {
            log.info("Shutting down network engine");
            networkExecutor.shutdown();
        }
    }

}
