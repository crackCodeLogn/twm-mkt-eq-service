package com.vv.personal.mkt.eq.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 28/07/21
 */
@ConfigMapping(prefix = "remote")
public interface RemoteServerConfig {

    String server();

    Integer serverResolution();

    String serverConnectionProtocol();

    Integer workerThreads();

    Integer pullIntervalDuration();
}
