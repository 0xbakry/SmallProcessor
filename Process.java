/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class Process {

    public String name;
    public int arrival_time;
    public int brust_time;
    public int remaining_brust_time;
    public int priority;
    public int quantum_time;
    public int turn_around_time;
    public int waiting_time;
    public int agat_factor;
    float v1, v2;

    public void setFactor(float v1, float v2) { // v1 and v2 are inputs from the AGATScheduler

        agat_factor = (int) ((10 - priority) + Math.ceil(arrival_time / v1) + Math.ceil(remaining_brust_time / v2));

    }

    public void setv1(float last) {
        float res;
        if (last > 10) {
            res = (last / 10);
        } else {
            res = 1;
        }
        v1 = res;
    }

    public void setv2(float max) {
        float res;
        if (max > 10) {
            res = (max / 10);
        } else {
            res = 1;
        }
        v2 = res;
    }

    public int quantum40() { // returns the Quantum -> round(40%)
        return (int) Math.round(quantum_time * 0.4);
    }

    public Process(String name, int priority, int arrival_time, int brust_time, int quantum) {
        this.name = name;
        this.priority = priority;
        this.arrival_time = arrival_time;
        this.brust_time = brust_time;
        remaining_brust_time = brust_time;
        this.quantum_time = quantum;
    }

    public Process(String name) {
        this.name = name;
    }

}
