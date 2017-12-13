import sun.security.provider.SHA;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Loader {


    public static  List< List<Shape>> loadfile(){
        List< List<Shape>> problems = new ArrayList<>();

        int x = 0,y = 0,i2 = 0;
        try {
            Scanner scanner = new Scanner(new File("problems.rfp"));
            while (scanner.hasNextLine()) {
                x=0;
                y=0;
                i2=0;
                List<Shape> shapes = new ArrayList<>();
                String line = scanner.nextLine();
                for(String shape:line.split("#")) {
                    for(int v = 1;v<shape.split(":").length;v+=2) {
                        String shape2 = shape.split("\\d*:")[v];
                        shape2 = shape2.replace("(", "");
                        shape2 = shape2.replace(")", "");
                        shape2 = shape2.replace(" ", "");
                        shape2 = shape2.replace(";", "");
                        List<Point2D.Double> points = new ArrayList<>();
                        String[] tpoints = shape2.split(",");
                        for (int i = 0; i < tpoints.length; i += 2) {
                            Point2D.Double p = new Point2D.Double(Double.parseDouble(tpoints[i]),Double.parseDouble(tpoints[i + 1]));
                            points.add(p);
                        }
                        if(i2==0){
                            shapes.add(new Shape(800, 800, points));
                        }else {
                            shapes.add(new Shape(100 + x * 50, 50 + y * 160, points));
                        }
                        x++;
                        i2++;
                        if(100+x*40>1000){
                            y++;
                            x=0;
                        }

                    }
                    y++;
                }
                problems.add(shapes);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return problems;
    }
    }


