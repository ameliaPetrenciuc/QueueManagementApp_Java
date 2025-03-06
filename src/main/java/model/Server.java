package model;

import simulation.SimulationManager;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private Task currentTask;
    public int totalServiceTime=0;
    public int clients=0;

    public Server() {
        this.tasks=new LinkedBlockingQueue<>();
        waitingPeriod=new AtomicInteger();
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public void run() {
        while (true) {
            try {
                currentTask=tasks.take();
                int taskTime=currentTask.getServiceTime();
                int i=0;
                while(i<taskTime){
                    Thread.sleep(1000);
                    currentTask.setServiceTime(currentTask.getServiceTime()-1);
                    waitingPeriod.decrementAndGet();
                    i++;
                }
                clients++;
                totalServiceTime+=taskTime;
                currentTask=null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

}