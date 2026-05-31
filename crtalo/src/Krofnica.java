

import java.awt.*;
import java.awt.geom.*;
import java.awt.Graphics2D;


public class Krofnica implements DrawableShape {

    private final int cx, cy, outerRadius, innerRadius;
    private final Color edgeColor;
    private final Color fillColor; // null = no fill
    private final float strokeWidth;

    public Krofnica(int cx, int cy, int outerRadius, int innerRadius,
                 Color edgeColor, Color fillColor, float strokeWidth) {
        this.cx = cx; this.cy = cy;
        this.outerRadius = Math.abs(outerRadius);
        this.innerRadius = Math.abs(innerRadius);
        this.edgeColor   = edgeColor;
        this.fillColor   = fillColor;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics2D g2d) {
        Ellipse2D outer = new Ellipse2D.Double(
                cx - outerRadius, cy - outerRadius, outerRadius * 2.0, outerRadius * 2.0);
        Ellipse2D inner = new Ellipse2D.Double(
                cx - innerRadius, cy - innerRadius, innerRadius * 2.0, innerRadius * 2.0);

        Area donut = new Area(outer);
        donut.subtract(new Area(inner));

        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fill(donut);
        }
        g2d.setColor(edgeColor);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(donut);
    }
}
