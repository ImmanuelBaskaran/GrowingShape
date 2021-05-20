import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

    Point world = new Point(0,0);

    double scalar = 1;

    public ContPanel(){
       Thread t = new Thread(this);
       t.run();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0   ,0 ,1800,1800);
        g.translate(world.x,world.y);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scalar,scalar);



//        for(Shape shape:shapes){
//                if(shape.valid){
//                    g.setColor(Color.BLACK);
//                }else{
//                    g.setColor(Color.CYAN);
//                }
//                shape.draw(g);
//
//        }


        g.setColor(Color.black);
        List<Shape> d = problems.get(problem);
        d.removeAll(solutions.get(problem));
        for(Shape unused:d){
            unused.draw(g);
        }

        g.setColor(Color.green);
        problems.get(problem).get(0).draw(g);
        g.setColor(Color.CYAN);
        for(Shape s:solutions.get(problem)){
            s.draw(g);
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

            if(key == KeyEvent.VK_A){
                world.x+=5;
                repaint();
            }
            if(key == KeyEvent.VK_D){
                world.x-=5;
                repaint();
            }
            if(key == KeyEvent.VK_S){
                world.y+=5;
                repaint();
            }
            if(key == KeyEvent.VK_W){
                world.y-=5;
                repaint();
            }
        if(key == KeyEvent.VK_Q){
                if(scalar<=0){
                    scalar=1;
                }
            scalar-=0.01;
            repaint();
        }
        if(key == KeyEvent.VK_E){
            scalar+=0.01;
            repaint();
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

                while (test / problem.get(0).findArea() < 0.30) {
                    current.removeAll(used);
                  //  current.sort();
                    for (int i = 1; i < current.size(); i++) {
                        Shape shape = current.get(i);
                        int rattemtps = 0;
                        shape.setX(problem.get(0).polygon.getBounds().x);
                        shape.setY(problem.get(0).polygon.getBounds().y);

                        shape.translate(new Point2D.Double((new Random().nextDouble() * problem.get(0).polygon.getBounds().width) / 10,
                                (new Random().nextDouble() * problem.get(0).polygon.getBounds().height) / 10));



                        shape.rotateAround(360*new Random().nextDouble());



//                        while(!shape.intersects(problem.get(0))){
//                            shape.rotateAround(1);
//                            rattemtps++;
//                            if(rattemtps>360){
//                                break;
//                            }



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
                                            used.get(j0).valid = false;
                                        }
                                    }
                                }
                                if (used.get(i0).contains(used.get(j0))) {
                                    if (used.get(i0).valid && used.get(j0).valid) {
                                        if (used.get(i0).findArea() > used.get(j0).findArea()) {
                                          used.get(j0).valid = false;
                                        } else {
                                            used.get(j0).valid = false;
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

                    if (test / problem.get(0).findArea() > 0.30) {
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
                        for (Shape shape1 : used) {
                            if (shape1.valid) {
                                shape1.translate(new Point2D.Double(+(problem.get(0).x - problem.get(0).polygon.getBounds().x) / 10, +(problem.get(0).y - problem.get(0).polygon.getBounds().y) / 10));
                                solution.add(shape1);
                            }else{
                                shape1.reset();
                            }
                        }
                        break;
                    } else {
                        List<Shape> reseter = new ArrayList<>();
                        List<Shape> solution = new ArrayList<>();
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
                    if (attemtps > 90000) {
                        List<Shape> incomplete = new ArrayList<>();
                        for (Shape shape1 : used) {
                            if (shape1.valid) {
                            //    shape1.translate(new Point2D.Double(+(problem.get(0).x - problem.get(0).polygon.getBounds().x) / 10, +(problem.get(0).y - problem.get(0).polygon.getBounds().y) / 10));
                                incomplete.add(shape1);
                            }else{
                                shape1.reset();
                            }
                        }
                        solutions.add(incomplete);
                        System.out.println(problems.indexOf(problem));
                        System.out.println(test / problem.get(0).findArea());
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


//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.geom.Point2D;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.*;
//import java.util.List;
//
//
//public class ContPanel extends JPanel implements KeyListener,Runnable{
//
//
//
//    Point2D[] ps = {new Point(0,0),new Point(10,0),new Point(0,10),new Point(10,10)};
//    List< List<Shape>> problems;
//    List<Shape> shapes;
//    List< List<Shape>> solutions;
//
//    List<Shape> used = new ArrayList<>();
//
//    int problem;
//
//    int attemtps = 0;
//
//
//    public ContPanel(){
//        Thread t = new Thread(this);
//        t.run();
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        g.setColor(Color.gray);
//        g.fillRect(0   ,0 ,1800,1800);
//        g.setColor(Color.CYAN);
//        for(Shape shape:shapes){
//            if(shape.valid){
//                g.setColor(Color.BLACK);
//            }else{
//                g.setColor(Color.CYAN);
//            }
//            shape.draw(g);
//
//        }
//    }
//
//
//    @Override
//    public void keyTyped(KeyEvent e) {
//
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//
//
//        int key = e.getKeyCode();
//        if (key == KeyEvent.VK_LEFT) {
//            problem--;
//            if(problem<0){
//                problem=0;
//            }
//            shapes = problems.get(problem);
//            repaint();
//        }
//
//        if (key == KeyEvent.VK_RIGHT) {
//            problem++;
//            if(problem>problems.size()-1){
//                problem=problems.size()-1;
//            }
//            shapes = problems.get(problem);
//            repaint();
//        }
//        if (key == KeyEvent.VK_A){
//            System.out.println(shapes.get(0).findArea());
//        }
//        if (key == KeyEvent.VK_A){
//            for(int i=0;i<shapes.size();i++){
//                for(int j=0;j<shapes.size();j++){
//                    if(shapes.get(i).intersects(shapes.get(j))){
//                        if(i!=j){
//                            System.out.println("Colision");
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//
//    }
//
//    @Override
//    public void run() {
//        problems = Loader.loadfile();
//        solutions = new ArrayList<>();
//        shapes = problems.get(0);
//        List<Shape> current = new ArrayList<>();
//        this.addKeyListener(this);
//        this.setFocusable(true);
//        this.requestFocus();
//        double test = 0;
//        try {
//            PrintWriter writert = new PrintWriter("the-filet-name.txt", "UTF-8");
//
//            for (List<Shape> problem : problems) {
//
//                current = problem;
//                List<Point2D.Double> occupiedVertices = new ArrayList<>();
//                List<Point2D.Double> borderingVertices = new ArrayList<>();
//                borderingVertices=problem.get(0).points;
//                for (Shape myShape : current)
//                {
//                    if (myShape.findArea()>current.get(0).findArea())
//                    {
//                        current.remove(myShape);
//                    }
//                }
////                Collections.sort(current, new Comparator<Shape>() { //sort by largest first
////                    @Override
////                    public int compare(Shape s1, Shape s2) {
////                        return Double.compare(s1.findArea(),s2.findArea());
////                    }
////                });
//                used.clear();
//                test = 0;
//                attemtps = 0;
//                while (test / problem.get(0).findArea() <0.3) {
//                    current.removeAll(used);
//
//
//                    for (int i = 1; i < current.size(); i++) {
//                        Shape shape = current.get(i);
//                        if (!used.contains(shape))
//                        {
//                            for (Point2D.Double roomVertex : problem.get(0).points)
//                            {
//                                if (!occupiedVertices.contains(roomVertex))
//                                {
//                                    int index=0;
//                                    while (index<shape.points.size() && !used.contains(shape))
//                                    {
//                                        int counter=-1;
//                                        while (counter<4 && !used.contains(shape))
//                                        {
//                                            counter++;
//                                            double rotationTotal = 0.5;
//                                            double xdiff= shape.points.get(0).getX() - shape.points.get(index).getX();
//                                            double ydiff= shape.points.get(0).getY() - shape.points.get(index).getY();
//                                            double xoffset;
//                                            double yoffset;
//                                            double border=0.02;
//                                            if (counter==0 || counter==1 ) xoffset=-border;
//                                            else xoffset=border;
//                                            if (counter==0 || counter==2 ) yoffset=border;
//                                            else yoffset=-border;
//
//                                            shape.setX(800);
//                                            shape.setY(800);
//                                            System.out.println("Old x is "+ shape.points.get(index).getX());
//                                            System.out.println("Old y is "+ shape.points.get(index).getY());
//                                            shape.translate(new Point2D.Double(roomVertex.getX() + xdiff +xoffset , roomVertex.getY() + ydiff  +yoffset));
//
//                                            System.out.println("translation " + counter);
//                                            System.out.println("New x is "+ shape.points.get(index).getX());
//                                            System.out.println("New y is "+ shape.points.get(index).getY());
//                                            while (!problem.get(0).contains(shape) && rotationTotal!=360)
//                                            {
//                                                shape.rotateAround(rotationTotal, shape.points.get(index));
//                                                rotationTotal+=0.5;
//
//                                            }
//                                            System.out.println("Just left rotation loop with a rotation of " + rotationTotal);
//                                            if (shape.intersects(problem.get(0))) {
//                                                //   System.out.println("Error with:" + problems.indexOf(problem));
//                                                System.out.println("Entered intersects if statement");
//                                                shape.reset();
//                                            } else if (problem.get(0).contains(shape)) {
//                                                System.out.println("YESSS shape is contained");
//
//                                                used.add(shape);
//                                                occupiedVertices.add(roomVertex);
//                                            } else {
//                                                System.out.println("Shape didnt fit so reset");
//                                                shape.reset();
//                                            }
//                                        }
//
//                                        index++;
//
//                                    }
//                                }
//                                else
//                                {
//                                    System.out.println("Room vertex occupied so skip it ");
//                                }
//
//                            }
//
//                        }
//
//
//
//                    }
//
//                    for (int i = 1; i < current.size(); i++) {
//                        Shape shape = current.get(i);
//                        shape.setX(problem.get(0).polygon.getBounds().x);
//                        shape.setY(problem.get(0).polygon.getBounds().y);
//
//                        shape.translate(new Point2D.Double((new Random().nextDouble() * problem.get(0).polygon.getBounds().width) / 10,
//                                (new Random().nextDouble() * problem.get(0).polygon.getBounds().height) / 10));
//
//
//                        shape.rotateAround(360 * new Random().nextDouble());
//
//
//
//                        if (shape.intersects(problem.get(0))) {
//                            //   System.out.println("Error with:" + problems.indexOf(problem));
//                            shape.reset();
//                        } else if (problem.get(0).contains(shape)) {
//
//                            used.add(shape);
//                        } else {
//                            shape.reset();
//                        }
//                    }
//
//
//                    checkIntersections();
//                    test = 0;
//                    for (Shape shape1 : used) {
//                        if (shape1.valid) {
//                            test += shape1.findArea();
//                        }
//                    }
//
//                    if (test / problem.get(0).findArea() > 0.02 || used.size()>3) {
//                        System.out.println("Yay:" + problems.indexOf(problem));
//                        System.out.println(test / problem.get(0).findArea());
//                        List<Shape> solution = new ArrayList<>();
//
//                        for (Shape shape1 : used) {
//                            if (shape1.valid) {
//                                shape1.translate(new Point2D.Double(-(problem.get(0).x - problem.get(0).polygon.getBounds().x) / 10, -(problem.get(0).y - problem.get(0).polygon.getBounds().y) / 10));
//                                solution.add(shape1);
//                            }
//                        }
//                        System.out.println(solution.size());
//                        solutions.add(solution);
//                        writert.print(problems.indexOf(problem) + 1);
//                        writert.print(":  ");
//                        String delim = "";
//                        for (Shape p : solution) {
//                            delim += p.toString();
//                            delim += ";";
//                        }
//                        delim = delim.replaceAll(",;", ";");
//                        delim = delim.substring(0, delim.length() - 1);
//                        writert.println(delim);
//                        break;
//                    } else {
//                        List<Shape> reseter = new ArrayList<>();
//                        for (Shape ss : used) {
//                            if (!ss.valid) {
//                                ss.valid = true;
//                                ss.reset();
//                                reseter.add(ss);
//                            }
//                        }
//                        used.removeAll(reseter);
//                    }
//                    attemtps++;
//                    if (attemtps > 70000) {
//                        System.out.println(problems.indexOf(problem));
//                        System.out.println(test / problem.get(0).findArea());
//                        break;
//                    }
//                }
//            }
//
//            PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
//            for (List<Shape> s : solutions) {
//                writer.print(solutions.indexOf(s) + 1);
//                writer.print(":  ");
//                for (Shape a : s) {
//                    writer.print(a.toString());
//                    writer.print(";");
//                }
//                writer.println();
//            }
//            writer.close();
//            writert.close();
//
//            ///////////////////////////////////////
//
//
//
//
//
//
//
//
//
//            ///////////////////////////////////
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
////        for(List<Shape> prob:problems){
////            for(Shape d:prob){
////                d.reset();
////            }
////        }
//        System.out.println("Solved");
//    }
//
//
//    private void checkIntersections()
//    {
//        int solLength = used.size();
//        for (int i0 = 0; i0 < solLength; i0++) {
//            for (int j0 = 0; j0 < solLength; j0++) {
//                if (i0 != j0) {
//                    if (used.get(i0).intersects(used.get(j0))) {
//                        if (used.get(i0).valid && used.get(j0).valid) {
//                            if (used.get(i0).findArea() > used.get(j0).findArea()) {
//                                used.get(j0).valid = false;
//                            } else {
//                                used.get(j0).valid = false;
//                            }
//                        }
//                    }
//                    if (used.get(i0).contains(used.get(j0))) {
//                        if (used.get(i0).valid && used.get(j0).valid) {
//                            if (used.get(i0).findArea() > used.get(j0).findArea()) {
//                                used.get(j0).valid = false;
//                            } else {
//                                used.get(j0).valid = false;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//}
