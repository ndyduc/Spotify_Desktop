package org.music.Activity;

import org.music.Components.Border_Radius;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class House extends Border_Radius {

    public House(int radius, String user_secret) {
        super(radius);
        setBackground(Color.decode("#1a1a1a"));
        setBorder(new EmptyBorder(10,10,10,10));

    }
}
