import javax.swing.*;


public class Frame {


    public static void main(String args[]){
        JFrame frame = new JFrame();
        frame.setSize(1600,1600);
        frame.setResizable(false);
        ContPanel p = new ContPanel();
        frame.add(p);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        System.out.print("");
    }

}
