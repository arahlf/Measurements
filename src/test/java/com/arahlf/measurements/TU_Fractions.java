package com.arahlf.measurements;

import com.arahlf.measurements.formatting.Fractions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TU_Fractions {

    @ParameterizedTest
    @MethodSource(value = "testCaseInputProvider")
    public void testEdgeCases(String expected, double number, int precision) {
        assertEquals(expected, Fractions.getFractionString(number, precision));
    }

    private static Stream<Arguments> testCaseInputProvider() {
        return Stream.of(
                Arguments.of("0", 0, 4),

                // positive numbers
                Arguments.of("1/8", .125, 8),
                Arguments.of("~1/8", .120, 8),
                Arguments.of("~1/8", .130, 8),
                Arguments.of("1-1/8", 1.125, 8),
                Arguments.of("~1-1/8", 1.1, 8),
                Arguments.of("~1-1/8", 1.13, 8),
                Arguments.of("1", 1, 4),
                Arguments.of("~2", 1.88, 4),
                Arguments.of("~2", 2.12, 4),
                Arguments.of("12", 12, 4),
                Arguments.of("~13", 12.97, 16),
                Arguments.of("~13", 13.02, 16),
                Arguments.of("12-3/16", 12.1875, 16),
                Arguments.of("~13-5/8", 13.630, 8),
                Arguments.of("~13-5/8", 13.640, 8),
                Arguments.of("143", 143, 4),
                Arguments.of("~187", 187.1, 4),
                Arguments.of("~187", 186.9, 4),
                Arguments.of("139-7/64", 139.109375, 64),
                Arguments.of("~139-9/64", 139.140629, 64),
                Arguments.of("~139-9/64", 139.140621, 64),

                // negative numbers
                Arguments.of("-1/8", -.125, 8),
                Arguments.of("~-1/8", -.120, 8),
                Arguments.of("~-1/8", -.130, 8),
                Arguments.of("-1-1/8", -1.125, 8),
                Arguments.of("~-1-1/8", -1.1, 8),
                Arguments.of("~-1-1/8", -1.13, 8),
                Arguments.of("-1", -1, 4),
                Arguments.of("~-2", -1.88, 4),
                Arguments.of("~-2", -2.12, 4),
                Arguments.of("-12", -12, 4),
                Arguments.of("~-13", -12.97, 16),
                Arguments.of("~-13", -13.02, 16),
                Arguments.of("-12-3/16", -12.1875, 16),
                Arguments.of("~-13-5/8", -13.630, 8),
                Arguments.of("~-13-5/8", -13.640, 8),
                Arguments.of("-143", -143, 4),
                Arguments.of("~-187", -187.1, 4),
                Arguments.of("~-187", -186.9, 4),
                Arguments.of("-139-7/64", -139.109375, 64),
                Arguments.of("~-139-9/64", -139.140629, 64),
                Arguments.of("~-139-9/64", -139.140621, 64)
        );
    }
}
