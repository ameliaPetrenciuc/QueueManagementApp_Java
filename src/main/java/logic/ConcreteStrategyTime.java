package logic;

import model.Server;
import model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int minTime = Integer.MAX_VALUE;//waiting time
        Server min=null;
        for(Server s: servers){
           if(s.getWaitingPeriod().intValue()<minTime){
               minTime=s.getWaitingPeriod().intValue();
               min=s;
           }
        }
       if(min!=null){
           min.addTask(t);
           t.setWaitingTime(min.getWaitingPeriod().intValue());
       }
    }
}

