package logic;

import model.Server;
import model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int minSize = Integer.MAX_VALUE;//waiting time
        Server min=null;
        for(Server s: servers){
            if(s.getTasks().isEmpty() &&  s.getCurrentTask()==null){
                min=s;
                break;
            }else if(s.getTasks().size()<minSize ){
                minSize=s.getTasks().size();
                min=s;
            }
        }
        if(min!=null){
            min.addTask(t);
            t.setWaitingTime(min.getWaitingPeriod().intValue());
        }
    }

}

