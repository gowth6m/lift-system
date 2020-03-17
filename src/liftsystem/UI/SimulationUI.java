/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftsystem.UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import liftsystem.Building;
import liftsystem.MechanicalLift;
import liftsystem.Person;
import liftsystem.SimulationTable;
import liftsystem.SimulationTable.Table;

public class SimulationUI extends javax.swing.JFrame {
    private int numberOfFloors;
    private Timer timer;
    private boolean startedSimulation;
    private final int unitTime; 
    private SimulationTable simulationTable;
    private Building building;
    private final int numberOfPeopleInBuilding;
    private ArrayList<Table> result;
    
    public void setNumberOfFloors(int floors){
        floors = Math.abs(floors);
        this.numberOfFloors = floors;
        
        try {
            // Refresh screen
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(SimulationUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @return int the number of floors
     */
    public int getNumberOfFloors(){
        return this.numberOfFloors;
    }
    
    // This functiin redraws the floors
    public void refresh() throws Exception {
        // Clear previous floors
        clearFloors();
        
        // Clear the logs
        this.txtSimulationLog.setText("");
        
        // Create new random buildings
        for(int i = 0; i < this.numberOfFloors; ++i){
           JPanelBuildingFloor floor = new JPanelBuildingFloor();
           
           // Set the floor properties here
           floor.setVisible(true);
           floor.setBounds(7, ((i + 1) * 25), 372, 21);
           floor.setFloor((this.numberOfFloors - i));
           floor.lblLift.setText("Floor: " + (this.numberOfFloors - i));
               
           // Add floor to the panel
           this.panelBuilding.add(floor);
           this.panelBuilding.revalidate();
           this.panelBuilding.repaint();
        }
    }
    
    /**
     * Clears the floors and create new ones
     */
    public void clearFloors(){
         // Clears the floors
         for(int i = 0; i < this.panelBuilding.getComponentCount(); ++i){
             try{
                 this.panelBuilding.getComponent(i).remove(null);
             }
             catch(Exception ex){
                 Logger.getLogger(SimulationUI.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
    }
    
    /**
     * Creates new form SimulationUI
     */
    public SimulationUI() {
        this.numberOfFloors = 20;
        this.startedSimulation = false;
        this.timer = null;
        this.unitTime = 5000;
        this.numberOfPeopleInBuilding = 20; // Simulate for 10 people
        
        initComponents();
        
        timer = new Timer(unitTime / this.sliderSpeed.getValue(), (ActionEvent e) -> {
            // Handle updating the UI here
            updateUI();
        });
    }
    
    // This function updates the UI
    static int timerCount = 0;
    public void updateUI(){
        if(timerCount >= this.result.size()){
            stopSimulation();
            this.startedSimulation = false;
            this.btnStartOrStopSimulation.setText("Start simulation");
            this.appendToLog("Finished the simulation.... picked " + this.numberOfPeopleInBuilding + " people");
            return;
        }
       
        // Get the details on this time
        int currentFloor = result.get(timerCount).currentFloor;
        int prevFloor = 1;
        Table currentStatistics = result.get(timerCount);
        Table prevStatistics = null;
        
        if(timerCount > 0 || prevStatistics != null){
           prevStatistics =  result.get(timerCount - 1);
           prevFloor = prevStatistics.currentFloor;
        }
            
        JPanelBuildingFloor floor = (JPanelBuildingFloor) this.panelBuilding.getComponent(this.building.getMaxFloorsInBuilding() - currentFloor);
               
        floor.lblLift.setBackground(new Color(30, 243, 76));
        floor.lblLift.setForeground(Color.WHITE);
        
        if(prevFloor > 0){
            floor = (JPanelBuildingFloor) this.panelBuilding.getComponent(this.building.getMaxFloorsInBuilding() - prevFloor);
            
            floor.lblLift.setBackground(new Color(216,216,216));
            floor.lblLift.setForeground(Color.WHITE);
        }
        
        this.appendToLog("Currently on floor: " + currentFloor);
        this.lblCumulativeWaitTime.setText("Cumulative wait time: " + currentStatistics.currentWaitTime);
        this.lblCumulativeCost.setText("Cumulative cost: " + currentStatistics.currentWaitTime);
        this.lblCurrentFloor.setText("Current Floor: " + currentStatistics.currentFloor);
        this.lblNextFloor.setText("Next Floor: " + currentStatistics.nextFloor);
        
        // Update for lift directions
        if(currentStatistics.currentLiftDirection){
            // Lift is going up
            this.lblLiftDirections_UP.setBackground(new Color(30, 243, 76));
            this.lblLiftDirections_UP.setForeground(Color.WHITE);
            
            this.lblLiftDirections_DOWN.setBackground(new Color(216,216,216));
            this.lblLiftDirections_DOWN.setForeground(Color.BLACK);
        }
        else{
            // Lift is going down
            this.lblLiftDirections_UP.setBackground(new Color(216,216,216));
            this.lblLiftDirections_UP.setForeground(Color.BLACK);
            
            this.lblLiftDirections_DOWN.setBackground(new Color(30, 243, 76));
            this.lblLiftDirections_DOWN.setForeground(Color.WHITE);
        }
       
        this.lblPeopleInLift.setText("People in lift: " + currentStatistics.numberOfPeopleInTheLift);
        this.lblAlreadyPicked.setText("Already Picked: " + currentStatistics.peopleAlreadyServed);
        this.lblRemainingPeople.setText("waitingToBePicked: " + currentStatistics.waitingToBePicked);
        
        // Upate timer count
        ++timerCount;
        
        // Set the current time
        this.lblSimulationTime.setText("Current time: " + timerCount);
        
        // Update progress bar
        this.progressBarStatus.setValue((int) (((double)timerCount / this.result.size()) * 100));
        
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBuilding = new javax.swing.JPanel();
        panelControls = new javax.swing.JPanel();
        btnStartOrStopSimulation = new javax.swing.JButton();
        progressBarStatus = new javax.swing.JProgressBar();
        sliderSpeed = new javax.swing.JSlider();
        lblSimulationSpeed = new javax.swing.JLabel();
        panelStatistics = new javax.swing.JPanel();
        lblCumulativeWaitTime = new javax.swing.JLabel();
        lblCumulativeCost = new javax.swing.JLabel();
        lblSimulationTime = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblLiftDirections_UP = new javax.swing.JLabel();
        lblLiftDirections_DOWN = new javax.swing.JLabel();
        lblCurrentFloor = new javax.swing.JLabel();
        lblNextFloor = new javax.swing.JLabel();
        lblPeopleInLift = new javax.swing.JLabel();
        lblAlreadyPicked = new javax.swing.JLabel();
        lblRemainingPeople = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSimulationLog = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulation UI");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(0, 0, 0));
        setName("MainProgram"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(800, 720));
        setType(java.awt.Window.Type.POPUP);

        panelBuilding.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 4, true), "Building"));

        javax.swing.GroupLayout panelBuildingLayout = new javax.swing.GroupLayout(panelBuilding);
        panelBuilding.setLayout(panelBuildingLayout);
        panelBuildingLayout.setHorizontalGroup(
            panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );
        panelBuildingLayout.setVerticalGroup(
            panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        panelControls.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls"));

        btnStartOrStopSimulation.setText("Start simulation");
        btnStartOrStopSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartOrStopSimulationActionPerformed(evt);
            }
        });

        progressBarStatus.setBackground(new java.awt.Color(153, 153, 153));
        progressBarStatus.setForeground(new java.awt.Color(30, 243, 76));
        progressBarStatus.setMaximumSize(new java.awt.Dimension(10, 19));
        progressBarStatus.setString("10%");

        sliderSpeed.setMaximum(10);
        sliderSpeed.setMinimum(1);
        sliderSpeed.setToolTipText("This is the simulation speed, minimum of 1 step per second,");
        sliderSpeed.setValue(10);
        sliderSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderSpeedStateChanged(evt);
            }
        });

        lblSimulationSpeed.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblSimulationSpeed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSimulationSpeed.setText("Simulation speed: 10");

        javax.swing.GroupLayout panelControlsLayout = new javax.swing.GroupLayout(panelControls);
        panelControls.setLayout(panelControlsLayout);
        panelControlsLayout.setHorizontalGroup(
            panelControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlsLayout.createSequentialGroup()
                .addGroup(panelControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelControlsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(progressBarStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelControlsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sliderSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(11, 11, 11)
                        .addComponent(lblSimulationSpeed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStartOrStopSimulation)))
                .addContainerGap())
        );
        panelControlsLayout.setVerticalGroup(
            panelControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlsLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(panelControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStartOrStopSimulation)
                    .addComponent(sliderSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSimulationSpeed))
                .addGap(18, 18, 18)
                .addComponent(progressBarStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelStatistics.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Stats"));

        lblCumulativeWaitTime.setText("Cumulative wait time: 0");

        lblCumulativeCost.setText("Cumulative cost: 0");

        lblSimulationTime.setText("Current time: 0");

        jLabel4.setText("Lift direction: ");

        lblLiftDirections_UP.setBackground(new java.awt.Color(30, 243, 76));
        lblLiftDirections_UP.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblLiftDirections_UP.setForeground(new java.awt.Color(255, 255, 255));
        lblLiftDirections_UP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLiftDirections_UP.setText("UP");
        lblLiftDirections_UP.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 3));
        lblLiftDirections_UP.setOpaque(true);

        lblLiftDirections_DOWN.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblLiftDirections_DOWN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLiftDirections_DOWN.setText("DOWN");
        lblLiftDirections_DOWN.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 3));
        lblLiftDirections_DOWN.setOpaque(true);

        lblCurrentFloor.setText("Current Floor: 0");

        lblNextFloor.setText("Next Floor: 0");

        lblPeopleInLift.setText("People in lift: 0");

        lblAlreadyPicked.setText("Already Picked: 0");

        lblRemainingPeople.setText("Already Picked: 0");

        javax.swing.GroupLayout panelStatisticsLayout = new javax.swing.GroupLayout(panelStatistics);
        panelStatistics.setLayout(panelStatisticsLayout);
        panelStatisticsLayout.setHorizontalGroup(
            panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStatisticsLayout.createSequentialGroup()
                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelStatisticsLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCumulativeWaitTime)
                            .addGroup(panelStatisticsLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSimulationTime)
                                    .addComponent(lblCurrentFloor))))
                        .addGap(50, 50, 50)
                        .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCumulativeCost)
                            .addGroup(panelStatisticsLayout.createSequentialGroup()
                                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblLiftDirections_UP)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblLiftDirections_DOWN))))
                    .addGroup(panelStatisticsLayout.createSequentialGroup()
                        .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelStatisticsLayout.createSequentialGroup()
                                .addGap(150, 150, 150)
                                .addComponent(lblNextFloor)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStatisticsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblRemainingPeople)
                                .addGap(62, 62, 62)))
                        .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAlreadyPicked)
                            .addComponent(lblPeopleInLift))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelStatisticsLayout.setVerticalGroup(
            panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStatisticsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCumulativeWaitTime)
                    .addComponent(lblCumulativeCost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSimulationTime)
                    .addComponent(jLabel4))
                .addGap(3, 3, 3)
                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLiftDirections_UP)
                    .addComponent(lblLiftDirections_DOWN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCurrentFloor)
                    .addComponent(lblNextFloor)
                    .addComponent(lblPeopleInLift))
                .addGap(12, 12, 12)
                .addGroup(panelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAlreadyPicked)
                    .addComponent(lblRemainingPeople))
                .addGap(34, 34, 34))
        );

        txtSimulationLog.setEditable(false);
        txtSimulationLog.setText("This is the log");
        jScrollPane2.setViewportView(txtSimulationLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBuilding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelControls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelStatistics, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2)))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelControls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelStatistics, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                    .addComponent(panelBuilding, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sliderSpeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderSpeedStateChanged

        this.lblSimulationSpeed.setText("Simulation speed: " + String.valueOf(this.sliderSpeed.getValue()));
    }//GEN-LAST:event_sliderSpeedStateChanged
    
    private void startSimulation(){
        this.startedSimulation = true;
        this.progressBarStatus.setValue(0);
        this.btnStartOrStopSimulation.setText("Stop simulation");
        this.appendToLog("Running simulation");
        this.txtSimulationLog.setText("");
        
        // Create a new worker thread
        SwingWorker<Integer, Void> worker;
                    
        if(this.building == null || this.simulationTable == null){
            // Instantiate a building
            this.building = new Building(this.numberOfFloors);
            
            // Generate the simulation table, run this in a separate thread            
            // Here you can separate the different Lift algorithms, but for now since
            // we are only using the mechanical lift algorithm, that is what we will use
            this.simulationTable = new SimulationTable(
                    this.building, 
                    new MechanicalLift(), 
                    this.numberOfPeopleInBuilding
            );
            
//            // Fill in the number of people on each floor
//            HashMap<Integer, Integer> hash = new HashMap<>();
//            
//            for(int i = 0; i < this.building.getNumberOfPeopleWaitingToBePicked(); ++i){
//                Person person = this.building.getPeopleWaitingToBePicked().get(i);
//                
//                if(hash.containsKey(person.getCurrentFloor())){
//                    hash.put(person.getCurrentFloor(), hash.get(person.getCurrentFloor()) + 1);
//                }
//                else{
//                    hash.put(person.getCurrentFloor(), 1);
//                }
//            }
//            
//            // Loop to add to building
//            // Clears the floors
//            for(int i = this.panelBuilding.getComponentCount(); i > 0; --i){
//                try{
//                    JPanelBuildingFloor floor = (JPanelBuildingFloor) this.panelBuilding.getComponent(i - 1);
//
//                    if(hash.containsKey(i)){
//                         floor.lblNumberOfPeople.setText("No. Of People: " + hash.get(i));
//                    }
//                    else{
//                        floor.lblNumberOfPeople.setText("No. Of People: " + 0);
//                    }
//                    
//                    floor.revalidate();
//                }
//                catch(Exception ex){
//                    Logger.getLogger(SimulationUI.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
         
            
            // Run the simulation
            // Reset the floors
            try {
                this.refresh();
            } catch (Exception ex) {
                Logger.getLogger(SimulationUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // The simulation starts here
            btnStartOrStopSimulation.setEnabled(false);
            writeToLog("Started simulation");
            appendToLog("Generating simulation table for " + numberOfPeopleInBuilding + " people");
                    
           // Run the simulation in a separate thread
            worker = new SwingWorker<Integer, Void>() {
                @Override
                public Integer doInBackground() {
                    result = simulationTable.run();
                    return 1;
                }
                
                @Override
                public void done() {
                    btnStartOrStopSimulation.setEnabled(true);
                    appendToLog("Finished generating table");
                    appendToLog("Lift has capacity of " + building.getLift().getCapacity());
                    
                    timer.setDelay((int) unitTime / sliderSpeed.getValue());
                    timer.start();
                }
            };

            // Call the SwingWorker from within the Swing thread
            worker.execute();
        }
        else{
            // Run the simulation in a separate thread
            worker = new SwingWorker<Integer, Void>() {
                @Override
                public Integer doInBackground() {            
                    // Just restart the simulation
                    result = simulationTable.restartSimulation().run();
                    return 1;
                }
                
                @Override
                public void done() {
                    btnStartOrStopSimulation.setEnabled(true);
                    appendToLog("Finished generating table");
                    appendToLog("Lift has capacity of " + building.getLift().getCapacity());
                    
                    timer.setDelay((int) unitTime / sliderSpeed.getValue());
                    timer.start();
                }
            };
            
            // Call the SwingWorker from within the Swing thread
            worker.execute();
        }

        //[30,243,76] - Selected
        //[214,217,223] - Not selected
    }
    
    private void appendToLog(String text){
        txtSimulationLog.setText(txtSimulationLog.getText() + "[*] " + text  + "\n" );
    }
    
    private void writeToLog(String text){
         txtSimulationLog.setText("[*] " + text + "\n");
    }
    
    private void stopSimulation(){
        timer.stop();
        this.startedSimulation = false;
        this.btnStartOrStopSimulation.setText("Start simulation");
        this.appendToLog("Simulation stopped");
        timerCount = 0;
    }
    
    private void btnStartOrStopSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartOrStopSimulationActionPerformed
        // TODO add your handling code here:
        if(startedSimulation){
            stopSimulation();
        }
        else{
           startSimulation();
        }
    }//GEN-LAST:event_btnStartOrStopSimulationActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStartOrStopSimulation;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAlreadyPicked;
    private javax.swing.JLabel lblCumulativeCost;
    private javax.swing.JLabel lblCumulativeWaitTime;
    private javax.swing.JLabel lblCurrentFloor;
    private javax.swing.JLabel lblLiftDirections_DOWN;
    private javax.swing.JLabel lblLiftDirections_UP;
    private javax.swing.JLabel lblNextFloor;
    private javax.swing.JLabel lblPeopleInLift;
    private javax.swing.JLabel lblRemainingPeople;
    private javax.swing.JLabel lblSimulationSpeed;
    private javax.swing.JLabel lblSimulationTime;
    private javax.swing.JPanel panelBuilding;
    private javax.swing.JPanel panelControls;
    private javax.swing.JPanel panelStatistics;
    private javax.swing.JProgressBar progressBarStatus;
    private javax.swing.JSlider sliderSpeed;
    private javax.swing.JTextPane txtSimulationLog;
    // End of variables declaration//GEN-END:variables
}
