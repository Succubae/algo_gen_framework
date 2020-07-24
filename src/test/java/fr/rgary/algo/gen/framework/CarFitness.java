package fr.rgary.algo.gen.framework;

import static fr.rgary.algo.gen.framework.BaseStaticValues.POPULATION;

public class CarFitness extends IndividualFitness {
    public static void calcFitness(CarIndividual car) {
//        throw new InternalError("NOT YET IMPLEMENTED");
        int carInZone = Constant.TRACK.getZoneNumberPerPosition(car.position);
        if (carInZone < car.maxZoneEntered || carInZone == -1) {
            LOGGER.warn("U-turns are BAD. You were in zone {} but returned to {}", car.maxZoneEntered, carInZone);
            car.active = false;
            car.fitnessValue = 0;
            car.maxZoneEntered = carInZone;
            return;
        }
        car.maxZoneEntered = carInZone;
        car.fitnessValue = Math.max(0, carInZone * 100) - (car.moveDone / 10f) + 450;

    }

    public static float getTotalFitness() {
        float total = 0;
        for (Individual car : POPULATION) {
            total += car.fitnessValue;
        }
        return total;
    }
}