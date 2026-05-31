import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JFrame {

    private Canvas canvas;
    private JButton btnUndo;
    private Color strokeColor = Color.BLACK;

    private static final Color[] PALETTE = {
        Color.BLACK, Color.WHITE,
        new Color(192,0,0),   Color.RED,
        new Color(255,128,0), Color.YELLOW,
        new Color(0,128,0),   Color.GREEN,
        new Color(0,0,192),   Color.CYAN,
        new Color(128,0,128), new Color(255,105,180),
        new Color(101,67,33), Color.GRAY,
        new Color(192,192,192)
    };

    public Panel() {
        setTitle("Paint");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new Canvas(800, 560);
        add(buildToolbar(), BorderLayout.NORTH);
        add(canvas,         BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JToolBar buildToolbar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setBackground(new Color(240, 240, 240));
        tb.setBorder(new MatteBorder(0, 0, 1, 0, new Color(180, 180, 180)));

        // ── tool buttons ──────────────────────────────────────────────────────
        ButtonGroup bg = new ButtonGroup();
        String[][] tools = {
            {"Linija",        "LINE"},
            {"Cetvorougao",  "RECTANGLE"},
            {"Krug",          "CIRCLE"},
            {"Krofnica", "DONUT"}
        };
        for (String[] t : tools) {
            JToggleButton btn = new JToggleButton(t[0]);
            btn.setFocusPainted(false);
            final Canvas.Tool tool = Canvas.Tool.valueOf(t[1]);
            btn.addActionListener(e -> canvas.setTool(tool));
            bg.add(btn);
            tb.add(btn);
            if (t[1].equals("LINE")) btn.setSelected(true);
        }

        tb.addSeparator(new Dimension(12, 0));

        // ── stroke width ──────────────────────────────────────────────────────
        tb.add(new JLabel("debljina: "));
        SpinnerNumberModel spinModel = new SpinnerNumberModel(2.0, 0.5, 20.0, 0.5);
        JSpinner spinner = new JSpinner(spinModel);
        spinner.setMaximumSize(new Dimension(65, 28));
        spinner.setPreferredSize(new Dimension(65, 28));
        spinner.addChangeListener(e ->
            canvas.setStrokeWidth(((Double) spinModel.getValue()).floatValue()));
        tb.add(spinner);
        tb.addSeparator(new Dimension(12, 0));

        tb.add(new JLabel("Boja: "));
        JPanel activeBox = new JPanel();
        activeBox.setBackground(strokeColor);
        activeBox.setPreferredSize(new Dimension(24, 24));
        activeBox.setMaximumSize(new Dimension(24, 24));
        activeBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        activeBox.setOpaque(true);
        tb.add(activeBox);

        tb.addSeparator(new Dimension(4, 0));

        for (Color c : PALETTE) {
            JPanel swatch = new JPanel();
            swatch.setBackground(c);
            swatch.setPreferredSize(new Dimension(18, 18));
            swatch.setMaximumSize(new Dimension(18, 18));
            swatch.setOpaque(true);
            swatch.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            swatch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            swatch.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    strokeColor = c;
                    activeBox.setBackground(c);
                    activeBox.repaint();
                    canvas.setStrokeColor(c);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    swatch.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                }
                @Override public void mouseExited(MouseEvent e) {
                    swatch.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                }
            });
            tb.add(swatch);
        }

        tb.addSeparator(new Dimension(12, 0));

        btnUndo = new JButton("↩ Undo");
        btnUndo.setFocusPainted(false);
        btnUndo.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnUndo.setBackground(new Color(70, 130, 180));
        btnUndo.setForeground(Color.WHITE);
        btnUndo.setOpaque(true);
        btnUndo.setBorderPainted(false);
        btnUndo.addActionListener(e -> doUndo());
        tb.add(btnUndo);

        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlZ, "undo");
        getRootPane().getActionMap().put("undo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { doUndo(); }
        });

        return tb;
    }

    private void doUndo() {
        canvas.undo();
        btnUndo.setEnabled(canvas.canUndo());
    }
}
