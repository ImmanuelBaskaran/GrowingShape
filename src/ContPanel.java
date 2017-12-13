import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ContPanel extends JPanel implements KeyListener,Runnable{



    Point2D[] ps = {new Point(0,0),new Point(10,0),new Point(0,10),new Point(10,10)};
    List< List<Shape>> problems;
    List<Shape> shapes;
    List< List<Shape>> solutions;

    List<Shape> used = new ArrayList<>();

    int problem;

    int attemtps = 0;


    public ContPanel(){
       Thread t = new Thread(this);
       t.run();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0   ,0 ,1800,1800);
        g.setColor(Color.CYAN);
        for(Shape shape:shapes){
                if(shape.valid){
                    g.setColor(Color.BLACK);
                }else{
                    g.setColor(Color.CYAN);
                }
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
                            if(i!=j){
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

    @Override
    public void run() {
        problems = Loader.loadfile();
        solutions = new ArrayList<>();
        shapes = problems.get(0);
        List<Shape> current = new ArrayList<>();
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();
        double test = 0;
        try {
            PrintWriter writert = new PrintWriter("the-filet-name.txt", "UTF-8");
            for (List<Shape> problem : problems) {
                current = problem;
                used.clear();
                test = 0;
                attemtps = 0;
                while (test / problem.get(0).findArea() < 0.3) {
                    current.removeAll(used);
                    for (int i = 1; i < current.size(); i++) {
                        Shape shape = current.get(i);
                        shape.setX(problem.get(0).polygon.getBounds().x);
                        shape.setY(problem.get(0).polygon.getBounds().y);

                        shape.translate(new Point2D.Double((new Random().nextDouble() * problem.get(0).polygon.getBounds().width) / 10,
                                (new Random().nextDouble() * problem.get(0).polygon.getBounds().height) / 10));


                        shape.rotateAround(180 * new Random().nextDouble());


                        if (shape.intersects(problem.get(0))) {
                            //   System.out.println("Error with:" + problems.indexOf(problem));
                            shape.reset();
                        } else if (problem.get(0).contains(shape)) {
                            used.add(shape);
                        } else {
                            shape.reset();
                        }
                    }
                    int solLength = used.size();
                    for (int i0 = 0; i0 < solLength; i0++) {
                        for (int j0 = 0; j0 < solLength; j0++) {
                            if (i0 != j0) {
                                if (used.get(i0).intersects(used.get(j0))) {
                                    if (used.get(i0).valid && used.get(j0).valid) {
                                        if (used.get(i0).findArea() > used.get(j0).findArea()) {
                                            used.get(j0).valid = false;
                                        } else {
                                            used.get(i0).valid = false;
                                        }
                                    }
                                }
                                if (used.get(i0).contains(used.get(j0))) {
                                    if (used.get(i0).valid && used.get(j0).valid) {
                                        if (used.get(i0).findArea() > used.get(j0).findArea()) {
                                            used.get(j0).valid = false;
                                        } else {
                                            used.get(i0).valid = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    test = 0;
                    for (Shape shape1 : used) {
                        if (shape1.valid) {
                            test += shape1.findArea();
                        }
                    }

                    if (test / problem.get(0).findArea() > 0.3) {
                        System.out.println("Yay:" + problems.indexOf(problem));
                        List<Shape> solution = new ArrayList<>();

                        for (Shape shape1 : used) {
                            if (shape1.valid) {
                                shape1.translate(new Point2D.Double(-(problem.get(0).x - problem.get(0).polygon.getBounds().x) / 10, -(problem.get(0).y - problem.get(0).polygon.getBounds().y) / 10));
                                solution.add(shape1);
                            }
                        }
                        System.out.println(solution.size());
                        solutions.add(solution);
                        writert.print(problems.indexOf(problem) + 1);
                        writert.print(":  ");
                        String delim = "";
                        for (Shape p : solution) {
                            delim += p.toString();
                            delim += ";";
                        }
                        delim = delim.replaceAll(",;", ";");
                        delim = delim.substring(0, delim.length() - 1);
                        writert.println(delim);
                        break;
                    } else {
                        List<Shape> reseter = new ArrayList<>();
                        for (Shape ss : used) {
                            if (!ss.valid) {
                                ss.valid = true;
                                ss.reset();
                                reseter.add(ss);
                            }
                        }
                        used.removeAll(reseter);
                    }
                    attemtps++;
                    if (attemtps > 70000) {
                        break;
                    }
                }
            }

            PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
            for (List<Shape> s : solutions) {
                writer.print(solutions.indexOf(s) + 1);
                writer.print(":  ");
                for (Shape a : s) {
                    writer.print(a.toString());
                    writer.print(";");
                }
                writer.println();
            }
            writer.close();
            writert.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        for(List<Shape> prob:problems){
//            for(Shape d:prob){
//                d.reset();
//            }
//        }
        System.out.println("Solved");
    }

}
