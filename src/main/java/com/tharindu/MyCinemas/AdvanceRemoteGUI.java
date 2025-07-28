package com.tharindu.MyCinemas;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

// Bridge interfaces and implementations
interface DeviceNew {
    void on();

    void off();

    boolean isEnabled();
}

class TvNew implements DeviceNew {
    private boolean on = false;

    @Override
    public void on() {
        on = true;
        System.out.println("[DeviceNew] TV switched ON");
    }

    @Override
    public void off() {
        on = false;
        System.out.println("[DeviceNew] TV switched OFF");
    }

    @Override
    public boolean isEnabled() {
        return on;
    }
}

class PlayerNew implements DeviceNew {
    private boolean on = false;

    @Override
    public void on() {
        on = true;
        System.out.println("[DeviceNew] Player switched ON");
    }

    @Override
    public void off() {
        on = false;
        System.out.println("[DeviceNew] Player switched OFF");
    }

    @Override
    public boolean isEnabled() {
        return on;
    }
}

abstract class RemoteNew {
    protected DeviceNew deviceNew;

    public RemoteNew(DeviceNew deviceNew) {
        this.deviceNew = deviceNew;
    }

    public void switchPower() {
        if (deviceNew.isEnabled()) deviceNew.off();
        else deviceNew.on();
    }
}

class BasicRemoteNew extends RemoteNew {
    public BasicRemoteNew(DeviceNew deviceNew) {
        super(deviceNew);
    }
}

// GUI Application
public class AdvanceRemoteGUI {
    private JPanel screenPanel;
    private CardLayout cardLayout;
    private RemoteNew remoteNew;
    private DeviceNew deviceNew;

    public AdvanceRemoteGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }
        selectDevice();
        initGUI();
    }

    private void selectDevice() {
        String[] options = {"TV Remote", "Player Remote"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select Remote Type:",
                "Choose Device",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice == 0) deviceNew = new TvNew();
        else if (choice == 1) deviceNew = new PlayerNew();
        else System.exit(0);
        remoteNew = new BasicRemoteNew(deviceNew);
    }

    private void initGUI() {
        JFrame frame = new JFrame("Bridge Pattern Remote Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Menu for a switching device
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Switch Device");
        JMenuItem switchItem = new JMenuItem("Switch Remotes");
        menu.add(switchItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        switchItem.addActionListener(e -> {
            selectDevice();
            updateScreen();
        });

        // Screen panel with CardLayout
        cardLayout = new CardLayout();
        screenPanel = new JPanel(cardLayout);
        screenPanel.setPreferredSize(new Dimension(600, 300));
        screenPanel.setBackground(Color.BLACK);

        // OFF state image panel
        JLabel offImage = new JLabel();
        offImage.setHorizontalAlignment(SwingConstants.CENTER);
        offImage.setVerticalAlignment(SwingConstants.CENTER);
        URL offUrl = getClass().getResource("/off.jpg");
        if (offUrl != null) {
            offImage.setIcon(new ImageIcon(offUrl));
        } else {
            offImage.setText("[off.png not found]");
            offImage.setForeground(Color.GRAY);
        }
        screenPanel.add(offImage, "OFF");

        // TV GIF
        JLabel tvVideo = new JLabel();
        tvVideo.setHorizontalAlignment(SwingConstants.CENTER);
        tvVideo.setVerticalAlignment(SwingConstants.CENTER);
        loadGif(tvVideo, "/tv.gif");
        screenPanel.add(tvVideo, "TV_ON");

        // Player GIF
        JLabel playerVideo = new JLabel();
        playerVideo.setHorizontalAlignment(SwingConstants.CENTER);
        playerVideo.setVerticalAlignment(SwingConstants.CENTER);
        loadGif(playerVideo, "/player.gif");    // <— no scaling
        screenPanel.add(playerVideo, "PLAYER_ON");

        // Remote control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton powerBtn = new JButton("Power");
        powerBtn.setPreferredSize(new Dimension(100, 40));
        controlPanel.add(powerBtn);

        powerBtn.addActionListener(e -> {
            remoteNew.switchPower();
            updateScreen();
        });

        // Layout
        frame.setLayout(new BorderLayout());
        frame.add(screenPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        updateScreen();
        frame.setVisible(true);
    }

    private void loadGif(JLabel label, String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            label.setIcon(new ImageIcon(url));
        } else {
            label.setText("[" + resourcePath.substring(1) + " not found]");
            label.setForeground(Color.WHITE);
        }
    }

    private void updateScreen() {
        if (!deviceNew.isEnabled()) {
            cardLayout.show(screenPanel, "OFF");
        } else if (deviceNew instanceof TvNew) {
            cardLayout.show(screenPanel, "TV_ON");
        } else {
            cardLayout.show(screenPanel, "PLAYER_ON");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdvanceRemoteGUI::new);
    }
}

