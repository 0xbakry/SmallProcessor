
import java.util.ArrayList;
import java.util.Scanner;

public class AGAT {

    /**
     * @param args the command line arguments
     */
    public static ArrayList<Process> AllProcess = new ArrayList<Process>();
    public static ArrayList<Process> readyProcess = new ArrayList<Process>();
    public static ArrayList<Process> deadList = new ArrayList<Process>();
    public static Scanner input;
    public static int processNumber = 0, processPriority = 0, processArrivalTime = 0, processBrustTime = 0, quantum = 0, maxRemainingBrust;
    public static String processName;
    public static int time = 0, sumOfBrust = 0;
    public static float last = 0;
    public static boolean change = false;

    public static void setV1() {
        for (int i = 0; i < AllProcess.size(); i++) {
            AllProcess.get(i).setv1(last);
        }

    }

    public static void setAttributes() {
        for (int i = 0; i < readyProcess.size(); i++) {
            readyProcess.get(i).setv2(maxBrust());
            readyProcess.get(i).setFactor(readyProcess.get(i).v1, readyProcess.get(i).v2);
        }

    }

    public static void FillReadyQueue() {
        while (true && !(AllProcess.isEmpty())) {
            if (AllProcess.get(0).arrival_time <= time) {

                readyProcess.add(AllProcess.get(0));
                AllProcess.remove(0);
                setAttributes();
            } else {
                break;
            }

        }

    }

    public static void finishedProcess(Process p, int time) {
        p.quantum_time = 0;
        p.turn_around_time = time - p.arrival_time;
        p.waiting_time = p.turn_around_time - p.brust_time;
        deadList.add(p);
        readyProcess.remove(p);
        System.out.println("Quantum Time: " + '0');
    }

    public static void betterAGAT(Process p, int act) {
        readyProcess.remove(p);
        readyProcess.add(p);
        p.quantum_time += (p.quantum_time - act);
    }

    public static float maxBrust() {
        float max = -1;
        for (int i = 0; i < readyProcess.size(); i++) {
            if (readyProcess.get(i).remaining_brust_time > max) {
                max = readyProcess.get(i).remaining_brust_time;
            }
        }
        return max;
    }

    public static Process miniAgat() {
        float mini = 9999;
        int k = 0;
        for (int i = 0; i < readyProcess.size(); i++) {
            if (readyProcess.get(i).agat_factor <= mini) {
                mini = readyProcess.get(i).agat_factor;
                k = i;
            }

        }
        return readyProcess.get(k);

    }

    public static void execution() {
        Process process1;
        if (time < sumOfBrust) {
            int active = 0; // TO KEEP TRACK EACH CYCLE
            FillReadyQueue();
            if (!change) {
                process1 = readyProcess.get(0);   //PICK THE FIRST PROCESS
                System.out.println("Runing: " + process1.name);
            } else {
                process1 = miniAgat();
                System.out.println("Runing: " + process1.name);
                change = false;
            }

            System.out.println("Quantum Time: " + process1.quantum_time);

            time += process1.quantum40();
            active += process1.quantum40();
            process1.remaining_brust_time -= active;

            if (process1.remaining_brust_time == 0) /// CASE 3 "HAS FINISHED ITS JOP"
            {
                finishedProcess(process1, time);

            } else {
                FillReadyQueue();
                if (miniAgat() != process1) /// CASE 2 "BETTER AGAT"
                {
                    betterAGAT(process1, active);
                    change = true;
                } else /// CASE 1 "CURRENT PROCESS STILL RUNING"
                {
                    for (int i = time; i < sumOfBrust; i++) {
                        if (process1.remaining_brust_time == 0) {
                            finishedProcess(process1, time);

                            break;
                        }

                        time++;
                        active++;
                        process1.remaining_brust_time--;

                        if (process1.quantum_time - active == 0) {
                            readyProcess.remove(process1);
                            readyProcess.add(process1);
                            process1.quantum_time += 2;
                            break;
                        } else {
                            FillReadyQueue();
                            if (miniAgat() != process1) {
                                change = true;
                                betterAGAT(process1, active);
                                break;
                            }
                        }

                    }
                }
            }
            System.out.println("Remaining Brust: " + process1.remaining_brust_time);
            System.out.println("Current Process's AGAT factor: " + process1.agat_factor);
            System.out.println("--------------------------------------------------");

            execution();
        }
    }

    public static void main(String[] args) {

        float avgWaiting = 0, avgTurnAround = 0;
        input = new Scanner(System.in);
        System.out.println("Enter the number of process need to execute:   ");
        processNumber = input.nextInt();

        for (int i = 0; i < processNumber; i++) {
            System.out.println("Enter the Process " + (i + 1) + " Name : ");
            processName = input.next();

            System.out.println("Enter the Process " + (i + 1) + " Priority : ");
            processPriority = input.nextInt();

            System.out.println("Enter the Process " + (i + 1) + " Arrival Time : ");
            processArrivalTime = input.nextInt();

            System.out.println("Enter the Process " + (i + 1) + " Brust Time : ");
            processBrustTime = input.nextInt();

            System.out.println("Enter the Process " + (i + 1) + " Quantum Time : ");
            quantum = input.nextInt();

            Process newProcess = new Process(processName, processPriority, processArrivalTime, processBrustTime, quantum);
            sumOfBrust += processBrustTime;
            AllProcess.add(newProcess);
        }

        last = AllProcess.get(processNumber - 1).arrival_time;
        setV1();

        execution();

        System.out.println("--------------------------------------------------" + "\n" + "\n");

        for (int i = 0; i < deadList.size(); i++) {
            avgWaiting += deadList.get(i).waiting_time;
            avgTurnAround += deadList.get(i).turn_around_time;
            System.out.println("Turnaround Time For " + deadList.get(i).name + " : " + deadList.get(i).turn_around_time);
            System.out.println("Waiting Time For " + deadList.get(i).name + " : " + (deadList.get(i).waiting_time));
            System.out.println("--------------------------------------------------" + "\n");

        }

        avgWaiting /= deadList.size();
        avgTurnAround /= deadList.size();
        System.out.println("Total Average Waiting Time : " + avgWaiting);
        System.out.println("Total Average Turn Around Time : " + avgTurnAround);

        /*
        Process p1 = new Process("p1", 4, 0, 17, 4);
        Process p2 = new Process("p2", 9, 3, 6, 3);
        Process p3 = new Process("p3", 3, 4, 10, 5);
        Process p4 = new Process("p4", 10, 29, 4, 2);
        AllProcess.add(p1);
        AllProcess.add(p2);
        AllProcess.add(p3);
        AllProcess.add(p4);

        last = AllProcess.get(3).arrival_time;
        setV1();
     
        
        execution();
         */
    }
}
