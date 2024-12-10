package org.music.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Rounded_Label extends JLabel {
    private int radius;
    private BufferedImage roundedImage;

    public Rounded_Label(ImageIcon icon, int radius) {
        super();
        this.radius = radius;
        if (icon != null) {
            setIcon(icon);
        }
    }

    private void createRoundedImage(ImageIcon icon) {
        if (icon != null && icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.drawImage(icon.getImage(), 0, 0, null);
            g2.dispose();

            // Tạo hình ảnh bo góc
            BufferedImage rounded = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rounded.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new RoundRectangle2D.Float(0, 0, img.getWidth(), img.getHeight(), radius, radius));
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();

            this.roundedImage = rounded;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (roundedImage != null) {
            g.drawImage(roundedImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            super.paintComponent(g);
        }
    }

    @Override
    public void setIcon(Icon icon) {
        if (icon instanceof ImageIcon) {
            createRoundedImage((ImageIcon) icon);
        } else {
            this.roundedImage = null;
        }
        super.setIcon(icon); // Cập nhật JLabel
    }

    public void setRadius(int radius) {
        this.radius = radius;
        if (this.getIcon() instanceof ImageIcon) {
            createRoundedImage((ImageIcon) this.getIcon());
            repaint();
        }
    }
}