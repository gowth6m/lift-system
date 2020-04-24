/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem;

import java.util.ArrayList;

/**
 * An abstract class that provides a boilerplate for lift algorithms
 */
public abstract class LiftAlgorithm {
    protected int counter;
    protected int capacity;
    protected int currentFloor;
    protected int totalWaitTime;
    protected int totalNumberOfPeopleWhoGotInToTheLiftOnThisFloor;
    protected int nextFloor;
    protected int totalCost;
    protected boolean liftDirection;
    protected ArrayList<Person> inTheLift;
    protected Building building; // Instance of the building class

    /**
     * The constructor it initializes this class
     */
    public LiftAlgorithm() {
        this.capacity = 10;
        this.inTheLift = new ArrayList<>();
        this.currentFloor = 1;
        this.totalWaitTime = 0;
        this.totalNumberOfPeopleWhoGotInToTheLiftOnThisFloor = 0;

        this.totalCost = 0;
        this.liftDirection = true;
        this.building = new Building();
        this.counter = 0;

        setNextFloor(this.currentFloor);
    }

    /**
     * Must be overidden this is where the lift logic comes in
     *
     * @return LiftAlgorithm being used from extended subclass
     */
    public LiftAlgorithm run() {
        return null;
    }

    /**
     * Gets the total cost
     *
     * @return int
     */
    public int getTotalCost() {
        return this.totalCost;
    }

    public int getTotalWaitTime() {
        return this.totalWaitTime;
    }

    public int getNumberOfPeopleWhoGotInLiftOnThisFloor() {
        return this.totalNumberOfPeopleWhoGotInToTheLiftOnThisFloor;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        }
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        // If current floor is less than minimum building floor set the current floor
        // to be equal to minimum floor
        if (currentFloor < this.building.getMinFloorsInBuilding()) {
            currentFloor = this.building.getMinFloorsInBuilding();
        } else if (currentFloor > this.building.getMaxFloorsInBuilding()) {
            // If current floor is less than minimum building floor set the current floor
            // to be equal to maximum floor
            currentFloor = this.building.getMaxFloorsInBuilding();
        }

        // Update the current floor number
        this.currentFloor = currentFloor;
        this.setNextFloor(this.currentFloor);
    }

    public ArrayList<Person> getPeopleInLift() {
        return this.inTheLift;
    }

    public void addPerson(Person person) {
        if (person != null && !this.isFull()) {
            this.inTheLift.add(person);
        }
    }

    public void removePerson(Person person) {
        if (person != null) {
            this.inTheLift.remove(person);
        }
    }

    public void clearAllPeopleFromLift() {
        this.inTheLift.clear();
    }

    public void reset() {
        this.setCurrentFloor(this.building.getMinFloorsInBuilding());
        this.counter = 0;
        this.totalCost = 0;
        this.totalWaitTime = 0;
        this.clearAllPeopleFromLift();
        this.liftDirection = true;
    }

    public int numberOfPeopleInLift() {
        return this.inTheLift.size();
    }


    public boolean getLiftDirection() {
        return this.liftDirection;
    }

    public void goUp() {
        this.liftDirection = true;

        this.setNextFloor(this.currentFloor);
    }

    public void goDown() {
        this.liftDirection = false;

        this.setNextFloor(this.currentFloor);
    }

    public boolean isFull() {
        return this.inTheLift.size() == this.capacity;
    }

    public boolean isEmpty() {
        return this.inTheLift.isEmpty();
    }


    public boolean isAtTheBottom() {
        return this.currentFloor == this.building.getMinFloorsInBuilding();
    }


    public boolean isAtTheTop() {
        return this.currentFloor == this.building.getMaxFloorsInBuilding();
    }


    public Building getBuilding() {
        return this.building;
    }


    public void setBuilding(Building building) {
        if (building != null) {
            this.building = building;
        }
    }

    public int getCounter() {
        return this.counter;
    }

    public boolean finished() {
        return (this.building.getNumberOfPeopleWaitingToBePicked() == 0 &&
                this.currentNumberOfPeopleInLift() == 0);
    }

    public int currentNumberOfPeopleInLift() {
        return this.inTheLift.size();
    }

    public int getNextFloor() {
        return this.nextFloor;
    }


    public void setNextFloor(int theCurrentFloor) {
        // Check if we've reached the bottom or top of the building
        if ((this.currentFloor == this.building.getMinFloorsInBuilding() && !this.liftDirection)  // If the lift is going down and we've reached the bottom
                || (this.currentFloor == this.building.getMaxFloorsInBuilding() && this.liftDirection)) // If the lift is going up and we've reached the top
        {
            // We reached the top or bottom, reverse the direction
            if (this.liftDirection) {
                // We were going up reverse the direction
                this.liftDirection = false;
                this.nextFloor -= 1;
            } else {
                // We were going down reverse the direction
                this.liftDirection = true;
                this.nextFloor += 1;
            }
        } else {
            // We've not yet reached the bottom or top, set the next floor
            if (this.liftDirection) {
                // If it is up then the next floor is up by 1
                this.nextFloor = ++theCurrentFloor;
            } else {
                // If it is up then the next floor is down by 1
                this.nextFloor = --theCurrentFloor;
            }
        }
    }
}