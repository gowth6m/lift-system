/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem;

import java.util.ArrayList;

public final class Building {
    private final int minFloor;
    private int maxFloor;
    private LiftAlgorithm lift;
    private final ArrayList<Person> waitingToBePicked;
    private final ArrayList<Person> alreadyPicked;

    public Building() {
        this.minFloor = 1;
        this.maxFloor = 20;
        this.alreadyPicked = new ArrayList<>();
        this.waitingToBePicked = new ArrayList<>();
        this.lift = null;
    }

    public Building(int maxFloor) {
        this.minFloor = 1;
        this.maxFloor = 20;
        this.alreadyPicked = new ArrayList<>();
        this.waitingToBePicked = new ArrayList<>();
        this.lift = null;

        this.setMaxFloorsInBuilding(maxFloor);
    }

    /**
     * @return the lift that is being used
     */
    public LiftAlgorithm getLift() {
        return this.lift;
    }

    /**
     * @param LA - The Lift to be used
     */
    public void setLift(LiftAlgorithm LA) {
        if (LA != null) {
            this.lift = LA;
            this.lift.setBuilding(this);
        }
    }

    /**
     * @return int - The maximum number of floors the building has
     */
    public int getMaxFloorsInBuilding() {
        return this.maxFloor;
    }

    /**
     * @param floors int - the maximum floors of the building
     */
    public void setMaxFloorsInBuilding(int floors) {
        if (floors > this.minFloor) {
            this.maxFloor = floors;
        }
    }

    /**
     * @return int - The minimum floors in the building, the default is 1
     */
    public int getMinFloorsInBuilding() {
        return this.minFloor;
    }

    /**
     * @return int - The number of people currently in the building
     */
    public int getNumberOfPeopleInBuilding() {
        return this.lift.getPeopleInLift().size() + this.alreadyPicked.size() + this.waitingToBePicked.size();
    }

    /**
     * @return int - The number of people in the lift
     */
    public int getNumberOfPeopleInTheLift() {
        return this.lift.getPeopleInLift().size();
    }

    /**
     * @return int - The number of people waiting to be picked
     */
    public int getNumberOfPeopleWaitingToBePicked() {
        return this.waitingToBePicked.size();
    }

    /**
     * @return int - The list of people waiting to be picked
     */
    public ArrayList<Person> getPeopleWaitingToBePicked() {
        return this.waitingToBePicked;
    }

    /**
     * @param person - A person waiting to be picked
     */
    public void addPersonWaitingToBePicked(Person person) {
        if (person != null) {
            this.waitingToBePicked.add(person);
        }
    }

    /**
     * @param person - The person to be removed from waiting list
     */
    public void removePersonWaitingToBePicked(Person person) {
        this.waitingToBePicked.remove(person);
    }

    /**
     * Clears the list of people waiting to be picked
     */
    public void clearPeopleWaitingToBePicked() {
        this.waitingToBePicked.clear();
    }

    /**
     * @return int- The number of people already picked
     */
    public int getNumberPeopleAlreadyPicked() {
        return this.alreadyPicked.size();
    }

    /**
     * @return ArrayList<> a list of people that have already been picked and delivered to their target floor
     */
    public ArrayList<Person> getPeopleAlreadyPicked() {
        return this.alreadyPicked;
    }

    /**
     * Clears the list of people to be picked
     */
    public void clearPeopleAlreadyPicked() {
        this.alreadyPicked.clear();
    }

    /**
     * @param person - A person that has already been picked
     */
    public void addPersonToAlreadyPicked(Person person) {
        if (person != null) {
            this.alreadyPicked.add(person);
        }
    }

    /**
     * @return ArrayList<> - The people currently in the lift
     */
    public ArrayList<Person> getPeopleInLift() {
        return this.lift.getPeopleInLift();
    }

    /**
     * Clears people in the lift
     */
    public void clearPeopleInTheLift() {
        this.lift.clearAllPeopleFromLift();
    }

    /**
     * Clears every person in the building
     */
    public void clearAll() {
        this.clearPeopleAlreadyPicked();
        this.clearPeopleWaitingToBePicked();
        this.clearPeopleInTheLift();
    }
}
