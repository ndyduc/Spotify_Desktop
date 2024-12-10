package org.music.Components;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel() {
        super(); // Gọi constructor mặc định
    }

    public ImagePanel(LayoutManager layout) {
        super(layout); // Cho phép đặt layout tùy chỉnh
    }

    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Vẽ các thành phần con trước
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}