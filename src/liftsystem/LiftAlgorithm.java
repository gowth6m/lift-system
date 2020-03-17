/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem;

import java.util.ArrayList;

/**
 * An abstract class that should be implemented for lift system
 */
public interface LiftAlgorithm {
    
    /**
     * Runs the simulation for the specified building
     */
    public LiftAlgorithm run();

    /**
     * @return int - the current cumulative cost in the lift
     */
    public int getTotalCost();
    
    /**
     * @return int - the current wait time
     */
    public int getTotalWaitTime();
    
    /**
     * @return int - The number of people on current floor
     */
    public int getNumberOfPeopleOnThisFloor();
    
    /**
     * @return int - the capacity of the lift
     */
    public int getCapacity();
    
    /**
     * @param capacity int - used to set the capacity of the lift
     */
    public void setCapacity(int capacity);
    
    /**
     * @return int - The current floor the lift is in
     */
    public int getCurrentFloor();
    
    /**
     * @param currentFloor int - The current floor the lift is in
     */
    public void setCurrentFloor(int currentFloor);
    
    /**
     * @return ArrayList<> - ArrayList of persons in the lift
     */
    public ArrayList<Person> getPeopleInLift();
    
    /**
     * @param person Person- adds a person to the list of people currently in the building
     */
    public void addPerson(Person person);
    
    /**
     * @param person Person - Removes the person from the lift
     */
    public void removePerson(Person person);
    
    /**
     * Removes every person from the lift
     */
    public void clearAllPeopleFromLift();
    
    /**
     * Resets the class
     */
    public void reset();
    
    /**
     * @return int - The number of people in the lift
     */
    public int numberOfPeopleInLift();
    
    /**
     * Specifies the lift direction up
     * @return boolean - true for up false for down
     */
    public boolean getLiftDirection();
    
    /**
     * Goes up by one floor
     */
    public void goUp();
    
    /**
     * Goes down by one floor
     */
    public void goDown();
    
    /**
     * @return boolean - true if lift is full
     */
    public boolean isFull();
    
    /**
     * @return boolean - true if lift is empty
     */
    public boolean isEmpty();
    
    /**
     * @return boolean - true if the lift is in the ground floor
     */
    public boolean isAtTheBottom();
    
     /**
     * @return boolean - true if the lift is in the top floor
     */
    public boolean isAtTheTop();
    
    /**
     * Gets the building the lift is in
     * @return Building - The building the lift is in
     */
    public Building getBuilding();
    
    /**
     * @param building - The building the lift is in
     */
    public void setBuilding(Building building);
    
    /**
     * @return int - This is the simulation counter
     */
    public int getCounter();
    
    /**
     * @return boolean - Indicates whether the lift has finished delivering all the people 
     */
    public boolean finished();
    
    /**
     * @return int - Returns the number of people in elevator
     */
    public int currentNumberOfPeopleInLift();
    
    /**
     * @return int - The next floor the elevator is going to
     */
    public int getNextFloor();
    
    /**
     * @param floor int - The next floor the lift is going to
     */
    public void setNextFloor(int floor);
}
