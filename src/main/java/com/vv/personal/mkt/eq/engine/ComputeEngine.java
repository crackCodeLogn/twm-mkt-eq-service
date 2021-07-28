package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.responses.IntraPnL;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.*;

/**
 * @author Vivek
 * @since 28-07-2021
 */
@Slf4j
public class ComputeEngine {

    private final ExecutorService computeEngine;

    public ComputeEngine(int computeThreadCount) {
        this.computeEngine = Executors.newFixedThreadPool(computeThreadCount);
    }

    public EquitiesMarketProto.LivePnL invokeLivePnLComputeEngine(EquitiesMarketProto.Holding holding, IntraPnL intraPnL) {
        IntraPnL.Data latestIntraPnL = intraPnL.getData().get(intraPnL.getData().size() - 1);
        try {
            return computeEngine.submit(compute(holding, latestIntraPnL)).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to compute live PnL for {} x {} having current price of {}", holding.getSymbol(), holding.getBuyRate(), latestIntraPnL.getValue());
        }
        return EquitiesMarketProto.LivePnL.newBuilder().build();
    }

    private Callable<EquitiesMarketProto.LivePnL> compute(EquitiesMarketProto.Holding holding, IntraPnL.Data latest) {
        return () -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            EquitiesMarketProto.LivePnL.Builder livePnLBuilder = EquitiesMarketProto.LivePnL.newBuilder();
            double holdingTotal = holding.getBuyRate() * holding.getQty();
            double currentTotal = latest.getValue() * holding.getQty();
            double diff = currentTotal - holdingTotal;
            double diffPercent = (diff / holdingTotal) * 100;
            livePnLBuilder.setDiff(diff);
            livePnLBuilder.setCurrentRate(latest.getValue());
            livePnLBuilder.setDiffPercent(diffPercent);
            livePnLBuilder.setHolding(holding);
            stopWatch.stop();
            log.debug("Compute took {} ms", stopWatch.getTime(TimeUnit.MILLISECONDS));
            return livePnLBuilder.build();
        };
    }

    public void destroyExecutor() {
        if (computeEngine != null && !computeEngine.isShutdown()) {
            log.info("Shutting down Compute engine");
            computeEngine.shutdown();
        }
    }
}
