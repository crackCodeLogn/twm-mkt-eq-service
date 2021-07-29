package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.responses.IntraPnL;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vivek
 * @since 28-07-2021
 */
/*
class NetworkEngineTest {

    @Test
    public void testActualExternalCall() {
        NetworkEngine networkEngine = new NetworkEngine("https://priceapi.moneycontrol.com/techCharts", 1);
        EquitiesMarketProto.Holdings.Builder holdingsBuilder = EquitiesMarketProto.Holdings.newBuilder();
        EquitiesMarketProto.Holding holding = EquitiesMarketProto.Holding.newBuilder()
                .setSymbol("IOC")
                .setQty(10)
                .setBuyRate(110.35)
                .build();
        holdingsBuilder.addHoldings(holding);
        System.out.printf("Holdings => \n%s\n****\n", holdingsBuilder.build());
        List<IntraPnL> livePnLs = networkEngine.invokeEngine(holdingsBuilder.build(), 1, 1627458645L, 1627458885L);
        System.out.println(livePnLs);
    }

}*/
