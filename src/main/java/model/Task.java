package model;

public class Task implements Comparable<Task>{

    private int arrivalTime;
    private int serviceTime;
    public int waitingTime;

    public Task( int arrivalTime, int serviceTime) {

        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        waitingTime=0;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String toString(){
        return "Arrival time: "+ this.arrivalTime+" serviceTime: "+this.serviceTime;
    }

    @Override
    public int compareTo(Task o) {
        return this.getArrivalTime()-o.getArrivalTime();
    }
}
