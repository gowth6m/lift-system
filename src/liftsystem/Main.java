package liftsystem;

import liftsystem.UI.SimulationUI;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create a new simulation form
        SimulationUI n = new SimulationUI();

        try {
            ImprovedLift iLift = new ImprovedLift();
            MechanicalLift mLift = new MechanicalLift();
            // Set the number of floors the building
            n.setNumberOfFloors(20);
            // Set the number of people in the building
            n.setNumberOfPeopleInBuilding(10);
            // Set the between iLift for Improved Lift or mLift for Mechanical Lift.
            n.setLiftAlgorithm(mLift);

            // Show the simulation form 
            n.setVisible(true);
        } catch (Exception e) {
            Logger.getLogger(SimulationUI.class.getName()).log(Level.SEVERE, null, e);

            n.writeToLog("An error occurred: " + e.getMessage(), "error");
        }

    }
}
