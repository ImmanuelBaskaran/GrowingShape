

import sun.security.provider.SHA;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shape {

    public Point point;

    List<Point2D.Double> points = new ArrayList();

    List<Point2D.Double> origin = new ArrayList();
    List<Line2D> lines = new ArrayList<>();

    public Path2D.Double polygon;

    Area test;

    int scale = 10;

    double x;
    double y;
    int numPoints;

    public boolean valid = true;


    public Shape(int x, int y,  List<Point2D.Double> points){
        point = new Point(x,y);
        this.x = x;
        this.y = y;
        this.points = (points);
        for(Point2D p : points) {
            origin.add((Point2D.Double) p.clone());
        }
        numPoints = points.size();
        recalculatePolygon();
        test = new Area(polygon);
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
        this.x = x;
        recalculatePolygon();
    }
    public void setY(double y){
        this.y = y;
        recalculatePolygon();
    }
    public void reset(){
        this.x = point.getX();
        this.y = point.getY();
        points.clear();
        for(Point2D p:origin){
            points.add((Point2D.Double) p.clone());
        }
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

        for(Line2D line:lines){
            for(Line2D line1:s.lines){
                if(line.intersectsLine(line1)){
                    return true;
                }
            }
        }
        return false;

    }

    public void rotateAround(double angle) {
        Point2D[] pt = new Point2D[points.size()];
        points.toArray(pt);
        Point2D center = calculateCentroid();
        AffineTransform.getRotateInstance(Math.toRadians(angle), center.getX(), center.getY())
                .transform(pt, 0, pt, 0, points.size()); // specifying to use this double[] to hold coords
        recalculatePolygon();
    }


    @Override
    public String toString() {
        String delim = "";
        for(Point2D p:points){
            delim+="(";
            delim+=p.getX();
            delim+=",";
            delim+=p.getY();
            delim+="),";
        }
        return delim;
    }

    public Point2D.Double calculateCentroid() {
        double x = 0.;
        double y = 0.;
        int pointCount = points.size();
        for (int i = 0;i < pointCount - 1;i++){
            final Point2D.Double point = points.get(i);
            x += point.getX();
            y += point.getY();
        }

        x = x/pointCount;
        y = y/pointCount;

        return new Point2D.Double(x, y);
    }


}
