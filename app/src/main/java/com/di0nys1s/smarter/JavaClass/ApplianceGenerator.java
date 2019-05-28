package com.di0nys1s.smarter.JavaClass;

import java.util.Random;

public class ApplianceGenerator {
    private double randomFridge;
    private  double randomWM;
    private double randomAC;
    private int randomHour;
    private int hour = 0;

    public double[] getRandomFridgeUsage() {

        double minFridge = 0.3;
        double maxFridge = 0.8;
        double fridge = minFridge + Math.random() * (maxFridge - minFridge);
        randomFridge = Math.round(fridge * 100.00) / 100.0;

        double[] fridgeUsages = new double[24];

        while (hour <= 23) {
            for (int i  = hour; i <= hour; i++) {
                fridgeUsages[i] = randomFridge;
            }
            hour++;
        }

        return fridgeUsages;
    }

    public double[] getRandomWmUsage() {
        double minWM = 0.4;
        double maxWM = 1.3;
        double wm = minWM + Math.random() * (maxWM - minWM);
        randomWM = Math.round(wm * 100.00) / 100.0;
        double zeroWM = 0.0;
        int maxHour = 21;
        int minHour = 6;

        double[] wmUsages = new double[24];

        boolean isHour = true;
        while (isHour) {
            double h = (minHour + (Math.random() * (maxHour - minHour + 1)));
            randomHour = (int) (Math.round(h * 100) / 100);
            int hour = 0;
            while (hour <= 23) {
                for (int i  = hour; i <= hour; i++) {
                    wmUsages[i] = zeroWM;
                }
                hour++;
                if (randomHour == hour && randomHour <= 19) {
                    for (int counter = 0; counter <= 2; counter++) {
                        for (int i  = randomHour; i <= 19; i++) {
                            wmUsages[i] = randomWM;
                        }
                        isHour = false;
                        randomHour++;
                        hour++;
                    }
                }
                else if (randomHour == 20) {
                    for (int counter = 0; counter <= 1; counter++) {
                        wmUsages[19] = randomWM;
                        isHour = false;
                        randomHour++;
                        hour++;
                    }
                }
                else if (randomHour == 21) {
                    wmUsages[20] = randomWM;
                    isHour = false;
                    randomHour++;
                    hour++;
                }
            }
        }

        return wmUsages;
    }

    public double[] getRandomAcUsage(Double temperature) {
        double minAC = 1;
        double maxAC = 5;
        double ac = minAC + Math.random() * (maxAC - minAC);
        randomAC = Math.round(ac * 100.00) / 100.0;
        double zeroAC = 0.0;
        int maxHourAC = 23;
        int minHourAC = 9;
        Random random = new Random();
        Boolean isAcOpen;
        int counter = 0;

        double[] acUsages = new double[24];

        while (hour <= maxHourAC) {
            if (temperature < 20) {
                while (hour <= maxHourAC) {
                    for (int i  = hour; i <= maxHourAC; i++) {
                        acUsages[i] = zeroAC;
                    }
                    hour++;
                }
            } else {
                if (hour < minHourAC) {
                    for (int i  = hour; i < minHourAC; i++) {
                        acUsages[i] = zeroAC;
                    }
                    hour++;
                }
                if (hour >= minHourAC) {
                    isAcOpen = random.nextBoolean();
                    while (hour <= maxHourAC) {
                        while(counter < 10 && hour <= 23) {
                            if (isAcOpen) {
                                for (int i  = hour; i <= maxHourAC; i++) {
                                    acUsages[i] = randomAC;
                                }
                                hour++;
                                counter++;
                                isAcOpen = random.nextBoolean();
                                continue;
                            } else if (!isAcOpen) {
                                for (int i  = hour; i <= maxHourAC; i++) {
                                    acUsages[i] = zeroAC;
                                }
                                hour++;
                                isAcOpen = random.nextBoolean();
                            }
                        }
                        if (hour <= maxHourAC) {
                            while (hour <= maxHourAC) {
                                for (int i  = hour; i <= maxHourAC; i++) {
                                    acUsages[i] = zeroAC;
                                }
                                hour++;
                            }
                        }
                    }

                }
            }
        }
        return acUsages;
    }
}
