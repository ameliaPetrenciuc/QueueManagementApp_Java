package logic;

import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private List<Server> servers;
    private int maxNoServers;
    private Strategy strategy;

    public Scheduler(int maxNoServers) {

        this.maxNoServers = maxNoServers;
        this.servers = new ArrayList<>();
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {

        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(Task t) {
        strategy.addTask(servers,t);
    }

    public List<Server> getServers() {
        return servers;
    }
}