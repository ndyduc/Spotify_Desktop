package org.music.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ImagePanel extends JPanel {
    private Image backgroundImage;
    private int cornerRadius; // Bán kính góc bo tròn

    public ImagePanel(int radius) {
        super(); // Gọi constructor mặc định
        this.cornerRadius = radius;
    }

    public ImagePanel(LayoutManager layout) {
        super(layout); // Cho phép đặt layout tùy chỉnh
    }

    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        repaint();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Chuyển đổi Graphics thành Graphics2D để sử dụng tính năng nâng cao
        Graphics2D g2d = (Graphics2D) g.create();

        // Bật khử răng cưa để làm mượt các góc bo tròn
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo vùng clip với các góc bo tròn
        Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2d.setClip(clip);

        // Vẽ hình nền (nếu có)
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Vẽ các thành phần con
        super.paintComponent(g2d);

        // Giải phóng tài nguyên
        g2d.dispose();
    }
}