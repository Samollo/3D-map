import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Triangle extends Polygon{

    Color color = Color.WHITE;
    ArrayList<Integer> zPoints;
    private Point barycentre;
    double bary=0;


    public Triangle(){
        super();
        zPoints = new ArrayList<Integer>();
    }

    public Triangle(Point a, Point b, Point c){
        super();
        zPoints = new ArrayList<Integer>();
        this.addPoint((int) a.getX(), (int) a.getY(), (int) a.getZ());
        this.addPoint((int) b.getX(), (int) b.getY(), (int) b.getZ());
        this.addPoint((int) c.getX(), (int) c.getY(), (int) c.getZ());
        barycentre = new Point(
                a.getX()+b.getX()+c.getX(),
                a.getY()+b.getY()+c.getY(),
                a.getZ()+b.getZ()+c.getZ()
        );
        this.bary=(a.getZ()+b.getZ()+c.getZ())/3;
        calColor();
    }

    public void addPoint(int x, int y, int z){
        addPoint(x, y);
        zPoints.add(z);
    }

    public int moyenne(){
        int res = 0;
        for(int i = 0; i < zPoints.size(); i++){
            res += zPoints.get(i);
        }
        return res / zPoints.size();
    }

    public void calColor(){
        //int m = moyenne();
        int m =(int) this.bary;
      //  System.out.println("M vaut : " + m);
        if(m < 0) color = new Color(113,93,62);
        else if(m >= 0 && m <= 18) color = new Color(4,145,44);
        else if(m > 18)  color = new Color(9,134,196);
        //else if(m > 19) color = Color.WHITE;
    }
    public void setColor(Color c){
        color = c;
    }

    public Color getColor(){
        return color;
    }

    public Point getBarycentre() {
        return barycentre;
    }

    public void setBarycentre(Point barycentre) {
        this.barycentre = barycentre;
    }
}
