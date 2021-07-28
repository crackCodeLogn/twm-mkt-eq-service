package com.vv.personal.mkt.eq.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 28/07/21
 */
@ConfigMapping(prefix = "remote")
public class RemoteServerConfig {

    private String remoteServer;
    private Integer remoteServerResolution;
    private String remoteServerConnectionProtocol;
    private String remoteServerModes;

    public String getRemoteServer() {
        return remoteServer;
    }

    public void setRemoteServer(String remoteServer) {
        this.remoteServer = remoteServer;
    }

    public Integer getRemoteServerResolution() {
        return remoteServerResolution;
    }

    public void setRemoteServerResolution(Integer remoteServerResolution) {
        this.remoteServerResolution = remoteServerResolution;
    }

    public String getRemoteServerConnectionProtocol() {
        return remoteServerConnectionProtocol;
    }

    public void setRemoteServerConnectionProtocol(String remoteServerConnectionProtocol) {
        this.remoteServerConnectionProtocol = remoteServerConnectionProtocol;
    }

    public String getRemoteServerModes() {
        return remoteServerModes;
    }

    public void setRemoteServerModes(String remoteServerModes) {
        this.remoteServerModes = remoteServerModes;
    }
}
