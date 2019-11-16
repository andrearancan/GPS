package com.example.fabrizio.gps;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /**
     * API tests for CalculatorApi
     */
    @Ignore
    public static class CalculatorApiTest {

        private final CalculatorApi api = new CalculatorApi();


        /**
         * calcolatrice semplice
         *
         * calcola i parametri delle gaussiane che descrivono la mappa
         *
         * @throws ApiException
         *          if the Api call fails
         */
        @Test
        public void computeTest() throws ApiException {
            String latitude = null;
            String longitude = null;
            String response = api.compute(latitude, longitude);

            // TODO: test validations
        }

    }
}