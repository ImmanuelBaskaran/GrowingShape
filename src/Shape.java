

import sun.security.provider.SHA;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shape {

    private Point point;

    List<Point2D> points = new ArrayList();

    public Path2D.Double polygon = new Path2D.Double();

    int x;
    int y;
    int numPoints;


    public Shape(int x, int y,  List<Point2D> points){
        point = new Point(x,y);
        this.x=x;
        this.y = y;
        this.points = (points);
        numPoints = points.size();
        polygon.moveTo(x,y);
        for(int i = 1;i<numPoints;i++){
            polygon.lineTo(x+points.get(i).getX()*10,y+points.get(i).getY()*10);
        }
        polygon.closePath();
    }

    public void draw(Graphics g){


        Graphics2D g2d = (Graphics2D) g.create();
        g2d.draw(polygon);
        g2d.dispose();
        
    }
    public double findArea(){
        double sum = 0;
        for(int i = 0;i<points.size();i+=1){

            sum+= ((points.get(i%numPoints).getX()*points.get((i+1)%numPoints).getY())-(points.get(i%numPoints).getY()*points.get((i+1)%numPoints).getX()));

        }
        return sum/2;
    }

    public boolean intersects(Shape s){

        for(Point2D p:s.points){
            if(polygon.contains(p.getX(),p.getY())){
                return true;
            }
        }
        return false;
    }
}
