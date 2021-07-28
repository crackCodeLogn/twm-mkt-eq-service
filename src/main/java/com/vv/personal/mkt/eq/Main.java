package com.vv.personal.mkt.eq;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vivek
 * @since 12/06/21
 */
@Slf4j
@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(args);
    }
}
