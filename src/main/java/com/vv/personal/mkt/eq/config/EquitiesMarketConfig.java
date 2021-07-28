package com.vv.personal.mkt.eq.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 27/07/21
 */
@ConfigMapping(prefix = "equity")
public class EquitiesMarketConfig {

    private String symbolsFileLocation;

    public String getSymbolsFileLocation() {
        return symbolsFileLocation;
    }

    public void setSymbolsFileLocation(String symbolsFileLocation) {
        this.symbolsFileLocation = symbolsFileLocation;
    }

}
