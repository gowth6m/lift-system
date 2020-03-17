package liftsystem;

import liftsystem.UI.SimulationUI;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create a new simulation form
        SimulationUI n = new SimulationUI();
        
        // Set the number of floors the building has
        n.setNumberOfFloors(20); 
        
        // Show the simulation form 
        n.setVisible(true);
       
       // This is just a test
       
    }   
}
