package org.javakov.dto;

public record BatDto(
        double[] position,
        double[] velocity,
        double frequency,
        double loudness,
        double pulseRate,
        double fitness
) {
}