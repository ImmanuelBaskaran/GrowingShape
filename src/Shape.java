

import sun.security.provider.SHA;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shape {

    private Point point;

    List<Point2D> points = new ArrayList();


    List<Line2D> lines = new ArrayList<>();

    public Path2D.Double polygon;

    double x;
    double y;
    int numPoints;


    public Shape(int x, int y,  List<Point2D> points){
        point = new Point(x,y);
        this.x = x;
        this.y = y;
        this.points = (points);
        numPoints = points.size();
        recalculatePolygon();
    }
    private void recalculatePolygon(){
        polygon = new Path2D.Double();
        lines.clear();
        polygon.moveTo(x,y);
        for(int i = 1;i<numPoints;i++){
            polygon.lineTo(x+points.get(i).getX()*10,y+points.get(i).getY()*10);
            lines.add(new Line2D.Double(new Point2D.Double(x+points.get(i-1).getX()*10,y+points.get(i-1).getY()*10),
                    new Point2D.Double(x+points.get(i).getX()*10,y+points.get(i).getY()*10)));
        }
        lines.add(new Line2D.Double(new Point2D.Double(x+points.get(numPoints-1).getX()*10,y+points.get(numPoints-1).getY()*10),
                  new Point2D.Double(x+points.get(0).getX()*10,y+points.get(0).getY()*10)));
        polygon.closePath();
    }
    public void setX(double x){
        this.x = (int)x;
        recalculatePolygon();
    }
    public void setY(double y){
        this.y = (int)y;
        recalculatePolygon();
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





        for(Line2D line:lines){
            for(Line2D line1:s.lines){
                if(line.intersectsLine(line1)){
                    return true;
                }
            }
        }
        return false;

    }
}
