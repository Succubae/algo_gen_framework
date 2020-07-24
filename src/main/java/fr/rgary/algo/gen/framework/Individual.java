package fr.rgary.algo.gen.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Individual.
 */
public abstract class Individual implements Comparable<Individual> {

    Logger LOGGER = LoggerFactory.getLogger(Individual.class);

    private int id;
    public float fitnessValue = 0;
    private static int maxId = 0;
    private boolean active;


    protected Individual() {
        //hidden constructor
        synchronized (Individual.class) {
            Individual.maxId++;
        }
        this.active = true;
        this.id = maxId;
        this.fitnessValue = 0;
    }

    protected Individual(Individual other) {
        super();
    }

    public void reset() {
        throw new RuntimeException("Re-implement me !");
    }

    public void mutate() {
        throw new RuntimeException("Re-implement me !");
    }

    public Individual breedChild(Individual parent) {
        throw new RuntimeException("Re-implement me !");
    }

    public void calcFitness() {
        throw new RuntimeException("Re-implement me !");
    }

    public int getId() {
        return id;
    }

    public float getFitnessValue() {
        return fitnessValue;
    }

    public Individual setFitnessValue(final float fitnessValue) {
        this.fitnessValue = fitnessValue;
        return this;
    }


    public synchronized int getMaxNumberAndIncrement() {
        Individual.maxId += 1;
        return Individual.maxId - 1;
    }

    public void updateNumber() {
        this.id = getMaxNumberAndIncrement();
    }


    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void reactivate() {
        this.active = true;
    }

    @Override
    public int compareTo(final Individual o) {
        if (o.fitnessValue == this.fitnessValue)
            return 0;
        return o.fitnessValue > this.fitnessValue ? 1 : -1;
    }
}
