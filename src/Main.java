import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import javax.swing.ImageIcon;

public class Main extends JFrame {

    private String currentTool = "Line";  // Herramienta de dibujo seleccionada
    private Color currentColor = Color.BLACK;  // Color seleccionado
    private Stroke currentStroke = new BasicStroke(2.0f);  // Tipo de línea seleccionado
    private DrawingPanel drawingPanel;

    public Main() {
        setTitle("Practica 04 - Paint 2.0 Talavera Felix Arnoldo Fabian");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);


        // Centrar la ventana en la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        setVisible(true);

        drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.WHITE);
        add(drawingPanel, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();

        // Crear y cargar imágenes
        ImageIcon lineIcon = resizeIcon(new ImageIcon("src/imagenes/pencil.png"), 20, 20);
        ImageIcon rectIcon = resizeIcon(new ImageIcon("src/imagenes/rectangle.png"), 20, 20);
        ImageIcon ovalIcon = resizeIcon(new ImageIcon("src/imagenes/circle.png"), 20, 20);
        ImageIcon blackColorIcon = resizeIcon(new ImageIcon("src/imagenes/black_point.png"), 20, 20);
        ImageIcon redColorIcon = resizeIcon(new ImageIcon("src/imagenes/red_point.png"), 20, 20);
        ImageIcon blueColorIcon = resizeIcon(new ImageIcon("src/imagenes/blue_point.png"), 20, 20);
        ImageIcon solidLineIcon = resizeIcon(new ImageIcon("src/imagenes/line_icon.png"), 20, 20);
        ImageIcon dashedLineIcon = resizeIcon(new ImageIcon("src/imagenes/line_lineas.png"), 20, 20);
        ImageIcon dottedLineIcon = resizeIcon(new ImageIcon("src/imagenes/line_punteada.png"), 20, 20);
        ImageIcon clearIcon = resizeIcon(new ImageIcon("src/imagenes/eraser.png"), 20, 20);

        // Crear botones con imágenes
        JButton lineButton = new JButton(lineIcon);
        JButton rectButton = new JButton(rectIcon);
        JButton ovalButton = new JButton(ovalIcon);
        JButton blackColorButton = new JButton(blackColorIcon);
        JButton redColorButton = new JButton(redColorIcon);
        JButton blueColorButton = new JButton(blueColorIcon);
        JButton solidLineButton = new JButton(solidLineIcon);
        JButton dashedLineButton = new JButton(dashedLineIcon);
        JButton dottedLineButton = new JButton(dottedLineIcon);
        JButton clearButton = new JButton(clearIcon);

        // Asignar acciones a los botones
        lineButton.addActionListener(e -> currentTool = "Line");
        rectButton.addActionListener(e -> currentTool = "Rect");
        ovalButton.addActionListener(e -> currentTool = "Oval");
        blackColorButton.addActionListener(e -> currentColor = Color.BLACK);
        redColorButton.addActionListener(e -> currentColor = Color.RED);
        blueColorButton.addActionListener(e -> currentColor = Color.BLUE);
        solidLineButton.addActionListener(e -> currentStroke = new BasicStroke(2.0f));
        dashedLineButton.addActionListener(e -> currentStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
        dottedLineButton.addActionListener(e -> currentStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2, 10}, 0));
        clearButton.addActionListener(e -> drawingPanel.clearDrawing());

        // Añadir botones a la barra de herramientas
        toolBar.add(lineButton);
        toolBar.add(rectButton);
        toolBar.add(ovalButton);
        toolBar.addSeparator();
        toolBar.add(blackColorButton);
        toolBar.add(redColorButton);
        toolBar.add(blueColorButton);
        toolBar.addSeparator();
        toolBar.add(solidLineButton);
        toolBar.add(dashedLineButton);
        toolBar.add(dottedLineButton);
        toolBar.addSeparator();
        toolBar.add(clearButton);

        add(toolBar, BorderLayout.NORTH);

        setVisible(true);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    class DrawingPanel extends JPanel {
        private List<Shape> shapes = new ArrayList<>();
        private List<Color> shapeColors = new ArrayList<>();
        private List<Stroke> shapeStrokes = new ArrayList<>();
        private Shape currentShape;
        private Point startPoint;

        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                }

                public void mouseReleased(MouseEvent e) {
                    Point endPoint = e.getPoint();
                    drawShape(startPoint, endPoint);
                    startPoint = null;
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            for (int i = 0; i < shapes.size(); i++) {
                g2d.setColor(shapeColors.get(i));
                g2d.setStroke(shapeStrokes.get(i));
                g2d.draw(shapes.get(i));
            }

            if (currentShape != null) {
                g2d.setColor(currentColor);
                g2d.setStroke(currentStroke);
                g2d.draw(currentShape);
            }
        }

        private void drawShape(Point start, Point end) {
            switch (currentTool) {
                case "Line":
                    currentShape = new Line2D.Double(start, end);
                    break;
                case "Rect":
                    currentShape = new Rectangle(Math.min(start.x, end.x), Math.min(start.y, end.y),
                            Math.abs(start.x - end.x), Math.abs(start.y - end.y));
                    break;
                case "Oval":
                    currentShape = new Ellipse2D.Double(Math.min(start.x, end.x), Math.min(start.y, end.y),
                            Math.abs(start.x - end.x), Math.abs(start.y - end.y));
                    break;
            }

            shapes.add(currentShape);
            shapeColors.add(currentColor);
            shapeStrokes.add(currentStroke);
            repaint();
        }

        public void clearDrawing() {
            shapes.clear();
            shapeColors.clear();
            shapeStrokes.clear();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}