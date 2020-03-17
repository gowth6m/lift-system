/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem;

/**
 * This class represents people in various floors
 */
public class Person {
    private int targetFloor;
    private int currentFloor;
    private int waitTime;
    private Building building;
    
    public Person(Building building){
        this.waitTime = 0;
        this.currentFloor = 1;
        this.targetFloor = 1;
        
        if(building == null){
            this.building = new Building();
        }
        
        this.building = building;
    }
    
    // Accessor functions
    public int getTargetFloor(){
        return this.targetFloor;
    }
    
    public void setTargetFloor(int floor){
        if(floor >= building.getMinFloorsInBuilding() || floor <= building.getMaxFloorsInBuilding()){
            this.targetFloor = floor;
        }
    }
    
    /**
     * @return int -  Gets the current floor the person is in
     */
    public int getCurrentFloor(){
        return this.currentFloor;
    }
    
    /**
     * 
     * @param floor - The current floor from where she / he is to be picked
     */
    public void setCurrentFloor(int floor){
        if(floor >= building.getMinFloorsInBuilding()|| floor <= building.getMaxFloorsInBuilding()){
            this.currentFloor = floor;
        }
    }
    
    /**
     * @return int the time the person has waited for the lift
     */
    public int getWaitTime(){
        return this.waitTime;
    }
    
    /**
     *
     * @param waitTime - The time the person has waited for the lift
     */
    public void setWaitTime(int waitTime){
        this.waitTime = Math.abs(waitTime);
    }
}
