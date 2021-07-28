package com.vv.personal.mkt.eq.engine;

import com.vv.personal.mkt.eq.config.BeanStore;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author Vivek
 * @since 28-07-2021
 */
@Slf4j
public class Starter {

    @Inject
    BeanStore beanStore;

    void onStart(@Observes StartupEvent startupEvent) {
        log.info("***Application starting up***");

        beanStore.OrchestratorEngine().invokeEngine();

        log.info("*** Shutting Down! ***");
        Quarkus.asyncExit();
    }
}
