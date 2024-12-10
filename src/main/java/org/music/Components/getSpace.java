package org.music.Components;

import javax.swing.*;
import java.awt.*;

public class getSpace extends JPanel {
    int width;
    int height;

    public getSpace(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setBackground(Color.BLACK); // Đặt màu nền cho getSpace
    }
}