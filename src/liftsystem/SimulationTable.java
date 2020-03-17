/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem;

import java.util.ArrayList;
import java.util.Random;

public class SimulationTable {
    private Building building;
    private final Random random;
    private boolean isSimulationOver;
    private int intialNumberOfPeople;
    
    public SimulationTable(Building building, LiftAlgorithm LA, int intialNumberOfPeople){
        this.intialNumberOfPeople = intialNumberOfPeople;
        this.random = new Random();
        this.isSimulationOver = false;
        this.building = building;
        
        // Simulate for specified number of people in building
        this.building.setLift(LA);
        
        // Assign an instance of a new class
        if(building != null){
            this.building  = building;
        }
        
        // Simulate for specified number of people
        if(intialNumberOfPeople > 0){
            simulateFor(this.intialNumberOfPeople);
        }
    }
    
    /**
     * Restarts the simulation with new variables
     */
    public SimulationTable restartSimulation(){
        // Reset work here
        this.isSimulationOver = false;
        this.building.clearAll();
        this.building.getLift().reset();
                
        simulateFor(this.intialNumberOfPeople);
        
        return this;
    }
    
    /**
     * Runs the simulation
     * @return the finished simulation table
     */
    public ArrayList<Table> run(){
        // Stores the states of simulation
        ArrayList<Table> temp = new ArrayList<>();
        LiftAlgorithm result;
        
        // Loop until the number of people in waiting list falls to zero       
        while(!this.building.getLift().finished()){
           result = this.building.getLift().run();
           
           Table currentStats =  new Table();
           currentStats.currentCumulativeCost = result.getTotalCost();
           currentStats.currentWaitTime = result.getTotalWaitTime();
           currentStats.currentFloor = result.getCurrentFloor();
           currentStats.nextFloor = result.getNextFloor();
           currentStats.numberOfPeopleInTheLift = result.numberOfPeopleInLift();
           currentStats.numberOfPeopleOnTheCurrentFloor = result.getNumberOfPeopleOnThisFloor();
           currentStats.currentLiftDirection = result.getLiftDirection();
           currentStats.peopleAlreadyServed = result.getBuilding().getNumberPeopleAlreadyPicked();
           currentStats.waitingToBePicked = result.getBuilding().getNumberOfPeopleWaitingToBePicked();
           
           // Add the simulation stats to the array
           temp.add(currentStats);
           
           // Go to the next floor
           this.building.getLift().setNextFloor(currentStats.nextFloor);
        }
        
        // End simulation and return results
        this.isSimulationOver = true;
        
        // Return the temp
        return temp;
    }

    /**
     * @return boolean - Indicates whether the simulation is over or not
     */
    public boolean isSimulationOver(){
        return this.isSimulationOver;
    }
    
    /**
     * 
     * @param numberOfPeopleInBuilding - Simulate for specified number of people in the building
     */
    private SimulationTable simulateFor(int numberOfPeopleInBuilding){
        for(int i = 0; i < numberOfPeopleInBuilding; ++i){
            Person p = new Person(building);
            
            // Set the current floor
            p.setCurrentFloor(
                    generateRandomInt(
                            building.getMinFloorsInBuilding(), 
                            building.getMaxFloorsInBuilding()
                    )
            );
            
            // Set the target flooor
            p.setTargetFloor(
                    generateRandomInt(
                            building.getMinFloorsInBuilding(), 
                            building.getMaxFloorsInBuilding()
                    )
            );
            
            this.building.addPersonWaitingToBePicked(p);
        }
        
        return this;
    }
   
    /**
     * Generate a random integer within a range inclusive of min, max
     * @param min - The minimum integer
     * @param max - The maximum integer
     * @return int - The random int
     */
    private int generateRandomInt(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }
    
    /**
     * This is the actual simulation table
     * Every member of this class is public and it does not have methods.
     * This is a dirty way of creating a structure/struct
     */
    public class Table{
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
        }
    }
}
