import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Frame  extends JFrame {
    public final int FORM_WIDTH = 600;
    public final int FORM_HEIGHT = 500;
    public JPanel panel1;
    private JRadioButton line;
    private JRadioButton ellipse;
    private JRadioButton circle;
    private JButton button1;
    private final ArrayList<Point> linePoints;

    Frame(){
        panel1= new Panel();
        linePoints = new ArrayList<>();
        setSize(FORM_WIDTH,FORM_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setContentPane(panel1);
        panel1.add(line);
        panel1.add(ellipse);
        panel1.add(circle);
        panel1.add(button1);
        setVisible(true);
        line.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        ellipse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        circle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linePoints.clear();
                panel1.repaint();
            }
        });
    }
    class Panel extends JPanel{
        Panel() {
            setPreferredSize(new Dimension(FORM_WIDTH, FORM_HEIGHT));
            addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e) {
                    linePoints.add(new Point(e.getX(), e.getY()));
                    panel1.repaint();
                }
            });
            addMouseMotionListener(new MouseAdapter(){
                @Override
                public void mouseDragged(MouseEvent e) {
                    linePoints.add(new Point(e.getX(), e.getY()));
                    panel1.repaint();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            if (line.isSelected()){
                for (int i = 0; i < linePoints.size() - 1; i++) {
                    Point p1 = linePoints.get(i);
                    Point p2 = linePoints.get(i + 1);
                    line(g,p1.x, p1.y, p2.x, p2.y);
                }
            }
            if (ellipse.isSelected()){
                super.paintComponent(g);
                for (int i = 0; i < linePoints.size() - 1; i++) {
                    super.paintComponent(g);
                    Point p1 = linePoints.get(i);
                    Point p2 = linePoints.get(i + 1);
                    ellipse(g,p1.x, p1.y, p2.x, p2.y);
                }
            }
            if (circle.isSelected()){
                for (int i = 0; i < linePoints.size() - 1; i++) {
                    super.paintComponent(g);
                    Point p1 = linePoints.get(i);
                    circle(g,linePoints.get(1).x, linePoints.get(1).y, (int) Math.sqrt((p1.x-linePoints.get(1).x)*(p1.x-linePoints.get(1).x)+(p1.y-linePoints.get(1).y)*(p1.y-linePoints.get(1).y)));
                }
            }
        }
    }

    void line(Graphics g, int I1, int J1, int I2,int J2){
        if(I1>I2){
            int i = I1; I1 = I2; I2 = i;
            int j = J1; J1 = J2; J2 = j;
        }
        int dI=I2-I1;
        int dJ=J2-J1;
        int S=0;
        if (dJ!=0){
            S=dJ/Math.abs(dJ);
        }
        int d=2*dJ-S*dI;
        if (S*dJ<=dI){
            int j=J1;
            for(int i=I1; i<=I2; i++){
                g.fillRect(i,j,2,2);
                d+=2*dJ;
                if (S*d>=0){
                    j+=S;
                    d-=2*S*dI;
                }
            }
        }
        else{
            int i =I1;
            for(int j= J1; (J2-j)*S>=0; j+=S){
                g.fillRect(i,j,2,2);
                d+=2*S*dI;
                if (S*d>=0){
                    d-=2*dJ;
                    i++;
                }
            }
        }

    }
    void ellipse(Graphics g, int X1, int Y1, int rx, int ry)
    {
        int x = 0;
        int y = ry;
        int rx2 = rx * rx;
        int ry2 = ry * ry;
        int delta = rx2 + ry2 - 2 * rx2 * ry;
        int error = 0;
        while (y >= 0)
        {
            // дуга 1-й четверти
            g.fillRect( X1 + x, Y1 - y, 2, 2);
            if (delta < 0)
            {
                error = 2 * delta + rx2 * (2 * y - 1);
                if (error <= 0)
                    delta += ry2 * (2 * ++x + 1);   // выбираем горизонтальный пиксель
                else
                    delta += ry2 * (2 * ++x + 1) - rx2 * (2 * --y - 1);    // выбираем диагональный пиксель
            }
            else
            {
                error = 2 * delta - ry2 * (2 * x + 1);
                if (error > 0)
                    delta -= rx2 * (2 * --y + 1);   // выбираем вертикальный пиксель
                else
                    delta += ry2 * (2 * ++x + 1) - rx2 * (2 * --y - 1);   // выбираем диагональный пиксель
            }
        }
    }

    void circle(Graphics g, int X1, int Y1, int r)
    {
        int x = 0;
        int y = r;
        int delta = 2 - 2 * r;
        int error;
        while (y >= 0)
        {
            // дуга 1-й четверти
            g.fillRect(X1 + x, Y1 - y, 2, 2);
            g.fillRect(X1 - x, Y1 - y, 2, 2);
            g.fillRect(X1 - x, Y1 + y, 2, 2);
            g.fillRect(X1 + x, Y1 + y, 2, 2);
            if (delta < 0)
            {
                error = 2 * (delta + y) - 1;
                if (error <= 0)
                    delta += 2 * ++x + 1;   // выбираем горизонтальный пиксель
                else
                    delta += 2 * (++x - --y + 1);   // выбираем диагональный пиксель
            }
            else
            {
                error = 2 * (delta - x) - 1;
                if (error > 0)
                    delta -= 2 * --y + 1;   // выбираем вертикальный пиксель
                else
                    delta += 2 * (++x - --y + 1);   // выбираем диагональный пиксель
            }
        }
    }
    public static void main(String[] args){
        new Frame();
    }

}
