package com.vv.personal.mkt.eq.feign;


import com.vv.personal.mkt.eq.responses.PnL;
import feign.Param;
import feign.RequestLine;

public interface PnLClient {

    @RequestLine("GET /intra?symbol={symbol}&resolution={resolution}&from={from}&to={to}")
    PnL getIntraPnL(@Param("symbol") String symbol,
                    @Param("resolution") Integer resolution,
                    @Param("from") Long start,
                    @Param("to") Long end);

    @RequestLine("GET /history?symbol={symbol}&resolution={resolution}&from={from}&to={to}")
    PnL getHistoricPnL(@Param("symbol") String symbol,
                       @Param("resolution") Integer resolution,
                       @Param("start") Long start,
                       @Param("end") Long end);

}
