package fr.rgary.algo.gen.framework;

import org.ejml.data.DMatrixRMaj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.rgary.algo.gen.framework.BaseStaticValues.MUTATE_CHANGE;
import static fr.rgary.algo.gen.framework.BaseStaticValues.RANDOM;
import static fr.rgary.algo.gen.framework.Constants.FIRST_HIDDEN_LAYER_SIZE;
import static fr.rgary.algo.gen.framework.Constants.INPUT_LAYER_SIZE;
import static fr.rgary.algo.gen.framework.Constants.NUM_LABEL;
import static fr.rgary.algo.gen.framework.Constants.SECOND_HIDDEN_LAYER_SIZE;

public class CarIndividual extends Individual {
    public List<DMatrixRMaj> allThetas;
    public DMatrixRMaj theta1 = new DMatrixRMaj(FIRST_HIDDEN_LAYER_SIZE, INPUT_LAYER_SIZE + 1);
    public DMatrixRMaj theta2 = new DMatrixRMaj(SECOND_HIDDEN_LAYER_SIZE, FIRST_HIDDEN_LAYER_SIZE + 1);
    public DMatrixRMaj theta3 = new DMatrixRMaj(NUM_LABEL, SECOND_HIDDEN_LAYER_SIZE + 1);

    public CarIndividual() {
        super();
    }

    public CarIndividual(final Individual other) {
        super(other);
        this.startPoint = Track.instance.startPoint;
        this.position = this.startPoint.clone();
        this.theta1 = new DMatrixRMaj(((CarIndividual)other).theta1);
        this.theta2 = new DMatrixRMaj(((CarIndividual)other).theta2);
        this.theta3 = new DMatrixRMaj(((CarIndividual)other).theta3);
        this.allThetas = new ArrayList<>(Arrays.asList(this.theta1, this.theta2, this.theta3));

    }

    @Override
    public void mutate() {
        this.updateNumber();
        int m = 0;
        int no_m = 0;
        this.fitnessValue = 0;
        for (int i = 0; i < this.allThetas.size(); i++) {
            for (int j = 0; j < this.allThetas.get(i).data.length; j++) {
                if (RANDOM.nextDouble() < MUTATE_CHANGE) {
                    m++;
                    this.allThetas.get(i).data[j] += NeuralNetwork.getRandomWithinBoundaries();
                    this.allThetas.get(i).data[j] = NeuralNetwork.limitThetaValueToBoundaries(this.allThetas.get(i).data[j]);
                } else {
                    no_m++;
                }
            }
        }
    }

    public CarIndividual breedChild(Individual individual) {
        CarIndividual parentAsCar = (CarIndividual) individual;
        CarIndividual child = new CarIndividual();
        for (int i = 0; i < this.allThetas.size(); i++) {
            for (int j = 0; j < this.allThetas.get(i).data.length; j++) {
                child.allThetas.get(i).data[j] = RANDOM.nextDouble() < 0.5 ? this.allThetas.get(i).data[j] : parentAsCar.allThetas.get(i).data[j];
            }
        }
        return child;
    }

    @Override
    public void calcFitness() {
//        throw new InternalError("NOT YET IMPLEMENTED");
        int carInZone = Constant.TRACK.getZoneNumberPerPosition(this.position);
        if (carInZone < this.maxZoneEntered || carInZone == -1) {
            LOGGER.warn("U-turns are BAD. You were in zone {} but returned to {}", this.maxZoneEntered, carInZone);
            this.deactivate();
            this.fitnessValue = 0;
            this.maxZoneEntered = carInZone;
            return;
        }
        this.maxZoneEntered = carInZone;
        this.fitnessValue = Math.max(0, carInZone * 100) - (this.moveDone / 10f) + 450;

    }
}