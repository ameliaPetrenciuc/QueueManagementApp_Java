package simulation;

import logic.SelectionPolicy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private final SimulationFrame sf;
    private SelectionPolicy select;

    public Controller(SimulationFrame sf) {
        this.sf = sf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == sf.start){
            int nrClients = sf.getT1().getText().isEmpty() ? 0 : Integer.parseInt(sf.getT1().getText());
            int nrQueues = sf.getT2().getText().isEmpty() ? 0 : Integer.parseInt(sf.getT2().getText());
            int maxArr = sf.getT5().getText().isEmpty() ? 0 : Integer.parseInt(sf.getT5().getText());
            int minArr = sf.getT4().getText().isEmpty() ? 0 : Integer.parseInt(sf.getT4().getText());
            int maxServ = sf.getT7().getText().isEmpty() ? 0 : Integer.parseInt(sf.getT7().getText());
            int minServ = sf.getT6().getText().isEmpty() ? 0 : Integer.parseInt(sf.getT6().getText());
            int totalTime = sf.getT3().getText().isEmpty() ? 0 : Integer.parseInt(sf.getT3().getText());
            System.out.println(select);
            SimulationManager mng=new SimulationManager(totalTime, maxArr, minArr, maxServ, minServ, nrQueues, nrClients,select,sf);
            Thread t =new Thread(mng);
            t.start();
        }
        if(e.getActionCommand().equals("Change")){
            String combo= (String) sf.comboBox.getSelectedItem();
            if(combo.equals("TimeStrategy")){
                select=SelectionPolicy.SHORTEST_TIME;
            }else {
                select=SelectionPolicy.SHORTEST_QUEUE;
            }
            System.out.println(select);
        }

    }
}
