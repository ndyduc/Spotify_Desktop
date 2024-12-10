package org.music.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Radius_With_Image extends JPanel {
    private int radius;
    private ImageIcon backgroundIcon;

    public Radius_With_Image(int radius, ImageIcon backgroundIcon) {
        this.radius = radius;
        this.backgroundIcon = backgroundIcon;
        setOpaque(false); // Đảm bảo nền trong suốt để hiển thị bo góc
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Kích hoạt khử răng cưa để đồ họa mượt mà
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ hình nền nếu có
        if (backgroundIcon != null) {
            g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        // Vẽ bo góc
        g2.setClip(null); // Reset clip sau khi vẽ hình nền
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    }

    public void setBackgroundIcon(ImageIcon backgroundIcon) {
        this.backgroundIcon = backgroundIcon;
        repaint(); // Cập nhật giao diện khi thay đổi ảnh nền
    }
}