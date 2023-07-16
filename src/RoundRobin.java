import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
// import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class RoundRobin {
    public Instant start;
    public Instant criticalRun;

    public ArrayList<Integer> arriveTimes = new ArrayList<>();
    public ArrayList<Integer> arrivesTimesCopy = new ArrayList<>();

    public ArrayList<Integer> holdOriginalArrivalTime = new ArrayList<>();
    public ArrayList<Integer> holdOriginalWaitingTime = new ArrayList<>();
    public ArrayList<Integer> holdTurnAroundTime = new ArrayList<>();
    public ArrayList<Integer> holdOExitTimes = new ArrayList<>();

    public ArrayList<Integer> holdCriticalEntryTime = new ArrayList<>();
    public ArrayList<Integer> holdCriticalEntryProcessName = new ArrayList<>();

    float averageWaitingTime;
    float averageTurnAroundTime;
    // ArrayList<Integer> holdBurst = new ArrayList<>();

    public Queue<ProcessCreation> readyQueue = new LinkedList<>(); // Ready Queue for processes

    public String criticalText;

    ProcessCreation criticalSectionBack;

    public int numberOfProcesses;
    public int quantumTime;
    public int endSimulationCounter = 0;
    public boolean runState = true;

    public int minArrivalTime, maxArrivalTime;
    public int maxBurstTime, minBurstTime;

    public ProcessCreation showProcessCreation;
    public boolean creationOccured = false;

    public int entryTime;

    public String logString = "";

    public String processCreateString = "";
    public String processExitString = "";

    // variables for generate quantum time method
    int sumOfBurstTimes;
    // public int medianBurst;

    // public static void main(String[] args) {
    // RoundRobin instance = new RoundRobin();
    // }

    public void createProcess(int aTime) {
        showProcessCreation = new ProcessCreation(aTime, minBurstTime, maxBurstTime);
        readyQueue.add(showProcessCreation);

        System.out.println("+++++++++++++++++++++++++++++++++++++\n");
        System.out.println("Process " + showProcessCreation.processName + ": created");
        System.out.println("Arrival Time: " + showProcessCreation.arrivalTime);
        System.out.println("Burst Time: " + showProcessCreation.burstTime);
        System.out.println("+++++++++++++++++++++++++++++++++++++\n");
        creationOccured = true;

    }

    // public

    public void checkForQueue() {
        // System.out.println(arriveTimes);
        // System.out.println(arrivesTimesCopy);
        for (int aTime : arrivesTimesCopy) {
            if ((int) Duration.between(start, Instant.now()).toSeconds() == aTime) {
                // System.out.println(aTime);
                createProcess(aTime);
                arrivesTimesCopy.remove(arrivesTimesCopy.indexOf(aTime));
                break;
            }

        }

        generateQuantumTimes();

    }

    public void generateQuantumTimes() {

        sumOfBurstTimes = 0;
        if (readyQueue.size() != 0) {
            for (ProcessCreation item : readyQueue) {
                sumOfBurstTimes += item.burstTime;
            }

            quantumTime = (int) sumOfBurstTimes / readyQueue.size();
            // if (quantumTime == 0) {
            // quantumTime = 1;
            // }
        }
    }

    public void checkEntryCriticalTime() {
        entryTime = (int) Duration.between(start, Instant.now()).toSeconds();
        holdCriticalEntryTime.add(entryTime);

    }

    public void determineArrivalTimes() {
        arriveTimes.clear();
        arrivesTimesCopy.clear();
        int counter = 0;
        for (int i = 0; i < numberOfProcesses; i++) {
            counter = ThreadLocalRandom.current().nextInt(minArrivalTime, maxArrivalTime + 1);
            arriveTimes.add(counter);
        }

        arrivesTimesCopy.addAll(arriveTimes);
    }

}

class ProcessCreation { // built for creating new processes
    public int burstTime;
    public int arrivalTime;
    public int originalArrivalTime;
    public int quantumNumber;
    public int originalBurstTime;

    public int waitingTime;
    public int turnAroundTime;
    public int exitTime;
    static int nameUpdate = 0;
    public int processName = 0; // E.g Process 1

    public ProcessCreation(int arrivalTime, int minBurst, int maxBurst) {
        this.burstTime = ThreadLocalRandom.current().nextInt(minBurst, maxBurst + 1); // sets the burstTime to a random
                                                                                      // time in
        // 1 to 5 seconds. variable is subject to change

        this.originalBurstTime = this.burstTime; // keeps the original burst time, does not change
        // this.arrivalTime = ThreadLocalRandom.current().nextInt(1, 6);
        this.arrivalTime = arrivalTime;
        this.originalArrivalTime = this.arrivalTime;
        nameUpdate += 1;
        this.processName = nameUpdate;

    }
}