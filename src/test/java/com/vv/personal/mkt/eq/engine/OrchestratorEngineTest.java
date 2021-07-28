package com.vv.personal.mkt.eq.engine;

import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vivek
 * @since 28-07-2021
 */
class OrchestratorEngineTest {

    @Test
    public void testOrchestration() {
        EquitiesMarketProto.Holdings.Builder holdingsBuilder = EquitiesMarketProto.Holdings.newBuilder();
        EquitiesMarketProto.Holding holding = EquitiesMarketProto.Holding.newBuilder()
                .setSymbol("IOC")
                .setQty(10)
                .setBuyRate(110.35)
                .build();
        holdingsBuilder.addHoldings(holding);

        NetworkEngine networkEngine = new NetworkEngine("https://priceapi.moneycontrol.com/techCharts", 1);
        ComputeEngine computeEngine = new ComputeEngine(8);

        OrchestratorEngine orchestratorEngine = new OrchestratorEngine(holdingsBuilder.build(), 1, 30, networkEngine, computeEngine);
        orchestratorEngine.invokeEngine();
    }

}