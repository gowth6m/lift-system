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
            // Set the number of floors the building has
            n.setNumberOfFloors(20);

            // Show the simulation form 
            n.setVisible(true);
        } catch (Exception e) {
            Logger.getLogger(SimulationUI.class.getName()).log(Level.SEVERE, null, e);

            n.writeToLog("Ab error occured: " + e.getMessage(), "error");
        }

    }
}
