package fr.rgary.algo.gen.framework;

import java.util.List;
import java.util.Random;

public class BaseStaticValues {
    public static final Random RANDOM = new Random();

    public static Double MUTATE_INDIVIDUAL_CHANGE = 0.3;
    public static Double MUTATE_CHANGE = 0.5;

    public static List<Individual> POPULATION;

    public BaseStaticValues(List<Individual> population, Double mutateIndividualChange, Double mutateChange) {
        BaseStaticValues.POPULATION = population;
        BaseStaticValues.MUTATE_INDIVIDUAL_CHANGE = mutateIndividualChange;
        BaseStaticValues.MUTATE_CHANGE = mutateChange;
    }

    public static void setPOPULATION(final List<Individual> POPULATION) {
        BaseStaticValues.POPULATION = POPULATION;
    }
}