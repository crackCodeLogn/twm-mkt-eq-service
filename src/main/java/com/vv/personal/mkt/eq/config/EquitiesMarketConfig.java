package com.vv.personal.mkt.eq.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 27/07/21
 */
@ConfigMapping(prefix = "equity")
public interface EquitiesMarketConfig {

    String holdingsFileLocation();

    Integer computeWorkerThreads();

    Integer orchestrationExecutionIntervalDuration();

}
