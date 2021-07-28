package com.vv.personal.mkt.eq.config;

import com.vv.personal.mkt.eq.engine.ComputeEngine;
import com.vv.personal.mkt.eq.engine.NetworkEngine;
import com.vv.personal.mkt.eq.engine.OrchestratorEngine;
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

    @Bean(destroyMethod = "destroyExecutor")
    public NetworkEngine NetworkEngine() {
        return new NetworkEngine(
                String.format("%s://%s", remoteServerConfig.getServerConnectionProtocol(), remoteServerConfig.getServer()),
                remoteServerConfig.getWorkerThreads()
        );
    }

    @Bean
    public OrchestratorEngine OrchestratorEngine() {
        EquitiesMarketProto.Holdings.Builder holdings = EquitiesMarketProto.Holdings.newBuilder();

        //TODO -- remove this post local testing from main
        EquitiesMarketProto.Holding holding = EquitiesMarketProto.Holding.newBuilder()
                .setSymbol("IOC")
                .setQty(10)
                .setBuyRate(110.35)
                .build();
        holdings.addHoldings(holding);

        return new OrchestratorEngine(
                holdings.build(),
                equitiesMarketConfig.getOrchestratorWorkerThreads(),
                equitiesMarketConfig.getExecutionIntervalDuration(),
                NetworkEngine(),
                ComputeEngine()
        );
    }

    @Bean
    public ComputeEngine ComputeEngine() {
        return new ComputeEngine(equitiesMarketConfig.getComputeWorkerThreads());
    }

}
