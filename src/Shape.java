

import sun.security.provider.SHA;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shape {

    private Point point;

    private List<Point2D> points = new ArrayList();

    int x;
    int y;
    int numPoints;


    public Shape(int x, int y,  List<Point2D> points){
        point = new Point(x,y);
        this.x=x;
        this.y = y;
        this.points = (points);
        numPoints = points.size();
    }

    public void draw(Graphics g){

        for(int i = 0;i<points.size();i+=1){
            g.drawLine((int)(x+points.get(i%numPoints).getX()*10),(int)(y+points.get(i%numPoints).getY()*10),
                    (int)(x+points.get((i+1)%numPoints).getX()*10),(int)(y+points.get((i+1)%numPoints).getY()*10));
        }

//        for(Point2D point:points){
//           g.fillRect((int) point.getX()*10+((int)point.getX()),(int)point.getY()*10+((int)point.getY()),10,10);
//        }
    }

}
