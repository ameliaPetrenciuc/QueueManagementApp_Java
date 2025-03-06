package simulation;
import logic.Scheduler;
import model.Server;
import model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SimulationFrame extends JFrame {
    public  JTextField t1,t2,t3, t4, t5, t6, t7;
    public JLabel label11 = new JLabel("Average Waiting Time:");
    public JLabel label12 = new JLabel("Average Service Time:");
    public JLabel label13= new JLabel("Peak Hour:");
    public JLabel label10= new JLabel("Queue Evolution: ");
    public JLabel label9= new JLabel("Waiting clients: ");
    public JLabel label8 = new JLabel("Current Time: ");
    public JTextArea field_waitingClients = new JTextArea();
    public JTextArea field_queue = new JTextArea();

    public JButton start = new JButton("Start");
    public JComboBox<String> comboBox;

    public SimulationFrame(int boardWidth, int boardHeight) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(boardWidth, boardHeight);
        Controller controller = new Controller(this);

        // Main panel
        JPanel principal = new JPanel();
        principal.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(principal);
        principal.setLayout(new GridLayout(1, 2));

        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createTitledBorder("DATA INPUT"));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        principal.add(leftPanel);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label1 = new JLabel("Number of clients");
        t1 = new JTextField(10);
        panel.add(label1);
        panel.add(t1);
        leftPanel.add(panel);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label2 = new JLabel("Number of queues");
        t2= new JTextField(10);
        panel2.add(label2);
        panel2.add(t2);
        leftPanel.add(panel2);


        JPanel panel3= new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label3 = new JLabel("Simulation interval");
        t3= new JTextField(10);
        panel3.add(label3);
        panel3.add(t3);
        leftPanel.add(panel3);

        JPanel panel4= new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label4 = new JLabel("Minim arrival time");
        t4= new JTextField(10);
        panel4.add(label4);
        panel4.add(t4);
        leftPanel.add(panel4);

        JPanel panel5= new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label5 = new JLabel("Maximum arrival time");
        t5= new JTextField(10);
        panel5.add(label5);
        panel5.add(t5);
        leftPanel.add(panel5);

        JPanel panel6= new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label6 = new JLabel("Minim service time");
        t6= new JTextField(10);
        panel6.add(label6);
        panel6.add(t6);
        leftPanel.add(panel6);

        JPanel panel7= new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label7 = new JLabel("Maxim service time");
        t7= new JTextField(10);
        panel7.add(label7);
        panel7.add(t7);
        leftPanel.add(panel7);

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
        leftPanel.add(comboPanel);

        comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"TimeStrategy", "ShortestQueueStrategy"}));
        comboBox.setMaximumSize(new Dimension(150, 25));
        comboPanel.add(comboBox);


        // Start button
        comboPanel.add(Box.createHorizontalStrut(5));
        comboPanel.add(start);

        start.addActionListener(controller);
        start.setActionCommand("Start");
        comboBox.addActionListener(controller);
        comboBox.setActionCommand("Change");

        // Right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createTitledBorder("SIMULATION"));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); // Set Y_AXIS alignment

        principal.add(rightPanel);

        JPanel panel8 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel8.add(label8);
        rightPanel.add(panel8);

        JPanel panel9 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel9.add(label9);
        rightPanel.add(panel9);


        field_waitingClients.setWrapStyleWord(true);
        field_waitingClients.setLineWrap(true);
        field_waitingClients.setEditable(false);
        field_waitingClients.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane scrollPane1 = new JScrollPane(field_waitingClients);
        scrollPane1.setPreferredSize(new Dimension(369, 58));
        rightPanel.add(scrollPane1);

        JPanel panel10 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel10.add(label10);
        rightPanel.add(panel10);

        field_queue.setWrapStyleWord(true);
        field_queue.setLineWrap(true);
        field_queue.setEditable(false);
        field_queue.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane scrollPane2 = new JScrollPane(field_queue);
        scrollPane2.setPreferredSize(new Dimension(369, 300));
        rightPanel.add(scrollPane2);

        JPanel panel11 = new JPanel();
        panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));

        panel11.add(label11);
        panel11.add(label12);
        panel11.add(label13);

        rightPanel.add(panel11);

        setVisible(true);
    }


    public void updateUI(List<Task> generatedTasks, int currentTime, List<Server> servers, int peakHour) {
        label8.setText("Current Time: " + currentTime);

        StringBuilder waitingClientsText = new StringBuilder();
        waitingClientsText.append("Waiting clients:\n");
        for (Task task : generatedTasks) {
            waitingClientsText.append("(").append(task.getArrivalTime()).append(" ").append(task.getServiceTime()).append(") ");
        }
        StringBuilder queueText = new StringBuilder();
        queueText.append("Queue Evolution:\n");
        int i=0;
        for (Server s : servers) {
            queueText.append("Queue " + i + ":");
            if (s.getTasks().isEmpty() && s.getCurrentTask() == null) {
                queueText.append(" (blocked)");
            } else {
                if (s.getCurrentTask() != null) {
                    Task currentTask = s.getCurrentTask();
                    queueText.append(" (" + currentTask.getArrivalTime() + " " + currentTask.getServiceTime() + ") ");
                }

                for (Task t : s.getTasks()) {
                    //System.out.println(t.toString());
                    queueText.append(" (" + t.getArrivalTime() + " " + t.getServiceTime() + ") ");
                }
            }
            queueText.append("\n");
            i++;
        }
        field_waitingClients.setText(waitingClientsText.toString());
        field_queue.setText(queueText.toString());

        label13.setText("Peak Hour:" + peakHour);
    }

    public JTextField getT1() {
        return t1;
    }

    public JTextField getT2() {
        return t2;
    }

    public JTextField getT3() {
        return t3;
    }

    public JTextField getT4() {
        return t4;
    }

    public JTextField getT5() {
        return t5;
    }

    public JTextField getT6() {
        return t6;
    }

    public JTextField getT7() {
        return t7;
    }

}
