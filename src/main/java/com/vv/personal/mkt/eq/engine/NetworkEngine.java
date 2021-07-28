package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.feign.PnLClient;
import com.vv.personal.mkt.eq.responses.IntraPnL;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Vivek
 * @since 28/07/21
 */
public class NetworkEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkEngine.class);

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
                LOGGER.error("Failed to get response from remote source for symbol '{}'.", holding.getSymbol(), e);
            }
        });
        return intraPnLS;
    }

    private Callable<IntraPnL> generateLivePnLData(EquitiesMarketProto.Holding holding, Integer resolution, Long start, Long end) {
        return () -> {
            PnLClient pnLClient = Feign.builder()
                    .decoder(new GsonDecoder())
                    .target(PnLClient.class, targetUrlBase);
            LOGGER.debug("Generated PnLClient: {}", pnLClient);

            IntraPnL intraPnL = pnLClient.getIntraPnL(holding.getSymbol(), resolution, start, end);
            LOGGER.debug("Received intra PNL data: {}", intraPnL);
            if (!intraPnL.getData().isEmpty()) {
                return intraPnL;
            }
            return new IntraPnL();
        };
    }

    public void destroyExecutor() {
        if (networkExecutor != null && !networkExecutor.isShutdown()) {
            networkExecutor.shutdown();
        }
    }

}
