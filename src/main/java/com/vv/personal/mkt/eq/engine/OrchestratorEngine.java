package com.vv.personal.mkt.eq.engine;

import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Vivek
 * @since 28/07/21
 */
public class OrchestratorEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrchestratorEngine.class);

    private final ScheduledExecutorService scheduledEngine;

    private final EquitiesMarketProto.Holdings holdings;
    private final int workerThreadCount;
    private final int executionIntervalInSeconds;

    public OrchestratorEngine(EquitiesMarketProto.Holdings holdings, int workerThreadCount, int executionIntervalInSeconds) {
        this.holdings = holdings;
        this.workerThreadCount = workerThreadCount;
        this.executionIntervalInSeconds = executionIntervalInSeconds;

        scheduledEngine = Executors.newSingleThreadScheduledExecutor();
        scheduledEngine.scheduleWithFixedDelay(, 0, executionIntervalInSeconds, TimeUnit.SECONDS);

    }


}
