package org.music.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class ShapeIcon implements Icon {

    private final Shape shape;
    private final Paint paint;
    private final Color color;
    private final int size;
    private final boolean fill;
    private final Stroke stroke;

    public ShapeIcon(final Shape shape, final Color color, final int size) {
        this(shape, color, size, true, new BasicStroke(0.5f));
    }

    public ShapeIcon(final Shape shape, final Color color, final int size, final boolean fill, final Stroke stroke) {
        this.stroke = stroke;
        this.fill = fill;
        this.color = color;
        // allow for customization of fill color/gradient
        // a plain color works just as well—this is a little fancier
        this.paint = new GradientPaint(0, 12, color.brighter(), 0, 20, color);
        this.size = size;
        // you could also define different constructors for different Shapes
        if (shape instanceof Path2D) {
            this.shape = ((Path2D)shape).createTransformedShape(AffineTransform.getScaleInstance(size, size));
        } else if (shape instanceof Area) {
            this.shape = ((Area) shape).createTransformedArea(AffineTransform.getScaleInstance(size, size));
        } else {
            this.shape = new Area(shape).createTransformedArea(AffineTransform.getScaleInstance(size, size));
        }
    }

    @Override
    public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
        final Graphics2D g2d = (Graphics2D)g.create();
        g2d.translate(x, y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (fill) {
            g2d.setPaint(paint);
            g2d.fill(shape);
        }
        g2d.setPaint(color);
        g2d.setStroke(stroke);
        g2d.draw(shape);
        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}