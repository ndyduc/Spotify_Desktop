package org.music.Components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static javax.swing.text.StyleConstants.getBackground;

public class Border_Radius extends JPanel {
    private int radius;

    public Border_Radius(int radius) {
        this.radius = radius;
        setOpaque(false);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());  // Lấy màu nền của panel
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius); // Bo các góc
    }
}