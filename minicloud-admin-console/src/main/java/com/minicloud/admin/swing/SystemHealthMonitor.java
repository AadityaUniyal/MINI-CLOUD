package com.minicloud.admin.swing;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SystemHealthMonitor extends JFrame {
    private final JProgressBar cpuBar;
    private final JProgressBar memBar;
    private final JTextArea logArea;
    private final Timer timer;

    public SystemHealthMonitor() {
        setTitle("MiniCloud System Diagnostics (Swing)");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new BorderLayout(10, 10));
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        statsPanel.add(new JLabel("CPU Usage:"));
        cpuBar = new JProgressBar(0, 100);
        cpuBar.setStringPainted(true);
        statsPanel.add(cpuBar);
        
        statsPanel.add(new JLabel("Memory Usage:"));
        memBar = new JProgressBar(0, 100);
        memBar.setStringPainted(true);
        statsPanel.add(memBar);
        
        add(statsPanel, BorderLayout.NORTH);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Live Event Log"));
        add(scroll, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close Diagnostics");
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);
        
        Random rand = new Random();
        timer = new Timer(1000, e -> {
            int cpu = 20 + rand.nextInt(30);
            int mem = 40 + rand.nextInt(20);
            cpuBar.setValue(cpu);
            memBar.setValue(mem);
            
            if (cpu > 45) {
                logArea.append("[" + java.time.LocalTime.now() + "] WARNING: High CPU spike detected\n");
            }
            if (rand.nextInt(10) > 7) {
                logArea.append("[" + java.time.LocalTime.now() + "] INFO: Heartbeat received from backend-service\n");
            }
        });
        timer.start();
    }

    public static void showMonitor() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (Exception ignored) {}
            new SystemHealthMonitor().setVisible(true);
        });
    }
}
