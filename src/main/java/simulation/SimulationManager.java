package simulation;

import logic.Scheduler;
import logic.SelectionPolicy;
import model.Server;
import model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationManager implements Runnable {

    public int timeLimit;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int maxServiceTime;
    public int minServiceTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy;
    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;
    float averageWaitingTime=0;
    private int timePeakHour = 0;
    private int maxClients=0;


    public SimulationManager(int timeLimit, int maxArrivalTime, int minArrivalTime, int maxServiceTime, int minServiceTime, int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy, SimulationFrame sf) {
        {
            this.timeLimit = timeLimit;
            this.maxArrivalTime = maxArrivalTime;
            this.minArrivalTime = minArrivalTime;
            this.minServiceTime = minServiceTime;
            this.maxServiceTime = maxServiceTime;
            this.numberOfServers = numberOfServers;
            this.numberOfClients = numberOfClients;
            this.selectionPolicy = selectionPolicy;
            this.frame = sf;
            scheduler = new Scheduler(numberOfServers);
            scheduler.changeStrategy(selectionPolicy);
            generateNRandomTasks();
        }
    }

    public List<Task> getGeneratedTasks() {
        return generatedTasks;
    }

    private void generateNRandomTasks() {
        int n = numberOfClients;
        generatedTasks = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            Task newTask = new Task(arrivalTime, serviceTime);
            generatedTasks.add(newTask);
        }
        Collections.sort(generatedTasks);
    }

    @Override
    public void run() {
        int currentTime = 0;

        while (currentTime < timeLimit) {

            Iterator<Task> iterator = generatedTasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(task);
                    averageWaitingTime=averageWaitingTime+task.getWaitingTime();
                    iterator.remove();
                }
            }
            frame.updateUI(generatedTasks,currentTime,scheduler.getServers(),timePeakHour);
            getPeakHour(scheduler.getServers(), currentTime);
            file(currentTime, generatedTasks, scheduler.getServers());

            if (generatedTasks.isEmpty()) {
                int nr = 0;
                for (Server s : scheduler.getServers()) {
                    if (s.getTasks().isEmpty() && s.getCurrentTask() == null) {
                        nr++;
                    }
                }
                if (nr == scheduler.getServers().size()) {
                    break;
                }
            }
            double totalWaitingTime = 0;
            for (Server server : scheduler.getServers()) {
                totalWaitingTime += server.getWaitingPeriod().doubleValue();
            }
            currentTime++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        averageWaitingTime=averageWaitingTime/numberOfClients;
        displayStatistics();
    }


    public void file(int currentTime, List<Task> generatedTasks, List<Server> servers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("task.txt", true))) {
            writer.newLine();
            writer.write("Current time: " + currentTime);
            writer.newLine();
            writer.write("Waiting clients: ");
            for (Task task : generatedTasks) {
                writer.write("(" + task.getArrivalTime() + " " + task.getServiceTime() + ") ");
            }
            writer.newLine();
            int i = 0;
            for (Server s : servers) {
                writer.write("Queue " + i + ":");
                if (s.getTasks().isEmpty() && s.getCurrentTask() == null) {
                    writer.write(" (blocked)");
                } else {
                    if (s.getCurrentTask() != null) {
                        Task task = s.getCurrentTask();
                        writer.write(" (" + task.getArrivalTime() + " " + task.getServiceTime() + ") ");
                    }
                    for (Task task : s.getTasks()) {
                        writer.write("(" + task.getArrivalTime() + " " + task.getServiceTime() + ") ");
                    }
                }
                writer.newLine();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayStatistics() {
        frame.label12.setText("Average Service Time:" + getAverageServiceTime());
        frame.label11.setText("Average Waiting Time:"+ averageWaitingTime );
        try {
            FileWriter wr = new FileWriter("task.txt",true);
            wr.write("---------------------------------------------"+"\n");
            wr.write("Statistics " +"\n");
            wr.write("Average Waiting Time: " + averageWaitingTime + "\n");
            wr.write("Average Service Time: " + getAverageServiceTime() + "\n");
            wr.write("Peak Hour: " + timePeakHour);
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public float getAverageServiceTime() {
        int clients=0;
        int timp=0;
        for(Server s:scheduler.getServers()){
            clients+=s.clients;
            timp+=s.totalServiceTime;
        }
        return  (float) timp /clients;
    }


    public void getPeakHour(List<Server> server, int time) {
        int clients = 0;
        for (Server s : server) {
            if(s.getCurrentTask()!= null){
                clients+=1;
            }
            clients += s.getTasks().size();
        }
        if (clients > maxClients ){
          maxClients=clients;
          timePeakHour=time;
        }
       // System.out.println(timePeakHour);
    }

}





