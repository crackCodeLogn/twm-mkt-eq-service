package com.vv.personal.mkt.eq.reader;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static com.vv.personal.mkt.eq.constants.Constants.*;

/**
 * @author Vivek
 * @since 29/07/21
 */
@Slf4j
public class HoldingsReader {

    private final String holdingsFileLocation;

    public HoldingsReader(String holdingsFileLocation) {
        this.holdingsFileLocation = holdingsFileLocation;
    }

    public EquitiesMarketProto.Holdings readInHoldings() {
        EquitiesMarketProto.Holdings.Builder holdings = EquitiesMarketProto.Holdings.newBuilder();
        try (CSVReaderHeaderAware csvReaderHeaderAware = new CSVReaderHeaderAware(new FileReader(holdingsFileLocation))) {
            Map<String, String> map;
            while ((map = csvReaderHeaderAware.readMap()) != null) {
                try {
                    holdings.addHoldings(generateHolding(
                            map.get(SYMBOL).strip(),
                            Integer.parseInt(map.get(QTY).strip()),
                            Double.parseDouble(map.get(BUY_PRICE).strip())
                    ));
                } catch (Exception e) {
                    log.error("Failed to read line having map [{}]. ", map, e);
                }
            }
        } catch (IOException | CsvValidationException ioException) {
            log.error("Failed to read in holdings data. ", ioException);
        }
        return holdings.build();
    }

    EquitiesMarketProto.Holding generateHolding(String symbol, Integer qty, Double buyPrice) {
        return EquitiesMarketProto.Holding.newBuilder()
                .setSymbol(symbol)
                .setQty(qty)
                .setBuyRate(buyPrice)
                .build();
    }

}
