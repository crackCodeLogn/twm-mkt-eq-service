package com.vv.personal.mkt.eq.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 28/07/21
 */
@ConfigMapping(prefix = "remote")
public class RemoteServerConfig {

    private String server;
    private Integer serverResolution;
    private String serverConnectionProtocol;
    private Integer workerThreads;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getServerResolution() {
        return serverResolution;
    }

    public void setServerResolution(Integer serverResolution) {
        this.serverResolution = serverResolution;
    }

    public String getServerConnectionProtocol() {
        return serverConnectionProtocol;
    }

    public void setServerConnectionProtocol(String serverConnectionProtocol) {
        this.serverConnectionProtocol = serverConnectionProtocol;
    }

    public Integer getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(Integer workerThreads) {
        this.workerThreads = workerThreads;
    }
}
