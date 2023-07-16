import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.Instant;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
// import javax.swing.JMenu;
// import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import com.formdev.flatlaf.*;

public class GUI extends RoundRobin implements ActionListener {
    public boolean readyToSimulate;// if false, no running of simulation, to prevent running a simulation while
                                   // another one
                                   // is running

    private boolean turnOffGenerateButton = false;

    int numProcesses;
    int burstRangeMin;
    int burstRangeMax;
    int arrivalRangeMin;
    int arrivalRangeMax;

    ArrayList<Integer> checkArriveTimes;

    private JFrame window = new JFrame();

    // Menus
    // private JMenuBar menuBar = new JMenuBar();
    // private JMenu helpMenu = new JMenu("helpMenu");

    // Gantt Chart Internal Frame
    JInternalFrame ganttChart = new JInternalFrame("Summary Chart", true, false, false, false);
    // Splitting GUI
    private JPanel userSection = new JPanel();
    private JPanel processSection = new JPanel();
    private JSplitPane userSectionAndProcesses = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userSection, processSection);

    private JSplitPane upperSectionAndGanttChart = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userSectionAndProcesses,
            ganttChart);

    private JInternalFrame userInputInternalFrame = new JInternalFrame("Configurations");
    private JInternalFrame arrivalTimesInternalFrame = new JInternalFrame("Generated Arrival Times");

    private JInternalFrame criticalSection = new JInternalFrame("Critical Section");
    private JPanel processQueueAndComplete = new JPanel();

    private JSplitPane criticalSectionAndProcessesSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, criticalSection,
            processQueueAndComplete);

    private JInternalFrame quantumTimeInternalFrame = new JInternalFrame("Ready Queue Time Quantum");
    private JInternalFrame logsInternalFrame = new JInternalFrame("Logs");
    private JSplitPane quantumTimeAndLogSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, quantumTimeInternalFrame,
            logsInternalFrame);
    private JSplitPane logsAndProcesses = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, quantumTimeAndLogSplitPane,
            criticalSectionAndProcessesSplit);
    private JInternalFrame processCreatedJInternalFrame = new JInternalFrame("Process Created");
    private JInternalFrame processExitedInternalFrame = new JInternalFrame("Process Exited");

    private JPanel configurationPanel = new JPanel();

    private JPanel configurationSubPanel1 = new JPanel();
    private JPanel configurationSubPanel2 = new JPanel();

    private JPanel numberOfProcessesPanel = new JPanel();
    private JPanel rangeOfBurstTimesPanel = new JPanel();
    private JPanel rangeOfArrivalTimesPanel = new JPanel();

    private JButton generateButton = new JButton("Generate");
    private JButton simulateButton = new JButton("Simulate");

    private JPanel fieldPanel1 = new JPanel();
    private JPanel fieldPanel2 = new JPanel();
    private JPanel fieldPanel3 = new JPanel();

    private JLabel numberOfProcessesLabel = new JLabel("Number of Processes");
    private JLabel rangeOfBurstTimesLabel = new JLabel("Range of Burst Times");
    private JLabel rangeOfArrivalTimesLabel = new JLabel("Range of Arrival Times");

    private JTextField numberOfProcessesTextField = new JTextField();

    private JTextField rangeOfBurstTimesTextField1 = new JTextField();
    private JTextField rangeOfBurstTimesTextField2 = new JTextField();

    private JTextField rangeOfArrivalTimeTextField1 = new JTextField();
    private JTextField rangeOfArrivalTimeTextField2 = new JTextField();

    private JTextArea arrivalTimeTextArea = new JTextArea();
    private JTextArea logsTextArea = new JTextArea();
    private JTextArea processCreatedTextArea = new JTextArea();
    private JTextArea processExitedJTextArea = new JTextArea();
    private JTextArea quantumtimeResultJTextArea = new JTextArea();
    private JTextArea criticalSectionJTextArea = new JTextArea();

    private JTextArea ganttJTextArea = new JTextArea();
    private JScrollPane ganttJScrollPane = new JScrollPane(ganttJTextArea);

    private JScrollPane generatedArrivalTimeScrollPane = new JScrollPane(arrivalTimeTextArea);
    private JScrollPane logsScrollPane = new JScrollPane(logsTextArea);
    private JScrollPane processCreatedScrollPane = new JScrollPane(processCreatedTextArea);
    private JScrollPane processExitedScrollPane = new JScrollPane(processExitedJTextArea);
    private JScrollPane criticalSectionScrollPane = new JScrollPane(criticalSectionJTextArea);

    public static void main(String[] args) {
        FlatDarkLaf.setup(); // sets the look and feel
        new GUI();

    }

    public GUI() {
        // setMenus();
        setOtherComponents();
        window.add(upperSectionAndGanttChart, BorderLayout.CENTER);
        // setting basic fram settings
        window.setTitle("Round Robin Scheduler Simulator By David A. Arungbemi");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setSize(800, 800);
    }

    private void fillConfiguration() {
        configurationPanel.setLayout(new BorderLayout());

        numberOfProcessesPanel.setLayout(new GridLayout(1, 2, 5, 0));
        numberOfProcessesPanel.add(numberOfProcessesLabel);
        fieldPanel1.setLayout(new GridLayout(1, 1, 5, 0));
        fieldPanel1.add(numberOfProcessesTextField);
        numberOfProcessesPanel.add(fieldPanel1);

        rangeOfBurstTimesPanel.setLayout(new GridLayout(1, 2, 5, 0));
        rangeOfBurstTimesPanel.add(rangeOfBurstTimesLabel);
        fieldPanel2.setLayout(new GridLayout(1, 2, 5, 0));
        fieldPanel2.add(rangeOfBurstTimesTextField1);
        fieldPanel2.add(rangeOfBurstTimesTextField2);
        rangeOfBurstTimesPanel.add(fieldPanel2);

        rangeOfArrivalTimesPanel.setLayout(new GridLayout(1, 2, 5, 0));
        rangeOfArrivalTimesPanel.add(rangeOfArrivalTimesLabel);
        fieldPanel3.setLayout(new GridLayout(1, 2, 5, 0));
        fieldPanel3.add(rangeOfArrivalTimeTextField1);
        fieldPanel3.add(rangeOfArrivalTimeTextField2);
        rangeOfArrivalTimesPanel.add(fieldPanel3);

        configurationSubPanel1.setBorder(new EmptyBorder(0, 0, 10, 0));
        configurationSubPanel1.setLayout(new GridLayout(3, 1, 0, 10));
        configurationSubPanel1.add(numberOfProcessesPanel);
        configurationSubPanel1.add(rangeOfBurstTimesPanel);
        configurationSubPanel1.add(rangeOfArrivalTimesPanel);

        configurationPanel.add(configurationSubPanel1, BorderLayout.CENTER);

        configurationSubPanel2.setLayout(new GridLayout(1, 2, 5, 0));
        configurationSubPanel2.add(generateButton);
        generateButton.addActionListener(this);
        configurationSubPanel2.add(simulateButton);
        simulateButton.addActionListener(this);
        configurationPanel.add(configurationSubPanel2, BorderLayout.SOUTH);

        userInputInternalFrame.setLayout(new GridLayout(1, 1));
        configurationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userInputInternalFrame.add(configurationPanel);

    }

    private void setOtherComponents() {

        fillConfiguration();

        // make textarea colors
        logsTextArea.setForeground(new Color(97, 242, 116));
        quantumtimeResultJTextArea.setForeground(Color.YELLOW);
        criticalSectionJTextArea.setForeground(new Color(235, 82, 16));
        processExitedJTextArea.setForeground(Color.PINK);
        processCreatedTextArea.setForeground(Color.CYAN);
        ganttJTextArea.setForeground(new Color(201, 159, 245));
        // set size
        ganttJTextArea.setFont(ganttJTextArea.getFont().deriveFont(15f));

        // remove internalframe icons
        ganttChart.setFrameIcon(null);
        userInputInternalFrame.setFrameIcon(null);
        arrivalTimesInternalFrame.setFrameIcon(null);
        criticalSection.setFrameIcon(null);
        quantumTimeInternalFrame.setFrameIcon(null);
        logsInternalFrame.setFrameIcon(null);
        processCreatedJInternalFrame.setFrameIcon(null);
        processExitedInternalFrame.setFrameIcon(null);

        // set jtext areas to non editable
        arrivalTimeTextArea.setEditable(false);
        logsTextArea.setEditable(false);
        processCreatedTextArea.setEditable(false);
        processExitedJTextArea.setEditable(false);
        quantumtimeResultJTextArea.setEditable(false);
        criticalSectionJTextArea.setEditable(false);
        ganttJTextArea.setEditable(false);

        // fillup arrivalTimeBox
        arrivalTimesInternalFrame.setLayout(new GridLayout(1, 1));
        generatedArrivalTimeScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        arrivalTimesInternalFrame.add(generatedArrivalTimeScrollPane);

        // fillup logs
        logsInternalFrame.setLayout(new GridLayout(1, 1));
        logsScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        logsInternalFrame.add(logsScrollPane);

        // fillup critical section
        criticalSection.setLayout(new GridLayout(1, 1));
        criticalSectionScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        criticalSection.add(criticalSectionScrollPane);

        // fill up process created
        processCreatedJInternalFrame.setLayout(new GridLayout(1, 1));
        processCreatedScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        processCreatedJInternalFrame.add(processCreatedScrollPane);

        // fill up process exited
        processExitedInternalFrame.setLayout(new GridLayout(1, 1));
        processExitedScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        processExitedInternalFrame.add(processExitedScrollPane);

        // fill up quantum time box
        quantumTimeInternalFrame.setLayout(new GridLayout(1, 1));
        quantumtimeResultJTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        quantumTimeInternalFrame.add(quantumtimeResultJTextArea);

        ganttChart.setLayout(new GridLayout(1, 1));
        ganttJScrollPane.setBorder(new EmptyBorder(0, 30, 0, 30));
        ganttChart.add(ganttJScrollPane);
        ganttChart.setVisible(true);
        // upperSectionAndGanttChart Splitplane
        upperSectionAndGanttChart.setOneTouchExpandable(false);
        upperSectionAndGanttChart.setDividerLocation(600);
        upperSectionAndGanttChart.setVisible(true);

        // user section settings
        userSection.setLayout(new GridLayout(1, 2));
        userInputInternalFrame.setVisible(true);
        arrivalTimesInternalFrame.setVisible(true);
        userSection.add(userInputInternalFrame);
        userSection.add(arrivalTimesInternalFrame);

        // userSectionAndProcesses splitplane
        userSectionAndProcesses.setOneTouchExpandable(false);
        userSectionAndProcesses.setDividerLocation(200);
        userSectionAndProcesses.setVisible(true);

        // logs and ready queue
        // logsand processes splitplane
        logsAndProcesses.setOneTouchExpandable(false);
        logsAndProcesses.setDividerLocation(300);
        logsAndProcesses.setVisible(true);

        processSection.setLayout(new GridLayout(1, 1));
        processSection.add(logsAndProcesses);
        quantumTimeInternalFrame.setVisible(true);
        logsInternalFrame.setVisible(true);

        // logs and quantum splitplane
        quantumTimeAndLogSplitPane.setOneTouchExpandable(false);
        quantumTimeAndLogSplitPane.setDividerLocation(100);
        quantumTimeAndLogSplitPane.setVisible(true);

        // criticalsection and processes

        // criticalSectionAndProcessesSplit
        criticalSectionAndProcessesSplit.setOneTouchExpandable(false);
        criticalSectionAndProcessesSplit.setDividerLocation(150);
        criticalSectionAndProcessesSplit.setVisible(true);

        criticalSection.setVisible(true);
        processCreatedJInternalFrame.setVisible(true);
        processExitedInternalFrame.setVisible(true);
        processQueueAndComplete.setLayout(new GridLayout(1, 2));
        processQueueAndComplete.add(processCreatedJInternalFrame);
        processQueueAndComplete.add(processExitedInternalFrame);
        processQueueAndComplete.setVisible(true);

    }

    // private void setMenus() { // sets the menu tab
    // helpMenu.setVisible(true);
    // menuBar.add(helpMenu);
    // menuBar.setVisible(true);
    // window.add(menuBar, BorderLayout.NORTH);
    // }

    public void collectUserInput() {
        // Integer.parseInt("Activate Error");
        try {

            numProcesses = Integer.parseInt(numberOfProcessesTextField.getText().replaceAll(" ", ""));

            burstRangeMin = Integer.parseInt(rangeOfBurstTimesTextField1.getText().replaceAll(" ", ""));
            burstRangeMax = Integer.parseInt(rangeOfBurstTimesTextField2.getText().replaceAll(" ", ""));

            arrivalRangeMin = Integer.parseInt(rangeOfArrivalTimeTextField1.getText().replaceAll(" ", ""));
            arrivalRangeMax = Integer.parseInt(rangeOfArrivalTimeTextField2.getText().replaceAll(" ", ""));

            // testing to see if errors involving range occur
            if (burstRangeMax < burstRangeMin || arrivalRangeMax < arrivalRangeMin) {
                // trigger error
                Integer.parseInt("Activate Error");
            }

            if (burstRangeMin == 0 || burstRangeMax == 0) {
                // trigger error
                Integer.parseInt("Activate Error");
            }

            numberOfProcesses = numProcesses;

            minBurstTime = burstRangeMin;
            maxBurstTime = burstRangeMax;

            minArrivalTime = arrivalRangeMin;
            maxArrivalTime = arrivalRangeMax;

            // System.out.println("Hello First!");

            printArrivalResults();

        }

        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Invalid Input", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void printArrivalResults() {
        checkArriveTimes = new ArrayList<>();
        checkArriveTimes.clear();

        determineArrivalTimes();
        checkArriveTimes.addAll(arriveTimes);

        Collections.sort(checkArriveTimes);
        int processNameCounter = 0;
        String holdText = "";
        for (int aTime : checkArriveTimes) {
            processNameCounter += 1;
            holdText = "Process " + processNameCounter + " ---> Arrival Time: " + aTime
                    + "\n";
            arrivalTimeTextArea.append(holdText);
        }
        // System.out.println(holdText);
        // System.out.println(checkArriveTimes);
        // readyToSimulate = true;
    }

    public void createGanttChart() {
        ganttJTextArea.append("\n");

        ganttJTextArea.append("Process: ");
        for (int exitN : holdCriticalEntryProcessName) {
            ganttJTextArea.append("P" + exitN + "    ");
        }
        ganttJTextArea.append("\n");
        ganttJTextArea.append("\n");

        ganttJTextArea.append("Critical Entry Time: ");
        for (int exitT : holdCriticalEntryTime) {
            ganttJTextArea.append("" + exitT + "      ");
        }

        holdCriticalEntryProcessName.clear();
        holdCriticalEntryTime.clear();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton && turnOffGenerateButton == false) {
            System.out.println(turnOffGenerateButton);
            readyToSimulate = true;
            arrivalTimeTextArea.setText("");
            logsTextArea.setText("");
            quantumtimeResultJTextArea.setText("");
            criticalSectionJTextArea.setText("");
            processCreatedTextArea.setText("");
            processExitedJTextArea.setText("");
            ganttJTextArea.setText("");
            collectUserInput();

        }

        if (e.getSource() == simulateButton && readyToSimulate == true) {
            readyToSimulate = false;
            turnOffGenerateButton = true;
            System.out.println(arriveTimes);
            SwingWorker work = createWorker();
            work.execute();
        }
    }

    public void calculate() {
        averageTurnAroundTime = 0;
        averageWaitingTime = 0;
        for (int i : holdTurnAroundTime) {
            averageTurnAroundTime += i;
        }

        for (int i : holdOriginalWaitingTime) {
            averageWaitingTime += i;
        }

        ganttJTextArea.append("\nAverage Turn Around Time: " +
                averageTurnAroundTime / holdTurnAroundTime.size() + "\n");

        ganttJTextArea.append("\nAverage Waiting Time: " +
                averageWaitingTime / holdOriginalWaitingTime.size() + "\n");

        // averageTurnAroundTime = 0;
        // averageWaitingTime = 0;
        holdTurnAroundTime.clear();
        holdOriginalWaitingTime.clear();
        turnOffGenerateButton = false;
    }

    public SwingWorker<Boolean, String> createWorker() {
        return new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Start Progress
                setProgress(0);
                // wait(500);

                start = Instant.now(); // start counting time for processes
                criticalText = "";

                while (true) {
                    if (readyQueue.size() > 0) {
                        if (runState == true) {
                            criticalRun = Instant.now();
                            criticalSectionBack = readyQueue.peek();
                            criticalSectionBack.quantumNumber = quantumTime;
                            runState = false;

                            // check time of enter
                            checkEntryCriticalTime();
                            holdCriticalEntryProcessName.add(criticalSectionBack.processName); // check name too

                            criticalText = "Process: " + criticalSectionBack.processName + "\nBurst Time: "
                                    + criticalSectionBack.burstTime
                                    + "\nTime Quantum: " + criticalSectionBack.quantumNumber;
                            // criticalSectionJTextArea.setText(criticalText);

                            System.out.println("Process " + criticalSectionBack.processName + ": Running!");

                            logString = "\nProcess " + criticalSectionBack.processName
                                    + ": Running!\n" + "Entry Time " + entryTime + "\nTime Quantum: "
                                    + criticalSectionBack.quantumNumber + "\n";

                            publish(logString);

                            // logsTextArea.append(logString);
                        }

                        if (Duration.between(criticalRun, Instant.now())
                                .toSeconds() == criticalSectionBack.quantumNumber
                                && criticalSectionBack.burstTime > criticalSectionBack.quantumNumber) {

                            criticalSectionBack.burstTime -= criticalSectionBack.quantumNumber;
                            criticalSectionBack.arrivalTime = (int) Duration.between(start, Instant.now()).toSeconds();
                            readyQueue.remove(); // removes from head of queue
                            readyQueue.add(criticalSectionBack);// sends to back of queue

                            System.out.println("-------------------------------------\n");

                            System.out.println(
                                    "Process " + criticalSectionBack.processName
                                            + ": Not complete \nReturned to Queue");

                            System.out.println("Arrival Time: " + criticalSectionBack.arrivalTime);
                            System.out.println("Burst Time: " + criticalSectionBack.burstTime);
                            System.out.println("Ready Time Quantum: " + quantumTime);
                            System.out.println("-------------------------------------\n");

                            logString = "\n-------------------------------------\n " + "Process "
                                    + criticalSectionBack.processName + ": Not complete\nReturned to Queue\n"
                                    + "New arrival Time: " + criticalSectionBack.arrivalTime + "\nBurst Time: "
                                    + criticalSectionBack.burstTime
                                    + "\n-------------------------------------\n";

                            publish(logString);
                            runState = true;
                        }

                        else if (Duration.between(criticalRun, Instant.now())
                                .toSeconds() == criticalSectionBack.burstTime
                                && criticalSectionBack.burstTime <= criticalSectionBack.quantumNumber) {

                            // process complete
                            criticalSectionBack.exitTime = (int) Duration.between(start, Instant.now()).toSeconds();
                            criticalSectionBack.turnAroundTime = criticalSectionBack.exitTime
                                    - criticalSectionBack.arrivalTime;
                            criticalSectionBack.waitingTime = criticalSectionBack.turnAroundTime
                                    - criticalSectionBack.originalBurstTime;

                            readyQueue.remove(); // removes from head of queue
                            holdOriginalWaitingTime
                                    .add((criticalSectionBack.exitTime - criticalSectionBack.originalArrivalTime)
                                            - criticalSectionBack.originalBurstTime);
                            holdTurnAroundTime
                                    .add(criticalSectionBack.exitTime - criticalSectionBack.originalArrivalTime);

                            System.out.println("*************************************\n");

                            System.out.println("Process " + criticalSectionBack.processName + ": Completed!");
                            System.out.println("Exit Time: " + criticalSectionBack.exitTime);

                            System.out.println("*************************************\n");

                            logString = "\n*************************************\n" + "Process "
                                    + criticalSectionBack.processName + ": Completed!\n" + "Exit Time: "
                                    + criticalSectionBack.exitTime + "\n*************************************\n";

                            processExitString = logString;
                            publish(logString);
                            runState = true;
                            endSimulationCounter++;
                        }
                    }

                    checkForQueue();
                    if (creationOccured == true) {
                        // quantumtimeResultJTextArea.setText("\n "
                        // + quantumTime);
                        logString = "\n+++++++++++++++++++++\n" + "Process " + showProcessCreation.processName
                                + ": Created!\n" + "Arrival Time: " + showProcessCreation.arrivalTime + "\nBurst Time: "
                                + showProcessCreation.burstTime + "\n+++++++++++++++++++++\n";

                        processCreateString = logString;
                        publish(logString);
                        // processCreatedTextArea.setText(logString);
                        // logsTextArea.append(logString);
                        creationOccured = false;
                    }

                    if (endSimulationCounter == arriveTimes.size()) {
                        endSimulationCounter = 0;
                        ProcessCreation.nameUpdate = 0;
                        Thread.sleep(1000);
                        // createGanttChart();
                        calculate();
                        break;
                    }

                }

                // wait(250);
                // Finished
                return true;

            }

            @Override
            protected void process(List<String> chunks) {
                // Get Info
                for (String result : chunks) {
                    // if (isCancelled())
                    // return;
                    logsTextArea.append(result);
                    criticalSectionJTextArea.setText("\n" + criticalText);
                    processCreatedTextArea.setText("\n" + processCreateString);
                    processExitedJTextArea.setText("\n\n" + processExitString);
                    quantumtimeResultJTextArea.setText("\n                               " + quantumTime);

                    System.out.println("Publishing was Successful: " + result);
                }
            }

            @Override
            protected void done() {
                boolean bStatus = false;
                try {
                    bStatus = get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("Finished with status " + bStatus);
            }

        };
    } // End of Method: createWorker()

}
