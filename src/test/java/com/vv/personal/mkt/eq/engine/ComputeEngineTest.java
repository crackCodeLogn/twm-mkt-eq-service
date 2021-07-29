package com.vv.personal.mkt.eq.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vivek
 * @since 28-07-2021
 */
class ComputeEngineTest {

    @Test
    public void testRoundOff() {
        assertEquals(34.568d, ComputeEngine.roundOff(34.5679898989d));
    }

}