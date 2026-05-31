import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
public class Canvas extends JPanel {

    public enum Tool { LINE, RECTANGLE, CIRCLE, DONUT }

    private BufferedImage image;
    private Graphics2D    g2d;

    private Tool    currentTool  = Tool.LINE;
    private Color   strokeColor  = Color.BLACK;
    private float   strokeWidth  = 2f;

    private Point   firstClick   = null;
    private Point   mousePos     = null;

    private final Deque<BufferedImage> undoStack = new ArrayDeque<>();
    private static final int MAX_UNDO = 30;

    public Canvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        initCanvas(width, height);
        addMouseListeners();
        addMouseMotionListeners();
    }

    private void initCanvas(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d   = createG2D(image);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
    }

    private Graphics2D createG2D(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g;
    }

    public void setTool(Tool t)           { currentTool = t; firstClick = null; mousePos = null; repaint(); }
    public void setStrokeColor(Color c)   { strokeColor = c; }
    public void setStrokeWidth(float w)   { strokeWidth = w; }

    public void undo() {
        if (!undoStack.isEmpty()) {
            image = undoStack.pop();
            g2d   = createG2D(image);
            firstClick = null;
            mousePos   = null;
            repaint();
        }
    }

    public boolean canUndo() { return !undoStack.isEmpty(); }

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (firstClick == null) {
                    firstClick = e.getPoint();
                    mousePos   = e.getPoint();
                    repaint();
                } else {
                    commitShape(firstClick, e.getPoint());
                    firstClick = null;
                    mousePos   = null;
                    repaint();
                }
            }
        });
    }

    private void addMouseMotionListeners() {
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (firstClick != null) {
                    mousePos = e.getPoint();
                    repaint(); 
                }
            }
        });
    }

    private void commitShape(Point p1, Point p2) {
        pushUndo();
        buildShape(p1, p2).draw(g2d);
    }

    private DrawableShape buildShape(Point p1, Point p2) {
        return switch (currentTool) {
            case LINE      -> new Linija(p1.x, p1.y, p2.x, p2.y,
                                       strokeColor, strokeWidth);
            case RECTANGLE -> new Cetvorougao(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y,
                                       strokeColor, null, strokeWidth);
            case CIRCLE    -> {
                int r = (int) p1.distance(p2);
                yield new Krug(p1.x, p1.y, r, strokeColor, null, strokeWidth);
            }
            case DONUT     -> {
                int outerR = (int) p1.distance(p2);
                int innerR = Math.max(1, outerR / 2);
                yield new Krofnica(p1.x, p1.y, outerR, innerR,
                                strokeColor, null, strokeWidth);
            }
        };
    }

    private void pushUndo() {
        if (undoStack.size() >= MAX_UNDO) undoStack.removeLast();
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D cg = copy.createGraphics();
        cg.drawImage(image, 0, 0, null);
        cg.dispose();
        undoStack.push(copy);
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);

        if (firstClick != null && mousePos != null) {
            Graphics2D pg = (Graphics2D) g.create();
            pg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            pg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
            buildShape(firstClick, mousePos).draw(pg);
            pg.dispose();
        }

        if (firstClick != null) {
            g.setColor(strokeColor);
            g.fillOval(firstClick.x - 4, firstClick.y - 4, 8, 8);
        }
    }
}
