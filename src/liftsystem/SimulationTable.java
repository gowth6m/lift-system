/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SimulationTable {
    private Building building;
    private final Random random;
    private boolean isSimulationOver;
    private int intialNumberOfPeople;
    private HashMap<Integer, Integer> generatedPeople; // Hashmap that maps people in current floor, with the total
    // with their respective number

    public SimulationTable(Building building, LiftAlgorithm LA, int initialNumberOfPeople) {
        this.intialNumberOfPeople = initialNumberOfPeople;
        this.random = new Random();
        this.isSimulationOver = false;
        this.building = building;
        this.generatedPeople = new HashMap<>();

        // Simulate for specified number of people in building
        this.building.setLift(LA);

        // Assign an instance of a new class
        if (building != null) {
            this.building = building;
        }

        // Simulate for specified number of people
        if (initialNumberOfPeople > 0) {
            simulateFor(this.intialNumberOfPeople);
        }
    }

    /**
     * Restarts the simulation with new variables
     */
    public SimulationTable restartSimulation() {
        // Reset work here
        this.isSimulationOver = false;
        this.building.clearAll();
        this.building.getLift().reset();

        simulateFor(this.intialNumberOfPeople);

        return this;
    }

    public SimulationTable setLiftCapacity(int capacity) {
        if (this.building.getLift() != null)
            this.building.getLift().setCapacity(capacity);

        return this;
    }

    /**
     * Runs the simulation
     *
     * @return the finished simulation table
     */
    public ArrayList<Table> run() {
        // Stores the states of simulation
        ArrayList<Table> temp = new ArrayList<>();
        LiftAlgorithm result;

        // Loop until the number of people in waiting list falls to zero       
        while (!this.building.getLift().finished()) {
            result = this.building.getLift().run();

            // This table stores the current state of the simulation
            Table currentStats = new Table();
            currentStats.currentCumulativeCost = result.getTotalCost();
            currentStats.currentWaitTime = result.getTotalWaitTime();
            currentStats.currentFloor = result.getCurrentFloor();
            currentStats.nextFloor = result.getNextFloor();
            currentStats.numberOfPeopleInTheLift = result.numberOfPeopleInLift();
            currentStats.currentLiftDirection = result.getLiftDirection();
            currentStats.peopleAlreadyServed = result.getBuilding().getNumberPeopleAlreadyPicked();
            currentStats.waitingToBePicked = result.getBuilding().getNumberOfPeopleWaitingToBePicked();

            // Calculate the new number of people on this floor
            if (generatedPeople.containsKey(currentStats.currentFloor)) {
                int t = generatedPeople.get(currentStats.currentFloor) - result.getNumberOfPeopleWhoGotInLiftOnThisFloor();

                // Update the current number if t < 0
                currentStats.numberOfPeoplePickedUp = result.getNumberOfPeopleWhoGotInLiftOnThisFloor();
                currentStats.numberOfPeopleOnTheCurrentFloor = Math.max(t, 0);
                generatedPeople.put(currentStats.currentFloor, currentStats.numberOfPeopleOnTheCurrentFloor);
            } else {
                // Acts as a failsafe
                currentStats.numberOfPeopleOnTheCurrentFloor = 0;
            }

            // Add the simulation stats to the array
            temp.add(currentStats);

            // Go to the next floor
            this.building.getLift().setCurrentFloor(currentStats.nextFloor);
        }

        // End simulation and return results
        this.isSimulationOver = true;

        // Return the temp
        return temp;
    }

    /**
     * @return boolean - Indicates whether the simulation is over or not
     */
    public boolean isSimulationOver() {
        return this.isSimulationOver;
    }

    /**
     * @param numberOfPeopleInBuilding - Simulate for specified number of people in the building
     */
    private SimulationTable simulateFor(int numberOfPeopleInBuilding) {
        for (int i = 0; i < numberOfPeopleInBuilding; ++i) {
            Person p = new Person(building);

            // Set the current floor
            int initialFloor = generateRandomInt(building.getMinFloorsInBuilding(), building.getMaxFloorsInBuilding());
            p.setInitialFloor(initialFloor);

            // Set the target floor
            int targetFloor = generateRandomInt(building.getMinFloorsInBuilding(), building.getMaxFloorsInBuilding());
            p.setTargetFloor(targetFloor);

            // Check that the initial floor and target floor are not equal
            while (initialFloor == targetFloor) {
                targetFloor = generateRandomInt(building.getMinFloorsInBuilding(), building.getMaxFloorsInBuilding());
                p.setTargetFloor(targetFloor);
            }

            // Update the number of people initially on that floor
            if (generatedPeople.containsKey(initialFloor)) {
                // Update number of people initially on this floor
                Integer newVal = generatedPeople.get(initialFloor) + 1;

                generatedPeople.put(initialFloor, newVal);
            } else {
                // Initialize the no. of people initially on this floor
                generatedPeople.put(initialFloor, 1);
            }

            this.building.addPersonWaitingToBePicked(p);
        }

        return this;
    }

    // --------------------------------------------------------------------------------------------//
    // TEST CASE (To compare between the 2 algorithms)
    // Manually assigning people to the lift
//    private SimulationTable simulateFor(int numberOfPeopleInBuilding) {
//        // person 1
//        Person p1 = new Person(building);
//        int initialFloor1 = 4;
//        p1.setInitialFloor(initialFloor1);
//        p1.setTargetFloor(2);
//        if (generatedPeople.containsKey(initialFloor1)) {
//            Integer newVal = generatedPeople.get(initialFloor1) + 1;
//            generatedPeople.put(initialFloor1, newVal);
//        } else {
//            generatedPeople.put(initialFloor1, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p1);
//
//        // person 2
//        Person p2 = new Person(building);
//        int initialFloor2 = 7;
//        p2.setInitialFloor(initialFloor2);
//        p2.setTargetFloor(15);
//        if (generatedPeople.containsKey(initialFloor2)) {
//            Integer newVal = generatedPeople.get(initialFloor2) + 1;
//            generatedPeople.put(initialFloor2, newVal);
//        } else {
//            generatedPeople.put(initialFloor2, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p2);
//
//        // person 3
//        Person p3 = new Person(building);
//        int initialFloor3 = 9;
//        p3.setInitialFloor(initialFloor3);
//        p3.setTargetFloor(14);
//        if (generatedPeople.containsKey(initialFloor3)) {
//            Integer newVal = generatedPeople.get(initialFloor3) + 1;
//            generatedPeople.put(initialFloor3, newVal);
//        } else {
//            generatedPeople.put(initialFloor3, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p3);
//
//        // person 4
//        Person p4 = new Person(building);
//        int initialFloor4 = 9;
//        p4.setInitialFloor(initialFloor4);
//        p4.setTargetFloor(12);
//        if (generatedPeople.containsKey(initialFloor4)) {
//            Integer newVal = generatedPeople.get(initialFloor4) + 1;
//            generatedPeople.put(initialFloor4, newVal);
//        } else {
//            generatedPeople.put(initialFloor4, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p4);
//
//        // person 5
//        Person p5 = new Person(building);
//        int initialFloor5 = 10;
//        p5.setInitialFloor(initialFloor5);
//        p5.setTargetFloor(17);
//        if (generatedPeople.containsKey(initialFloor5)) {
//            Integer newVal = generatedPeople.get(initialFloor5) + 1;
//            generatedPeople.put(initialFloor5, newVal);
//        } else {
//            generatedPeople.put(initialFloor5, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p5);
//
//        // person 6
//        Person p6 = new Person(building);
//        int initialFloor6 = 4;
//        p6.setInitialFloor(initialFloor6);
//        p6.setTargetFloor(3);
//        if (generatedPeople.containsKey(initialFloor6)) {
//            Integer newVal = generatedPeople.get(initialFloor6) + 1;
//            generatedPeople.put(initialFloor6, newVal);
//        } else {
//            generatedPeople.put(initialFloor6, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p6);
//
//        // person 7
//        Person p7 = new Person(building);
//        int initialFloor7 = 2;
//        p7.setInitialFloor(initialFloor7);
//        p7.setTargetFloor(15);
//        if (generatedPeople.containsKey(initialFloor7)) {
//            Integer newVal = generatedPeople.get(initialFloor7) + 1;
//            generatedPeople.put(initialFloor7, newVal);
//        } else {
//            generatedPeople.put(initialFloor7, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p7);
//
//        // person 8
//        Person p8 = new Person(building);
//        int initialFloor8 = 9;
//        p8.setInitialFloor(initialFloor8);
//        p8.setTargetFloor(16);
//        if (generatedPeople.containsKey(initialFloor8)) {
//            Integer newVal = generatedPeople.get(initialFloor8) + 1;
//            generatedPeople.put(initialFloor8, newVal);
//        } else {
//            generatedPeople.put(initialFloor8, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p8);
//
//        // person 9
//        Person p9 = new Person(building);
//        int initialFloor9 = 3;
//        p9.setInitialFloor(initialFloor9);
//        p9.setTargetFloor(11);
//        if (generatedPeople.containsKey(initialFloor9)) {
//            Integer newVal = generatedPeople.get(initialFloor9) + 1;
//            generatedPeople.put(initialFloor9, newVal);
//        } else {
//            generatedPeople.put(initialFloor9, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p9);
//
//        // person 10
//        Person p10 = new Person(building);
//        int initialFloor10 = 9;
//        p10.setInitialFloor(initialFloor10);
//        p10.setTargetFloor(16);
//        if (generatedPeople.containsKey(initialFloor10)) {
//            Integer newVal = generatedPeople.get(initialFloor10) + 1;
//            generatedPeople.put(initialFloor10, newVal);
//        } else {
//            generatedPeople.put(initialFloor10, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p10);
//
//        // ---------- //
//
//        // person 11
//        Person p11 = new Person(building);
//        int initialFloor11 = 3;
//        p11.setInitialFloor(initialFloor11);
//        p11.setTargetFloor(8);
//        if (generatedPeople.containsKey(initialFloor11)) {
//            Integer newVal = generatedPeople.get(initialFloor11) + 1;
//            generatedPeople.put(initialFloor11, newVal);
//        } else {
//            generatedPeople.put(initialFloor11, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p11);
//
//        // person 12
//        Person p12 = new Person(building);
//        int initialFloor12 = 7;
//        p12.setInitialFloor(initialFloor2);
//        p12.setTargetFloor(13);
//        if (generatedPeople.containsKey(initialFloor12)) {
//            Integer newVal = generatedPeople.get(initialFloor12) + 1;
//            generatedPeople.put(initialFloor12, newVal);
//        } else {
//            generatedPeople.put(initialFloor12, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p12);
//
//        // person 13
//        Person p13 = new Person(building);
//        int initialFloor13 = 9;
//        p13.setInitialFloor(initialFloor13);
//        p13.setTargetFloor(16);
//        if (generatedPeople.containsKey(initialFloor13)) {
//            Integer newVal = generatedPeople.get(initialFloor13) + 1;
//            generatedPeople.put(initialFloor13, newVal);
//        } else {
//            generatedPeople.put(initialFloor13, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p13);
//
//        // person 4
//        Person p14 = new Person(building);
//        int initialFloor14 = 4;
//        p14.setInitialFloor(initialFloor4);
//        p14.setTargetFloor(12);
//        if (generatedPeople.containsKey(initialFloor14)) {
//            Integer newVal = generatedPeople.get(initialFloor14) + 1;
//            generatedPeople.put(initialFloor14, newVal);
//        } else {
//            generatedPeople.put(initialFloor4, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p14);
//
//        // person 15
//        Person p15 = new Person(building);
//        int initialFloor15 = 10;
//        p15.setInitialFloor(initialFloor15);
//        p15.setTargetFloor(17);
//        if (generatedPeople.containsKey(initialFloor15)) {
//            Integer newVal = generatedPeople.get(initialFloor15) + 1;
//            generatedPeople.put(initialFloor15, newVal);
//        } else {
//            generatedPeople.put(initialFloor15, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p15);
//
//        // person 16
//        Person p16 = new Person(building);
//        int initialFloor16 = 4;
//        p16.setInitialFloor(initialFloor16);
//        p16.setTargetFloor(5);
//        if (generatedPeople.containsKey(initialFloor16)) {
//            Integer newVal = generatedPeople.get(initialFloor16) + 1;
//            generatedPeople.put(initialFloor16, newVal);
//        } else {
//            generatedPeople.put(initialFloor16, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p16);
//
//        // person 17
//        Person p17 = new Person(building);
//        int initialFloor17 = 2;
//        p17.setInitialFloor(initialFloor17);
//        p17.setTargetFloor(18);
//        if (generatedPeople.containsKey(initialFloor17)) {
//            Integer newVal = generatedPeople.get(initialFloor17) + 1;
//            generatedPeople.put(initialFloor17, newVal);
//        } else {
//            generatedPeople.put(initialFloor17, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p17);
//
//        // person 8
//        Person p18 = new Person(building);
//        int initialFloor18 = 9;
//        p18.setInitialFloor(initialFloor18);
//        p18.setTargetFloor(20);
//        if (generatedPeople.containsKey(initialFloor18)) {
//            Integer newVal = generatedPeople.get(initialFloor18) + 1;
//            generatedPeople.put(initialFloor18, newVal);
//        } else {
//            generatedPeople.put(initialFloor18, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p18);
//
//        // person 19
//        Person p19 = new Person(building);
//        int initialFloor19 = 3;
//        p19.setInitialFloor(initialFloor19);
//        p19.setTargetFloor(11);
//        if (generatedPeople.containsKey(initialFloor19)) {
//            Integer newVal = generatedPeople.get(initialFloor19) + 1;
//            generatedPeople.put(initialFloor19, newVal);
//        } else {
//            generatedPeople.put(initialFloor19, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p19);
//
//        // person 20
//        Person p20 = new Person(building);
//        int initialFloor20 = 6;
//        p20.setInitialFloor(initialFloor20);
//        p20.setTargetFloor(19);
//        if (generatedPeople.containsKey(initialFloor20)) {
//            Integer newVal = generatedPeople.get(initialFloor20) + 1;
//            generatedPeople.put(initialFloor20, newVal);
//        } else {
//            generatedPeople.put(initialFloor20, 1);
//        }
//        this.building.addPersonWaitingToBePicked(p20);
//
//        return this;
//    }
    // --------------------------------------------------------------------------------------------//

    /**
     * Gets the generated list of people each with their respective number
     *
     * @return
     */
    public HashMap<Integer, Integer> GetGeneratedPeopleList() {
        return this.generatedPeople;
    }

    /**
     * Generate a random integer within a range inclusive of min, max
     *
     * @param min - The minimum integer
     * @param max - The maximum integer
     * @return int - The random int
     */
    private int generateRandomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * This is the actual simulation table
     * Every member of this class is public and it does not have methods.
     */
    public class Table {
        public int currentFloor;
        public int numberOfPeopleOnTheCurrentFloor;
        public int numberOfPeopleInTheLift;
        public int nextFloor;
        public int currentCumulativeCost;
        public int currentWaitTime;
        public boolean currentLiftDirection;
        public int peopleAlreadyServed;
        public String description;
        public int waitingToBePicked;
        public int numberOfPeoplePickedUp;

        private Table() {
            this.waitingToBePicked = 0;
            this.description = "";
            this.peopleAlreadyServed = 0;
            this.currentLiftDirection = true;
            this.currentWaitTime = 0;
            this.currentCumulativeCost = 0;
            this.nextFloor = 0;
            this.numberOfPeopleInTheLift = 0;
            this.numberOfPeopleOnTheCurrentFloor = 0;
            this.currentFloor = 0;
            this.numberOfPeoplePickedUp = 0;
        }
    }
}
