package com.tharindu.MyCinemas;

import javax.swing.*;
import java.awt.*;

// Bridge interfaces and implementations
interface Device {
    void on();
    void off();
    boolean isEnabled();
}

class Tv implements DeviceNew {
    private boolean on = false;

    @Override
    public void on() {
        on = true;
        System.out.println("TvNew on");
    }

    @Override
    public void off() {
        on = false;
        System.out.println("TvNew off");
    }

    @Override
    public boolean isEnabled() {
        return on;
    }
}

class Player implements DeviceNew {
    private boolean on = false;

    @Override
    public void on() {
        on = true;
        System.out.println("PlayerNew on");
    }

    @Override
    public void off() {
        on = false;
        System.out.println("PlayerNew off");
    }

    @Override
    public boolean isEnabled() {
        return on;
    }
}

abstract class Remote {
    protected DeviceNew deviceNew;

    public Remote(DeviceNew deviceNew) {
        this.deviceNew = deviceNew;
    }

    public void switchPower() {
        if (deviceNew.isEnabled()) deviceNew.off();
        else deviceNew.on();
    }
}

class BasicRemote extends RemoteNew {
    public BasicRemote(DeviceNew deviceNew) {
        super(deviceNew);
    }
}

// GUI Application
public class BasicRemoteGUI {
    private JPanel screenPanel;
    private final RemoteNew remoteNew;
    private DeviceNew deviceNew;

    public BasicRemoteGUI() {
        // Ask user which deviceNew to control
        Object[] options = {"TV RemoteNew", "PlayerNew RemoteNew"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select RemoteNew Type:",
                "Choose DeviceNew",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice == 0) {
            deviceNew = new TvNew();
        } else if (choice == 1) {
            deviceNew = new PlayerNew();
        } else {
            System.exit(0);
        }
        remoteNew = new BasicRemoteNew(deviceNew);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Bridge Pattern RemoteNew Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout(10, 10));

        // Center panel for deviceNew screen
        screenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (deviceNew.isEnabled()) {
                    // ON state
                    setBackground(Color.BLUE.darker());
                    g.setColor(Color.WHITE);
                    g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));
                    g.drawString(getDeviceName() + " ON", 50, getHeight() / 2);
                } else {
                    // OFF state
                    setBackground(Color.BLACK);
                }
            }
        };
        frame.add(screenPanel, BorderLayout.CENTER);

        // RemoteNew control buttons
        JPanel remotePanel = new JPanel();
        remotePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton powerBtn = new JButton("Power");
        remotePanel.add(powerBtn);
        frame.add(remotePanel, BorderLayout.SOUTH);

        // Power button action
        powerBtn.addActionListener(e -> {
            remoteNew.switchPower();
            screenPanel.repaint();
        });

        // Center the window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String getDeviceName() {
        return deviceNew instanceof TvNew ? "TV" : "PlayerNew";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BasicRemoteGUI::new);
    }
}