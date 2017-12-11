import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.List;


public class ContPanel extends JPanel implements KeyListener{



    Point2D[] ps = {new Point(0,0),new Point(10,0),new Point(0,10),new Point(10,10)};
    List< List<Shape>> problems;
    List<Shape> shapes;

    int problem;


    public ContPanel(){
        problems = Loader.loadfile();
        shapes = problems.get(0);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();
        System.out.print("");
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0   ,0 ,1800,1800);
        g.setColor(Color.black);
        for(Shape shape:shapes){
            shape.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                problem--;
                if(problem<0){
                    problem=0;
                }
                shapes = problems.get(problem);
                repaint();
            }

            if (key == KeyEvent.VK_RIGHT) {
                problem++;
                if(problem>problems.size()-1){
                    problem=problems.size()-1;
                }
                shapes = problems.get(problem);
                repaint();
            }
            if (key == KeyEvent.VK_A){
                System.out.println(shapes.get(0).findArea());
            }
            if (key == KeyEvent.VK_A){
                for(int i=0;i<shapes.size();i++){
                    for(int j=0;j<shapes.size();j++){
                        if(shapes.get(i).intersects(shapes.get(j))){
                            if(i==j){
                                System.out.println("Colision");
                            }
                        }
                    }
                }
            }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
