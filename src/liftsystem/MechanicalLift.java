/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem;

import java.util.ArrayList;

public final class MechanicalLift implements LiftAlgorithm {
    private int counter;
    private int capacity; 
    private int currentFloor; 
    private int totalWaitTime;
    private int totalNumberOfPeopleOnThisFloor;
    private int nextFloor;
    private int totalCost;
    private boolean liftDirection;
    private final ArrayList<Person> inTheLift;
    private Building building; // Instance of the building class

    public MechanicalLift() {
        this.capacity = 10;
        this.inTheLift = new ArrayList<>();
        this.currentFloor = 1;
        this.totalWaitTime = 0;
        this.totalNumberOfPeopleOnThisFloor = 0;
 
        this.totalCost = 0;
        this.liftDirection = true;
        this.building = new Building();
        this.counter = 0;
        
        setNextFloor(this.currentFloor);
    }

     @Override
    public MechanicalLift run() {
        // Simulation logic goes here        
        // Let the people who have reached their target floors alight
        checkReachedDestination();

        // Start moving the people one step either up or down             
        checkWaiting();
        
        this.counter++;
        
        return this;
     }
    
    private void checkReachedDestination(){
        // Check if there is a person waiting to get off the lift on the current floor
        for(int j = 0; j < building.getNumberOfPeopleInTheLift(); ++j){
            Person personInLift = building.getPeopleInLift().get(j);

            // Check if the person is getting out at this point
            if(personInLift.getTargetFloor() == this.getCurrentFloor()){
                // Get off the lift
               // Add the person to the completed list
               building.addPersonToAlreadyPicked(personInLift);
               
               // Calculate the cost for the person
               this.totalCost += (
                       personInLift.getWaitTime() + 
                       Math.abs(personInLift.getTargetFloor() - 
                               personInLift.getCurrentFloor()
                       )
                    );
               
               // Calculate the wait time
               this.totalWaitTime += personInLift.getWaitTime();

               // Remove him/her from the lift
               this.removePerson(personInLift);
            }
        }
    }
    
    
    private void checkWaiting(){
        int peopleOnThisFloor = 0;
        
        for(int j = 0; j < this.building.getNumberOfPeopleWaitingToBePicked(); ++j){
            // Search for all people that are on the current floor
            Person personWaiting = this.building.getPeopleWaitingToBePicked().get(j);

            // If the persom is waiting to be picked up
            if(personWaiting.getCurrentFloor() == this.getCurrentFloor()){
                // Increment the total number of people on this floor
                peopleOnThisFloor++;
                
                // Check if the elevator is full
                if(!this.isFull()){
                    // The lift is not full add the person
                    this.addPerson(personWaiting);
                    
                    // Remove 1 to the number of people on this floor since she/he
                    // has boarded the lift
                    peopleOnThisFloor--;

                    // Remove him from waiting list
                    this.building.removePersonWaitingToBePicked(personWaiting);
                }
                else{
                    // Increment the waiting time for this person
                    personWaiting.setWaitTime(personWaiting.getWaitTime() + 1);
                }
            }
            else{
                // Increment the waiting time for this person
                personWaiting.setWaitTime(personWaiting.getWaitTime() + 1);
            }
        }
        
        // Set the new number of people on this floor
        this.totalNumberOfPeopleOnThisFloor = peopleOnThisFloor;
    }
    
    @Override
    public int getTotalCost() {
        return this.totalCost;
    }

    @Override
    public int getTotalWaitTime() {
      return this.totalWaitTime;
    }
    
    @Override
    public int getNumberOfPeopleOnThisFloor() {
        return this.totalNumberOfPeopleOnThisFloor;
    }
    
    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        if(capacity > 0){
            this.capacity = capacity;
        }
    }

    @Override
    public int getCurrentFloor() {
        return this.currentFloor;
    }

    @Override
    public void setCurrentFloor(int currentFloor) {
        if(currentFloor >= this.building.getMinFloorsInBuilding() && currentFloor <= this.building.getMaxFloorsInBuilding()){
            this.currentFloor = currentFloor;
        }
    }

    @Override
    public ArrayList<Person> getPeopleInLift() {
        return this.inTheLift;
    }

    @Override
    public void addPerson(Person person) {
        if(person != null && !this.isFull()){
           this.inTheLift.add(person);
        }
    }

    @Override
    public void removePerson(Person person) {
        if(person != null){
            this.inTheLift.remove(person);
        }
    }

    @Override
    public void clearAllPeopleFromLift() {
        this.inTheLift.clear();
    }
    
    @Override
    public void reset(){
        this.setCurrentFloor(this.building.getMinFloorsInBuilding());
        this.counter = 0;
        this.totalCost = 0;
        this.totalWaitTime = 0;
        this.clearAllPeopleFromLift();
        this.liftDirection = true;
    }

    @Override
    public int numberOfPeopleInLift() {
        return this.inTheLift.size();
    }

    @Override
    public boolean getLiftDirection() {
        return this.liftDirection;
    }

    @Override
    public void goUp() {
        if(this.isAtTheTop()){
            goDown();
            return;
        }
        
        this.liftDirection = true;
        
        this.currentFloor++;
        
        this.setNextFloor(this.currentFloor);
    }

    @Override
    public void goDown() {
        if(this.isAtTheBottom()){
            goUp();
            return;
        }
        
        this.liftDirection = false;
        
        this.currentFloor--;
        
        this.setNextFloor(this.currentFloor);
    }

    @Override
    public boolean isFull() {
        return this.inTheLift.size() == this.capacity;
    }

    @Override
    public boolean isEmpty() {
        return this.inTheLift.isEmpty();
    }

    @Override
    public boolean isAtTheBottom() {
        return this.currentFloor == this.building.getMinFloorsInBuilding();
    }

    @Override
    public boolean isAtTheTop() {
        return this.currentFloor == this.building.getMaxFloorsInBuilding();
    }

    @Override
    public Building getBuilding() {
        return this.building;
    }

    @Override
    public void setBuilding(Building building) {
        if(building != null){
            this.building = building;
        }
    }

    @Override
    public int getCounter() {
        return this.counter;
    }

    @Override
    public boolean finished() {
        return (this.building.getNumberOfPeopleWaitingToBePicked() == 0 && this.currentNumberOfPeopleInLift() == 0);
    }

    @Override
    public int currentNumberOfPeopleInLift() {
        return this.inTheLift.size();
    }

    @Override
    public int getNextFloor() {
        return this.nextFloor;
    }

    @Override
    public void setNextFloor(int theCurrentFloor) {
        // Check the direction the lift is going
        if(this.liftDirection){
            // If it is up then the next floor is up by 1
            ++theCurrentFloor;
        }
        else{
            // If it is up then the next floor is down by 1
            --theCurrentFloor;
        }
        
        // Check the previous - next floor
        if(this.nextFloor > 0){
            if(this.nextFloor >= this.building.getMinFloorsInBuilding() && this.nextFloor <= this.building.getMaxFloorsInBuilding()){
                this.currentFloor = this.nextFloor;
            }
        }
        
        if(theCurrentFloor >= this.building.getMinFloorsInBuilding() && theCurrentFloor <= this.building.getMaxFloorsInBuilding()){
            // Set the new next floor
            this.nextFloor = theCurrentFloor;
        }
        
        if(this.currentFloor == this.nextFloor){
            // Check the direction it was going
            if(this.liftDirection){
                this.liftDirection = false;
            }
            else{
                this.liftDirection = true;
            }
        }
    }
}
