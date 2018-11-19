import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class VoronoiGraph extends JFrame implements MouseListener {
    
    JPanel graphPane, controlPane;
    JButton pickColor, randomColor;
    JComboBox comboNorm;
    final static String[] NORMS = {"Euclidean", "Manhattan"};
    
    Color color = Color.BLUE;
    
    BufferedImage image;
    
    List<Point> nodes;
    List<Color> areas;
    
    public VoronoiGraph() {
        super("Graph");
        setSize(500, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        graphPane = new JPanel() {
            public void paint(Graphics g) {
                g.drawImage(image, 0, 0, this);
            }};
        graphPane.addMouseListener(this);
        
        pickColor = new JButton("Pick Color");
        pickColor.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Color input = JColorChooser.showDialog(graphPane, "Choose your color", color);
                    if (input != null) {
                        color = input;
                    }
                    controlPane.setBackground(color);
                }});
            
        randomColor = new JButton("Random Color");
        randomColor.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Random rd = new Random();
                    color = new Color(rd.nextFloat(), rd.nextFloat(), rd.nextFloat());
                    controlPane.setBackground(color);
                }});
                
        comboNorm = new JComboBox<String>(NORMS);
        comboNorm.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    updateImage();
                }});
        
        controlPane = new JPanel();
        controlPane.add(pickColor, BorderLayout.WEST);
        controlPane.add(randomColor, BorderLayout.CENTER);
        controlPane.add(comboNorm, BorderLayout.EAST);
        controlPane.setBackground(color);
        controlPane.setOpaque(true);
        
        add(graphPane, BorderLayout.CENTER);
        add(controlPane, BorderLayout.SOUTH);
        
        nodes = new ArrayList<Point>();
        areas = new ArrayList<Color>();        
        
        setVisible(true);
        
        image = new BufferedImage(graphPane.getWidth(), graphPane.getHeight(), BufferedImage.TYPE_INT_RGB);
    }
    
    public double norm(int x0, int y0, int x1, int y1, int type) {
        double norm = 0;
        switch (type) {
            case 0:
                norm = (x1-x0)*(x1-x0) + (y1-y0)*(y1-y0);
                break;
            case 1:
                norm = Math.abs(x1-x0) + Math.abs(y1-y0);
                break;
        }
        return norm;
    }
    
    public void updateImage() {
        int normType = comboNorm.getSelectedIndex();
        Color pixel;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixel = Color.BLACK;
                double closest = Double.MAX_VALUE;
                for (int i = 0; i < nodes.size(); i++) {
                    Point p = nodes.get(i);
                    if (closest > norm(x, y, p.x, p.y, normType)) {
                        closest = norm(x, y, p.x, p.y, normType);
                        pixel = areas.get(i);
                    }
                }
                image.setRGB(x, y, pixel.getRGB());
            }
        }
        
        Graphics g = image.getGraphics();
        for (int i = 0; i < nodes.size(); i++) {
            Point p = nodes.get(i);
            g.setColor(areas.get(i).darker());
            g.fillOval(p.x-5, p.y-5, 10, 10);
        }
        
        graphPane.repaint();
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        nodes.add(me.getPoint());
        areas.add(color);
        updateImage();
    }
    
    @Override
    public void mouseEntered(MouseEvent me) {}
    
    @Override
    public void mouseExited(MouseEvent me) {}
    
    @Override
    public void mousePressed(MouseEvent me) {}
    
    @Override
    public void mouseReleased(MouseEvent me) {}
 
    public static void main(String[] args) {
        new VoronoiGraph();
    }        
    
}
