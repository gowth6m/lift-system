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

    public SimulationTable(Building building, LiftAlgorithm LA, int intialNumberOfPeople) {
        this.intialNumberOfPeople = intialNumberOfPeople;
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
        if (intialNumberOfPeople > 0) {
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
                currentStats.numberOfPeopleOnTheCurrentFloor = t < 0 ? 0 : t;
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
        //-------------------------------------------//
        //This is a test case
        // Person p = new Person(building)

        // The person is initially at floor 6 an wants to go to floor 1
        // the lift starts at floor 7, so the total cost for delivering this person
        // will be 33, and the wait time is 27
        // p.setInitialFloor(6);
        // p.setTargetFloor(1);
        // this.building.addPersonWaitingToBePicked(p); // Adds the person to wait list

        // Set the buildings initial floor.This is the floor that the simulation will start in
        // building.getLift().setCurrentFloor(7);

        // return this;
        //------------------------------------------//

        for (int i = 0; i < numberOfPeopleInBuilding; ++i) {
            Person p = new Person(building);

            // Set the current floor
            int initialFloor = generateRandomInt(building.getMinFloorsInBuilding(), building.getMaxFloorsInBuilding());
            p.setInitialFloor(initialFloor);

            // Set the target flooor
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
     * This is a dirty way of creating a structure/struct
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
