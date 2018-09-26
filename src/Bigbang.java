import javax.swing.*;
import java.awt.*;

public class Bigbang {



    public static void createUI(){
        JFrame frame = new JFrame("Diderot Maps");
        JPanel panel = new JPanel();
        frame.setContentPane(panel);
        panel.setLayout(new BorderLayout());
        Point pointOfView = new Point(-1060, -1120,-660);

        CustomPanel cp = new CustomPanel(1280, 720, pointOfView);
        panel.add(cp, BorderLayout.CENTER);
        frame.addMouseListener(cp);
        frame.addKeyListener(cp);
        frame.pack();
        frame.setSize(1280,720);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setFocusable(true);
        panel.setFocusable(false);
        cp.setFocusable(false);
    }
    public static void main(String[] args){
        createUI();
    }



}
