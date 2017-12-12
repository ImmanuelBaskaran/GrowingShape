

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

    int scale = 10;

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

        polygon.moveTo(x+points.get(0).getX()* scale,y+points.get(0).getY()* scale);
        for(int i = 1;i<numPoints;i++){
            polygon.lineTo(x+points.get(i).getX()* scale,y+points.get(i).getY()* scale);
            lines.add(new Line2D.Double(new Point2D.Double(x+points.get(i-1).getX()* scale,y+points.get(i-1).getY()* scale),
                    new Point2D.Double(x+points.get(i).getX()* scale,y+points.get(i).getY()* scale)));
        }
        lines.add(new Line2D.Double(new Point2D.Double(x+points.get(numPoints-1).getX()* scale,y+points.get(numPoints-1).getY()* scale),
                  new Point2D.Double(x+points.get(0).getX()* scale,y+points.get(0).getY()* scale)));
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

    public boolean contains(Shape shape){
        for(Point2D point:shape.points){
            if(!polygon.contains(shape.x+point.getX()*scale,shape.y+point.getY()*scale)){
                return false;
            }
        }
        return true;
    }

    public void translate(Point2D delta){

        for(Point2D point:points){
            point.setLocation(point.getX()+delta.getX(),point.getY()+delta.getY());
        }

        recalculatePolygon();
    }

    public boolean intersects(Shape s){

        if(s.contains(this)){
            return false;
        }
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
