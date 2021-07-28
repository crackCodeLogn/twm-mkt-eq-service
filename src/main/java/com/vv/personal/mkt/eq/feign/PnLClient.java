package com.vv.personal.mkt.eq.feign;


import com.vv.personal.mkt.eq.responses.IntraPnL;
import feign.Param;
import feign.RequestLine;

public interface PnLClient {

    @RequestLine("GET /intra?symbol={symbol}&resolution={resolution}&from={from}&to={to}")
    IntraPnL getIntraPnL(@Param("symbol") String symbol,
                         @Param("resolution") Integer resolution,
                         @Param("from") Long start,
                         @Param("to") Long end);

    /*
    @RequestLine("GET /history")
        //TODO -- check
    HistoricPnL getHistoricPnL(@Param("symbol") String symbol,
                               @Param("resolution") Integer resolution,
                               @Param("start") Long start,
                               @Param("end") Long end);
     */

}
