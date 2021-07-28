package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.feign.PnLClient;
import com.vv.personal.mkt.eq.responses.IntraPnL;
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

    public List<IntraPnL> invokeEngine(EquitiesMarketProto.Holdings holdings, Integer resolution, Long start, Long end) {
        List<IntraPnL> intraPnLS = new ArrayList<>(holdings.getHoldingsCount());
        holdings.getHoldingsList().forEach(holding -> {
            Future<IntraPnL> livePnLFuture = networkExecutor.submit(generateLivePnLData(holding, resolution, start, end));
            try {
                IntraPnL livePnL = livePnLFuture.get();
                if (StringUtils.isNoneEmpty(livePnL.getS())) {
                    livePnL.setHolding(holding);
                    intraPnLS.add(livePnL);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error("Failed to get response from remote source for symbol '{}'.", holding.getSymbol(), e);
            }
        });
        return intraPnLS;
    }

    private Callable<IntraPnL> generateLivePnLData(EquitiesMarketProto.Holding holding, Integer resolution, Long start, Long end) {
        return () -> {
            PnLClient pnLClient = Feign.builder()
                    .decoder(new GsonDecoder())
                    .target(PnLClient.class, targetUrlBase);
            log.debug("Generated PnLClient: {}", pnLClient);

            IntraPnL intraPnL = pnLClient.getIntraPnL(holding.getSymbol(), resolution, start, end);
            log.debug("Received intra PNL data: {}", intraPnL);
            if (!intraPnL.getData().isEmpty()) {
                return intraPnL;
            }
            return new IntraPnL();
        };
    }

    public void destroyExecutor() {
        if (networkExecutor != null && !networkExecutor.isShutdown()) {
            log.info("Shutting down network engine");
            networkExecutor.shutdown();
        }
    }

}
