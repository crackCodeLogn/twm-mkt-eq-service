package com.vv.personal.mkt.eq.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 27/07/21
 */
@ConfigMapping(prefix = "equity")
public class EquitiesMarketConfig {

    private String symbolsFileLocation;
    private Integer orchestratorWorkerThreads;
    private Integer computeWorkerThreads;
    private Integer executionIntervalDuration;

    public String getSymbolsFileLocation() {
        return symbolsFileLocation;
    }

    public void setSymbolsFileLocation(String symbolsFileLocation) {
        this.symbolsFileLocation = symbolsFileLocation;
    }

    public Integer getOrchestratorWorkerThreads() {
        return orchestratorWorkerThreads;
    }

    public void setOrchestratorWorkerThreads(Integer orchestratorWorkerThreads) {
        this.orchestratorWorkerThreads = orchestratorWorkerThreads;
    }

    public Integer getExecutionIntervalDuration() {
        return executionIntervalDuration;
    }

    public void setExecutionIntervalDuration(Integer executionIntervalDuration) {
        this.executionIntervalDuration = executionIntervalDuration;
    }

    public Integer getComputeWorkerThreads() {
        return computeWorkerThreads;
    }

    public void setComputeWorkerThreads(Integer computeWorkerThreads) {
        this.computeWorkerThreads = computeWorkerThreads;
    }
}
