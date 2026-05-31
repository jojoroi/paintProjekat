

import java.awt.*;
import java.awt.Graphics2D;


public class Krug implements DrawableShape {

    private final int cx, cy, radius;
    private final Color edgeColor;
    private final Color fillColor; // null = no fill
    private final float strokeWidth;

    public Krug(int cx, int cy, int radius,
                  Color edgeColor, Color fillColor, float strokeWidth) {
        this.cx = cx; this.cy = cy;
        this.radius = Math.abs(radius);
        this.edgeColor   = edgeColor;
        this.fillColor   = fillColor;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics2D g2d) {
        int x = cx - radius;
        int y = cy - radius;
        int d = radius * 2;

        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fillOval(x, y, d, d);
        }
        g2d.setColor(edgeColor);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawOval(x, y, d, d);
    }
}
