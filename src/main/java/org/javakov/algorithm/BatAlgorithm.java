package org.javakov.algorithm;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.javakov.dto.BatDto;
import org.javakov.dto.BatParametersDto;

import java.util.Random;

@Slf4j
public class BatAlgorithm {
    private BatDto[] bats;
    @Getter
    private BatDto bestBat;
    private final BatParametersDto parameters;
    private final Random random = new Random();

    public BatAlgorithm(BatParametersDto parameters) {
        this.parameters = parameters;
        log.info("Инициализация алгоритма летучих мышей");
        initializeBats();
        updateBestBat();
    }

    private void initializeBats() {
        bats = new BatDto[parameters.populationSize()];
        for (int i = 0; i < parameters.populationSize(); i++) {
            double[] position = new double[parameters.dimensions()];
            double[] velocity = new double[parameters.dimensions()];
            for (int j = 0; j < parameters.dimensions(); j++) {
                position[j] = parameters.min() + (parameters.max() - parameters.min()) * random.nextDouble();
                velocity[j] = 0.0;
            }
            double frequency = parameters.fMin() + (parameters.fMax() - parameters.fMin()) * random.nextDouble();
            double loudness = parameters.aInit();
            double pulseRate = parameters.rInit();
            double fitness = calculateFitness(position);

            bats[i] = new BatDto(position, velocity, frequency, loudness, pulseRate, fitness);
            log.debug("Создана летучая мышь {} с fitness={}", i, fitness);
        }
    }

    private void updateBestBat() {
        bestBat = bats[0];
        for (BatDto bat : bats) {
            if (bat.fitness() < bestBat.fitness()) {
                bestBat = bat;
            }
        }
        log.info("Лучшая летучая мышь обновлена: fitness={}", bestBat.fitness());
    }

    private double calculateFitness(double[] position) {
        return FitnessFunction.calculate(position);
    }

    public void run() {
        for (int iter = 0; iter < parameters.maxIterations(); iter++) {
            log.info("Итерация {}", iter);
            for (int i = 0; i < bats.length; i++) {
                BatDto bat = bats[i];
                double beta = random.nextDouble();
                double frequency = parameters.fMin() + (parameters.fMax() - parameters.fMin()) * beta;

                double[] newPosition = bat.position().clone();
                double[] newVelocity = bat.velocity().clone();

                for (int j = 0; j < parameters.dimensions(); j++) {
                    newVelocity[j] += (bat.position()[j] - bestBat.position()[j]) * frequency
                            + (random.nextDouble() - 0.5);
                    newPosition[j] += newVelocity[j];

                    if (newPosition[j] < parameters.min()) newPosition[j] = parameters.min();
                    if (newPosition[j] > parameters.max()) newPosition[j] = parameters.max();
                }

                log.debug("Летучая мышь {}: частота={}, новая позиция={}", i, frequency, newPosition);

                if (random.nextDouble() > bat.pulseRate()) {
                    double[] localSearchPosition = bestBat.position().clone();
                    for (int j = 0; j < parameters.dimensions(); j++) {
                        localSearchPosition[j] += bat.loudness() * (random.nextDouble() * 2 - 1);
                    }
                    double newFitness = calculateFitness(localSearchPosition);

                    if (newFitness < bat.fitness() && random.nextDouble() < bat.loudness()) {
                        newPosition = localSearchPosition;
                        log.debug("Летучая мышь {} улучшила позицию локальным поиском", i);
                    }
                }

                double currentFitness = calculateFitness(newPosition);
                if (currentFitness < bat.fitness() && random.nextDouble() < bat.loudness()) {
                    bats[i] = new BatDto(newPosition, newVelocity, frequency, bat.loudness() * parameters.alpha(),
                            bat.pulseRate() * (1 - Math.exp(-parameters.gamma() * iter)), currentFitness);
                    log.debug("Летучая мышь {} обновлена: fitness={}", i, currentFitness);
                }
            }
            updateBestBat();
        }
        log.info("Оптимизация завершена. Лучшее решение: {}", bestBat.position());
        log.info("Значение функции: {}", bestBat.fitness());
    }
}