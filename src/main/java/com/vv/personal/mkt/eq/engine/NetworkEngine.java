package com.vv.personal.mkt.eq.engine;

import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vivek
 * @since 28/07/21
 */
public class NetworkEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkEngine.class);

    private final ExecutorService networkExecutor;

    public NetworkEngine(int networkWorkerThreadCount) {
        networkExecutor = Executors.newFixedThreadPool(networkWorkerThreadCount);
    }

    public EquitiesMarketProto.LivePnLs conjurer(EquitiesMarketProto.Holdings holdings) {

    }

    private Callable<EquitiesMarketProto.LivePnL> generateLivePnLData(EquitiesMarketProto.Holding holding) {
        return () -> {
            EquitiesMarketProto.LivePnL.Builder livePnLBuilder = EquitiesMarketProto.LivePnL.newBuilder();


            return livePnLBuilder.build();
        };
    }

    public void destroyExecutor() {
        if (networkExecutor != null && !networkExecutor.isShutdown()) {
            networkExecutor.shutdown();
        }
    }

}
