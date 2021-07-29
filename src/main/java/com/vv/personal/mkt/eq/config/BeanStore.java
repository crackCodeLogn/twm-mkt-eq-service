package com.vv.personal.mkt.eq.config;

import com.vv.personal.mkt.eq.engine.ComputeEngine;
import com.vv.personal.mkt.eq.engine.NetworkEngine;
import com.vv.personal.mkt.eq.engine.OrchestratorEngine;
import com.vv.personal.mkt.eq.reader.HoldingsReader;
import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * @author Vivek
 * @since 28-07-2021
 */
@Configuration
public class BeanStore {

    @Inject
    RemoteServerConfig remoteServerConfig;
    @Inject
    EquitiesMarketConfig equitiesMarketConfig;

    @Bean
    public HoldingsReader HoldingsReader() {
        return new HoldingsReader(equitiesMarketConfig.getHoldingsFileLocation());
    }

    @Bean(destroyMethod = "destroyExecutor")
    public NetworkEngine NetworkEngine() {
        return new NetworkEngine(
                String.format("%s://%s", remoteServerConfig.getServerConnectionProtocol(), remoteServerConfig.getServer()),
                remoteServerConfig.getWorkerThreads()
        );
    }

    @Bean(destroyMethod = "destroyExecutor")
    public OrchestratorEngine OrchestratorEngine() {
        EquitiesMarketProto.Holdings holdings = HoldingsReader().readInHoldings();

        return new OrchestratorEngine(
                holdings,
                equitiesMarketConfig.getOrchestratorWorkerThreads(),
                equitiesMarketConfig.getExecutionIntervalDuration(),
                remoteServerConfig.getServerResolution(),
                NetworkEngine(),
                ComputeEngine()
        );
    }

    @Bean(destroyMethod = "destroyExecutor")
    public ComputeEngine ComputeEngine() {
        return new ComputeEngine(equitiesMarketConfig.getComputeWorkerThreads());
    }

}
