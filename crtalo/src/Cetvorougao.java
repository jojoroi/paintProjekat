

import java.awt.*;
import java.awt.Graphics2D;

public class Cetvorougao implements DrawableShape {

    private final int x, y, width, height;
    private final Color edgeColor;
    private final Color fillColor; // null = no fill
    private final float strokeWidth;

    public Cetvorougao(int x, int y, int width, int height,
                Color edgeColor, Color fillColor, float strokeWidth) {
        this.x = width  >= 0 ? x : x + width;
        this.y = height >= 0 ? y : y + height;
        this.width  = Math.abs(width);
        this.height = Math.abs(height);
        this.edgeColor   = edgeColor;
        this.fillColor   = fillColor;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fillRect(x, y, width, height);
        }
        g2d.setColor(edgeColor);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawRect(x, y, width, height);
    }
}
