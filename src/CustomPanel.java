import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;

public class CustomPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage buffImg;
    private Graphics graph;
    private Dimension dimension;
    private Matrice base, terrain;
    private ArrayList<Triangle> listTriangle;
    private double angle;
    private int flag;
    private Thread t;
    private Point[][] testTerrain, testBase;
    DepthVision dv;

    private int DijkstraFlag = 0;
    private Point depart;
    private Point arrive;
    Dijkstra dij;

    public CustomPanel(int width, int height, Point p) {
        dimension = new Dimension(width, height);
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graph = buffImg.getGraphics();
        base = new Matrice(129, 21, 0, graph, p);
        terrain = new Matrice(129, 21, 1, graph, p);
        base.init();
        terrain.init();
        terrain.appPerlin();
        //base.diamantCarre();
        //terrain.synchroniser(base.getSommets());
        angle = 0;
        flag = -1;
        dv = new DepthVision(base, terrain);
        testBase = applyRotation(base, angle);
        base.projection(testBase); // new coords
        testTerrain = applyRotation(terrain, angle);
        terrain.projection(testTerrain); // new coords
        dij = new Dijkstra(terrain, graph);

        update(flag);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(buffImg, 0, 0, null);
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public void setBuffImg(BufferedImage buffImg) {
        this.buffImg = buffImg;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        flag += 1;
        switch (key) {
            case KeyEvent.VK_Q:
                update(0);
                break;
            case KeyEvent.VK_D:
                update(1);
                break;
            case KeyEvent.VK_Z:
                move(new Point(terrain.getPointOfView().getX(),terrain.getPointOfView().getY(),terrain.getPointOfView().getZ()+20));
                break;

            case KeyEvent.VK_S:
                move(new Point(terrain.getPointOfView().getX(),terrain.getPointOfView().getY(),terrain.getPointOfView().getZ()-20));
                break;

            case KeyEvent.VK_UP:
                move(new Point(terrain.getPointOfView().getX()+20,terrain.getPointOfView().getY(),terrain.getPointOfView().getZ()));
                break;
            case KeyEvent.VK_DOWN:
                move(new Point(terrain.getPointOfView().getX()-20,terrain.getPointOfView().getY(),terrain.getPointOfView().getZ()));
                break;
            case KeyEvent.VK_LEFT:
                move(new Point(terrain.getPointOfView().getX(),terrain.getPointOfView().getY()+20,terrain.getPointOfView().getZ()));
                break;
            case KeyEvent.VK_RIGHT:
                move(new Point(terrain.getPointOfView().getX(),terrain.getPointOfView().getY()-20,terrain.getPointOfView().getZ()));
                break;
        }
    }

    public Point[][] applyRotation(Matrice terrain, double angle){
        Point[][] tmp = new Point[terrain.getSommets().length][terrain.getSommets().length];
        Rotation firstR = new Rotation(new Point(terrain.getTaille()/2, terrain.getTaille()/2, 0), angle);

        for (int i = 0; i < terrain.getSommets().length; i++) {
            for (int j = 0; j < terrain.getSommets().length; j++) {
                double x = terrain.getSommets()[i][j].getX();
                double y = terrain.getSommets()[i][j].getY();

                Vecteur vert = firstR.transform(x, y);
                tmp[i][j] = new Point(vert.getX(), vert.getY(), terrain.getSommets()[i][j].getZ());
            }
        }

        return tmp;
    }

    public void update(int flag){
        graph.clearRect(0,0,1280,720);
       /* base.diamantCarre();
        terrain.synchroniser(base.getSommets());*/
        if(flag == 0) angle += Math.PI/36;
        if(flag == 1) angle -= Math.PI/36;
        testBase = applyRotation(base, angle);
        base.projection(testBase); // new coords

        testTerrain = applyRotation(terrain, angle);
        terrain.projection(testTerrain); // new coords
        dv.init();
        base.dessin();
        bordure(base, terrain);
        listTriangle = terrain.dessin();

        dij.setMatrice(terrain.getsommetsProjete());
        dij.afficher();

        repaint();
    }

    public void move(Point newPOV){
        graph.clearRect(0,0,1280,720);

        this.base.setPointOfView(newPOV);
        this.terrain.setPointOfView(newPOV);

        base.projection(testBase); // new coords
        terrain.projection(testTerrain);
        dv.init();
        base.dessin();
        bordure(base,terrain);
        listTriangle = terrain.dessin();

        dij.setMatrice(terrain.getsommetsProjete());
        dij.afficher();

        repaint();
    }

    public static void bordure(Matrice a, Matrice b){
        Polygon face = new Polygon();
        Polygon face1 = new Polygon();
        Polygon face2 = new Polygon();
        Polygon face3 = new Polygon();
        int fin = a.getsommetsProjete().length-2;


        for(int i = 0; i < b.getsommetsProjete().length; i++){
            face.addPoint( (int) b.getsommetsProjete()[0][i].getX(), (int) b.getsommetsProjete()[0][i].getY());
            face1.addPoint( (int) b.getsommetsProjete()[i][0].getX(), (int) b.getsommetsProjete()[i][0].getY());

            face2.addPoint( (int) b.getsommetsProjete()[i][fin].getX(), (int) b.getsommetsProjete()[i][fin].getY());
            face3.addPoint( (int) b.getsommetsProjete()[fin][i].getX(), (int) b.getsommetsProjete()[fin][i].getY());

        }

        face.addPoint( (int) a.getsommetsProjete()[0][fin].getX(), (int) a.getsommetsProjete()[0][fin].getY());
        face.addPoint( (int) a.getsommetsProjete()[0][0].getX(), (int) a.getsommetsProjete()[0][0].getY());

        face1.addPoint( (int) a.getsommetsProjete()[fin][0].getX(), (int) a.getsommetsProjete()[fin][0].getY());
        face1.addPoint( (int) a.getsommetsProjete()[0][0].getX(), (int) a.getsommetsProjete()[0][0].getY());

        face2.addPoint( (int) a.getsommetsProjete()[fin][fin].getX(), (int) a.getsommetsProjete()[fin][fin].getY());
        face2.addPoint( (int) a.getsommetsProjete()[0][fin].getX(), (int) a.getsommetsProjete()[0][fin].getY());

        face3.addPoint( (int) a.getsommetsProjete()[fin][fin].getX(), (int) a.getsommetsProjete()[fin][fin].getY());
        face3.addPoint( (int) a.getsommetsProjete()[fin][0].getX(), (int) a.getsommetsProjete()[fin][0].getY());


        a.getGraph().setColor(Color.RED);
        a.getGraph().setColor(new Color(0,51,102));
        a.getGraph().fillPolygon(face);
        a.getGraph().fillPolygon(face1);
        a.getGraph().fillPolygon(face2);
        a.getGraph().fillPolygon(face3);
        a.getGraph().drawPolygon(face);
        a.getGraph().drawPolygon(face1);
        a.getGraph().drawPolygon(face2);
        a.getGraph().drawPolygon(face3);

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void soleil(){
        Point pSoleil = new Point(terrain.getTaille()/2, terrain.getTaille()/2, 900);
        Point[][] tmp = new Point[1][1];
        tmp[0][0] = pSoleil;
        terrain.projection(tmp);
        pSoleil = tmp[0][0];
    }

    public ArrayList<Double> rayTracing(Point pSoleil){
        ArrayList<Double> listDist = new ArrayList<Double>();
        for(int i = 0; i < listTriangle.size(); i++){
            // TROUVE LE BARYCENTRE DU POLYGON LISTTRIANGE.GET(i) ET FAIRE LA NORME
             listDist.add(terrain.norme(pSoleil, listTriangle.get(i).getBarycentre()));
        }
        return listDist;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        base.dessin();
        bordure(base,terrain);

        terrain.dessin();

        if (DijkstraFlag == 0) {
            depart = new Point(e.getX(), e.getY(), 0);
        }
        if (DijkstraFlag == 1) {
            arrive = new Point(e.getX(), e.getY(), 0);
            DijkstraFlag++;
        }
        if (DijkstraFlag == 2) {
            dij.calcul(depart, arrive);
            dij.afficher();
            repaint();
            DijkstraFlag = -1;
        }
        DijkstraFlag++;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}