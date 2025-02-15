package org.javakov.algorithm;

public class FitnessFunction {
    public static double calculate(double[] position) {
        double sum = 0.0;
        for (double x : position) {
            sum += x * x; // функция сферы
        }

        return sum;
    }
}
