

import java.awt.*;
import java.awt.Graphics2D;

//Povuces jednu prevarantsku
public class Linija implements DrawableShape {

    private final int x1, y1, x2, y2;
    private final Color color;
    private final float strokeWidth;

    public Linija(int x1, int y1, int x2, int y2, Color color, float strokeWidth) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1, y1, x2, y2);
    }
}
