package org.music.Components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius;
    private int hgap; // Khoảng cách ngang
    private int vgap; // Khoảng cách dọc
    private Color borderColor = Color.decode("#1a1a1a");
    private int borderWidth = 3;

    public RoundedPanel(int radius, Color bgColor, int hgap, int vgap) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.hgap = hgap;
        this.vgap = vgap;
        setOpaque(false);
        setLayout(new BorderLayout(hgap, vgap));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền với bo góc
        g2.setColor(backgroundColor);
        g2.fillRoundRect(hgap, vgap, getWidth() - 2 * hgap, getHeight() - 2 * vgap, cornerRadius, cornerRadius);

        // Vẽ viền bên trong với độ dày viền
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderWidth)); // Đặt độ dày viền
        g2.drawRoundRect(hgap, vgap, getWidth() - 2 * hgap, getHeight() - 2 * vgap, cornerRadius, cornerRadius);
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        repaint();
    }
}