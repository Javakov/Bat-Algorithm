package org.javakov.dto;

public record BatParametersDto(
        int populationSize,
        int maxIterations,
        int dimensions,
        double min,
        double max,
        double fMin,
        double fMax,
        double aInit,
        double rInit,
        double alpha,
        double gamma
) {

    public BatParametersDto() {
        this(
                30,
                1000,
                2,
                -5.0,
                5.0,
                0.0,
                2.0,
                0.9,
                0.5,
                0.9,
                0.9
        );
    }
}
