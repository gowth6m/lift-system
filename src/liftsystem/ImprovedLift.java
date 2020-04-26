package liftsystem;

import java.util.ArrayList;

public final class ImprovedLift extends LiftAlgorithm {

    public ImprovedLift() {
        // Call the super class constructor, must always be called to initialize the class
        super();
    }

    @Override
    public ImprovedLift run() {
        // Simulation logic goes here
        // Let the people who have reached their target floors alight
        checkReachedDestination();

        // This function checks if anyone is waiting to enter the lift on the current floor
        checkWaiting();

        for (Person personInLift : building.getPeopleInLift()) {
            // This is to change direction if there are no more targets to be picked up or deliver
            if (this.building.getPeopleWaitingToBePicked().isEmpty() && personInLift.getTargetFloor() < this.getCurrentFloor()) {
                this.liftDirection = false;
            }
            if (this.building.getPeopleWaitingToBePicked().isEmpty() && personInLift.getTargetFloor() > this.getCurrentFloor()) {
                this.liftDirection = true;
            }
        }

        // Update the counter
        this.counter++;

        // Return this class at its current state, so that it can be logged by the
        // SimulationTable class
        return this;
    }

    private void checkReachedDestination() {
        // Temporarily stores a list of persons to be removed from the lift list, this is to prevent errors such as null
        // exception and to ensure the programs runs correctly
        ArrayList<Person> toBeRemoved = new ArrayList<>();

        // Check if there is a person waiting to get off the lift on the current floor
        for (Person personInLift : building.getPeopleInLift()) {

            // Check if the person is getting out at this point
            if (personInLift.getTargetFloor() == this.getCurrentFloor()) {
                // Get off the lift
                // Add the person to the completed list
                building.addPersonToAlreadyPicked(personInLift);

                // Calculate the wait time
                this.totalWaitTime += personInLift.getWaitTime();

                // Calculate the cost for delivering this person to his/her floor
                this.totalCost += (personInLift.getWaitTime() + (Math.abs(personInLift.getInitialFloor() - personInLift.getTargetFloor()))) + 1;

                // Remove him/her from the lift
                toBeRemoved.add(personInLift);
            }
        }

        // Remove the people in the temporary list
        for (Person p : toBeRemoved) {
            this.removePerson(p);
        }
    }

    private void checkWaiting() {
        int peopleOnThisFloor = 0;

        // Temporarily stores a list of persons to be removed from the wait list, this is to prevent errors such as null
        // exception and to ensure the programs runs correctly
        ArrayList<Person> toBeRemoved = new ArrayList<>();

        for (Person personWaiting : this.building.getPeopleWaitingToBePicked()) {
            // Search for all people that are on the current floor
            // Person personWaiting = this.building.getPeopleWaitingToBePicked().get(j);

            // If the person is waiting to be picked up
            if (personWaiting.getInitialFloor() == this.getCurrentFloor()) {
                // Check to see if the persons target floor is equal to current floor
                if (personWaiting.getTargetFloor() == personWaiting.getInitialFloor()) {
                    // Remove him from waiting list
                    toBeRemoved.add(personWaiting);

                    // Update the persons on this floor list
                    ++peopleOnThisFloor;
                } else {
                    // Check if the elevator is full
                    if (!this.isFull()) {
                        // The lift is not full add the person
                        this.addPerson(personWaiting);

                        // Remove him from waiting list
                        toBeRemoved.add(personWaiting);
                    } else {
                        // Increment the waiting time for this person, and the cost for the person
                        personWaiting.setWaitTime(personWaiting.getWaitTime() + 1);
                    }
                }
            } else {
                // Increment the waiting time for this person
                personWaiting.setWaitTime(personWaiting.getWaitTime() + 1);
            }
        }

        // Remove the people in the temporary list
        for (Person p : toBeRemoved) {
            this.building.removePersonWaitingToBePicked(p);

            // Add 1 to the number of people who got picked on this floor
            ++peopleOnThisFloor;
        }

        // Set the new number of people on this floor
        this.totalNumberOfPeopleWhoGotInToTheLiftOnThisFloor = peopleOnThisFloor;
    }
}
