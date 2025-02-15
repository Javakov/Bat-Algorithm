package org.javakov;

import org.javakov.algorithm.BatAlgorithm;
import org.javakov.dto.BatDto;
import org.javakov.dto.BatParametersDto;

public class Main {
    public static void main(String[] args) {
        BatParametersDto parameters = new BatParametersDto();

        BatAlgorithm batAlgorithm = new BatAlgorithm(parameters);
        batAlgorithm.run();

        BatDto bestBat = batAlgorithm.getBestBat();

        System.out.println("Лучшее решение:");
        for (double x : bestBat.position()) {
            System.out.printf("%.6f ", x);
        }
        System.out.printf("\nЗначение функции: %.6f", bestBat.fitness());
    }
}