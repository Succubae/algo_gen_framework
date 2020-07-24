package fr.rgary.algo.gen.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static fr.rgary.algo.gen.framework.BaseStaticValues.MUTATE_INDIVIDUAL_CHANGE;
import static fr.rgary.algo.gen.framework.BaseStaticValues.POPULATION;
import static fr.rgary.algo.gen.framework.BaseStaticValues.RANDOM;

public class GeneticAlgorithm {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticAlgorithm.class);

    public static long totalBreedTime = 0;
    public static long totalMutateTime = 0;

    public static float prevBestFit = -1;

    private static Individual best;

    public GeneticAlgorithm() {
//        this.polygons = Track.instance.zones;
    }

    public static void natureIsBeautiful() {
        POPULATION.parallelStream().forEach(IndividualFitness::calcFitness);
        Collections.sort(POPULATION);
        PrintUtil.logPopulationNumberAndFitnessAsTable(POPULATION);
        best = POPULATION.get(0);
        if (best.fitnessValue < prevBestFit) {
            LOGGER.info("What ? ");
        }
        prevBestFit = best.fitnessValue;
        List<Individual> localPopulation = new ArrayList<>();
        selection(localPopulation);
        mutateAll(localPopulation);
        localPopulation.add(0, best);
        POPULATION = new ArrayList<>(localPopulation);
        POPULATION.forEach(Individual::reset);
    }

    private static Individual selectBasedOnFitness(float fitnessTotal) {
        double rand = RANDOM.nextDouble() * fitnessTotal;
        double runningSum = 0;
        for (Individual individual : POPULATION) {
            runningSum += individual.fitnessValue;
            if (runningSum > rand) {
                return individual;
            }
        }
        throw new InternalError("I HAVE NOTHING TO DO HERE !!!!");
    }

    private static List<Individual> selection(List<Individual> localPopulation) {
        float fitnessTotal = Fitness.getTotalFitness();
        while (localPopulation.size() < Processor.toSelect) {
            localPopulation.add(new Individual(selectBasedOnFitness(fitnessTotal)));
        }
        while (localPopulation.size() < POPULATION.size() - 1) {
            int i = RANDOM.nextInt(POPULATION.size());
            int j = RANDOM.nextInt(POPULATION.size());
            while (j == i) {
                j = RANDOM.nextInt(POPULATION.size());
            }
            Individual firstParent = POPULATION.get(i);
            Individual secondParent = POPULATION.get(j);

            Individual child = firstParent.breedChild(secondParent);
            localPopulation.add(child);
        }
        return localPopulation;
    }

    private static void mutateAll(List<Individual> localPopulation) {
        long start = System.nanoTime();
        localPopulation.parallelStream().filter(o -> RANDOM.nextDouble() < MUTATE_INDIVIDUAL_CHANGE).forEach(Individual::mutate);
        totalMutateTime += (System.nanoTime() - start);
    }


}
