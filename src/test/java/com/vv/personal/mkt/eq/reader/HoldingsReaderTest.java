package com.vv.personal.mkt.eq.reader;

import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vivek
 * @since 29/07/21
 */
class HoldingsReaderTest {

    @Test
    public void testHoldingsReader() {
        HoldingsReader holdingsReader = new HoldingsReader("src/test/resources/landing/holdings.csv");
        EquitiesMarketProto.Holdings holdings = holdingsReader.readInHoldings();
        System.out.println(holdings);
        assertEquals("XYZ", holdings.getHoldingsList().get(0).getSymbol());
        assertEquals(111.75, holdings.getHoldingsList().get(0).getBuyRate());
        assertEquals("ABD", holdings.getHoldingsList().get(1).getSymbol());
        assertEquals(95, holdings.getHoldingsList().get(1).getQty());
    }

}