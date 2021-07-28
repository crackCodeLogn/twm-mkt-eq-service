package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.responses.IntraPnL;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Vivek
 * @since 28/07/21
 */
@Slf4j
public class OrchestratorEngine {

    private final NetworkEngine networkEngine;
    private final ComputeEngine computeEngine;

    private final EquitiesMarketProto.Holdings holdings;
    private final ScheduledExecutorService scheduledEngine;

    public OrchestratorEngine(EquitiesMarketProto.Holdings holdings, int workerThreadCount, int executionIntervalInSeconds,
                              NetworkEngine networkEngine, ComputeEngine computeEngine) {
        this.holdings = holdings;
        this.networkEngine = networkEngine;
        this.computeEngine = computeEngine;

        scheduledEngine = Executors.newScheduledThreadPool(workerThreadCount);
        scheduledEngine.scheduleWithFixedDelay(this::invokeEngine, 0, executionIntervalInSeconds, TimeUnit.SECONDS);
    }

    public void invokeEngine() {
        List<IntraPnL> intraPnLS = networkEngine.invokeEngine(holdings, 1, 1627458645L, 1627458885L);
        if (!intraPnLS.isEmpty()) {
            EquitiesMarketProto.LivePnLs.Builder livePnLs = EquitiesMarketProto.LivePnLs.newBuilder();
            intraPnLS.forEach(intraPnL ->
                    livePnLs.addLivePnLs(
                            computeEngine.invokeLivePnLComputeEngine(intraPnL.getHolding(), intraPnL)
                    ));
            log.info("Live PnL => \n{}\n", livePnLs);
        } else {
            log.warn("!!! NO LIVE PNL COMPUTED in this iteration !!!");
        }
    }

}
