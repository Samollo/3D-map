import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class Matrice extends JPanel {


    private Graphics graph;

    private int nbCases, pas;
    private int size;
    private Point[][] sommets;
    private boolean mg;
    private Point[][] sommetsProjete;
    private Point pointOfView;

    public Matrice(int nbCases, int pas, int decalage, Graphics g, Point p) {
        this.nbCases = nbCases - decalage;
        this.pas = pas;
        this.graph = g;
        this.pointOfView = p;
        if(decalage != 0) mg = true;
        else mg = false;
        sommets = new Point[nbCases][nbCases];
        size = nbCases*pas;
    }

    public void init() { /* Decalage vaut 0 pour matrice de base, #pas si matrice d'elevation */
        for (int i = pas/2; i < size; i += pas) {
            for (int j = pas/2; j < size; j += pas) {
                sommets[i/pas][j/pas] = new Point((double) i-10, (double) j-10, 0.0);
                /* Idee : Au lieu de dessiner sur (i,j) je vais accéder un point contenu dans la position (i,j) de ma matrice et ensuite je dessine sur les coordonées de ce point */
            }
        }
    }


    public double norme(Point a, Point b){
       return Math.sqrt(Math.pow(b.getX()-a.getX(), 2) + Math.pow(b.getY()-a.getY(), 2) + Math.pow(b.getZ()-a.getZ(), 2));

    }

    public ArrayList<Triangle> dessin(){
        ArrayList<Triangle> listTriangle = new ArrayList<Triangle>();
        for (int i = 0; i < sommetsProjete.length-1; i += 1) {
            for (int j = 0; j < sommetsProjete[0].length-1; j += 1) {
                Point e = new Point(sommetsProjete[i][j].getX(), sommetsProjete[i][j].getY(), sommets[i][j].getZ());
                Point f = new Point(sommetsProjete[i + 1][j].getX(), sommetsProjete[i + 1][j].getY(), sommets[i + 1][j].getZ());
                Point g = new Point(sommetsProjete[i][j + 1].getX(), sommetsProjete[i][j + 1].getY(), sommets[i][j + 1].getZ());

                Triangle triangle = new Triangle(e, f, g);

                Point a = new Point(sommetsProjete[i + 1][j + 1].getX(), sommetsProjete[i + 1][j + 1].getY(), sommets[i + 1][j + 1].getZ());
                Point b = new Point(sommetsProjete[i + 1][j].getX(), sommetsProjete[i + 1][j].getY(), sommets[i + 1][j].getZ());
                Point c = new Point(sommetsProjete[i][j + 1].getX(), sommetsProjete[i][j + 1].getY(), sommets[i][j + 1].getZ());

                Triangle triangleBis = new Triangle(a, b, c);
                listTriangle.add(triangle);
                listTriangle.add(triangleBis);


                if (mg == true) {
                    graph.setColor(triangle.getColor());
                    graph.fillPolygon(triangle);
                    graph.setColor(triangleBis.getColor());
                    graph.fillPolygon(triangleBis);
                    graph.setColor(Color.black);
                    if(triangle.getColor() != Color.BLUE) graph.drawPolygon(triangle);
                    if(triangleBis.getColor() != Color.BLUE) graph.drawPolygon(triangleBis);

                } else{
                    graph.setColor(Color.darkGray);
                    graph.setColor(Color.darkGray);
                    graph.drawPolygon(triangle);
                    graph.drawPolygon(triangleBis);
                }
            }
        }
        return listTriangle;
    }

    public double produitScalaire(Vecteur u, Vecteur v){
        return u.getX()*v.getX() + u.getY()*v.getY() + u.getZ()*v.getZ();
    }

    // rempli sommetsProjete[][]
     public void projection(Point[][] sommets){
        sommetsProjete = new Point[sommets.length][sommets.length];
        Vecteur vNormal = new Vecteur(1,0, 0);
        Vecteur u = new Vecteur(0,1,0);
        Vecteur v = new Vecteur(0,0,1);

         double normeNormale = Math.pow(vNormal.getX(), 2) + Math.pow(vNormal.getY(), 2) + Math.pow(vNormal.getZ(), 2);
        double d = Math.abs(pointOfView.getZ());
        for(int i = 0; i < sommets.length; i++){
            for(int j = 0; j < sommets[0].length; j++){
                Vecteur vAM = new Vecteur(sommets[i][j].getX()-pointOfView.getX(), sommets[i][j].getY()-pointOfView.getY(), sommets[i][j].getZ()-pointOfView.getZ());
                double xOM = -d*vNormal.getX() + ( (d*normeNormale) / produitScalaire(vAM, vNormal) ) * vAM.getX();
                double yOM = -d*vNormal.getY() + ( (d*normeNormale) / produitScalaire(vAM, vNormal) ) * vAM.getY();
                double zOM = -d*vNormal.getZ() + ( (d*normeNormale) / produitScalaire(vAM, vNormal) ) * vAM.getZ();
                Vecteur OpMp = new Vecteur(xOM, yOM, zOM);
                double x = produitScalaire(u, OpMp);
                double y = produitScalaire(v, OpMp);
                sommetsProjete[i][j] = new Point(x, y, sommets[i][j].getZ());
            }
        }
     }

     public Point centralized(Point toCenter){
        return new Point(toCenter.getX()+this.getTaille()/2, toCenter.getY()+this.getTaille()/2, toCenter.getZ());
     }

    public void synchroniser(Point[][] tab){
        for(int i = 0; i < sommets.length-1; i++){
            for(int j = 0; j < sommets[0].length-1; j++){
                sommets[i][j].setZ(
                          (tab[i][j].getZ() + tab[i][j+1].getZ() + tab[i+1][j].getZ() + tab[i+1][j+1].getZ()) / 4 );
                tab[i][j].setZ(250);
            }
        }

    }

    /* algorithme creation elevation du terrain */
    public double[][] diamantCarre() {
        double[][] mdc = new double[nbCases][nbCases];
        int height = nbCases;
        int borne = 40;
        /* Init des coins */
        mdc[0][0] = ThreadLocalRandom.current().nextDouble(-borne, borne);
        mdc[0][height - 1] = ThreadLocalRandom.current().nextDouble(-borne, borne);
        mdc[height - 1][0] = ThreadLocalRandom.current().nextDouble(-borne, borne);
        mdc[height - 1][height - 1] = ThreadLocalRandom.current().nextDouble(-borne, borne);

        int i = height - 1;
        int id;
        int decalage;
        while (i > 1) {
            id = i / 2;
            for (int indiceX = id; indiceX < height; indiceX += i) { /* début de la phase du diamant */
                for (int indiceY = id; indiceY < height; indiceY += i) {
                    double moyenne = (mdc[indiceX - id][indiceY - id]
                            + mdc[indiceX - id][indiceY + id]
                            + mdc[indiceX + id][indiceY + id]
                            + mdc[indiceX + id][indiceY - id]) / 4;
                    mdc[indiceX][indiceY] = moyenne + (-id) + (Math.random() * (id + id));

                    mdc[indiceX][indiceY] = moyenne + ThreadLocalRandom.current().nextDouble(-height, height);
                    sommets[indiceX][indiceY].setZ(mdc[indiceX][indiceY]);
                }
            }
            for (int indiceX = 0; indiceX < height; indiceX += id) { /* début de la phase du carré */
                if (indiceX % i == 0) {
                    decalage = id;
                } else {
                    decalage = 0;
                }
                for (int indiceY = decalage; indiceY < height; indiceY += i) {
                    int somme = 0;
                    int n = 0;
                    if (indiceX >= id) {
                        somme += mdc[indiceX - id][indiceY];
                        n += 1;
                    }
                    if (indiceX + id < height) {
                        somme += mdc[indiceX + id][indiceY];
                        n += 1;
                    }
                    if (indiceY >= id) {
                        somme += mdc[indiceX][indiceY - id];
                        n += 1;
                    }
                    if (indiceY + id < height) {
                        somme += mdc[indiceX][indiceY + id];
                        n += 1;
                    }
                    mdc[indiceX][indiceY] = somme / n + ThreadLocalRandom.current().nextDouble(-height, height);
                    sommets[indiceX][indiceY].setZ(mdc[indiceX][indiceY]);

                }
            }
            i = id;
        }
        return mdc;
    }

    public void appPerlin(){
        for(int i = 0; i < nbCases; i++){
            for(int j = 0; j < nbCases; j++){
                sommets[i][j].setZ(perlinNoise(i, j, 10)*100);
            }
        }
    }

    public double perlinNoise(double x, double y, double res){
        double tmpX, tmpY;
        int x0,y0,ii,jj,gi0,gi1,gi2,gi3;
        double unit = 1.0f/Math.sqrt(2);
        double tmp,s,t,u,v,Cx,Cy,Li1,Li2;
        Vecteur gradient2[] = {
                new Vecteur(unit, unit, 0),new Vecteur(-unit, unit, 0),
                new Vecteur(unit, -unit, 0),new Vecteur(-unit, -unit, 0),
                new Vecteur(1, 0, 0),new Vecteur(-1, 0, 0),
                new Vecteur(0, 1, 0),new Vecteur(0, -1, 0),};
        int perm[] =
                {151,160,137,91,90,15,131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,
                        142,8,99,37,240,21,10,23,190,6,148,247,120,234,75,0,26,197,62,94,252,219,
                        203,117,35,11,32,57,177,33,88,237,149,56,87,174,20,125,136,171,168,68,175,
                        74,165,71,134,139,48,27,166,77,146,158,231,83,111,229,122,60,211,133,230,220,
                        105,92,41,55,46,245,40,244,102,143,54,65,25,63,161,1,216,80,73,209,76,132,
                        187,208,89,18,169,200,196,135,130,116,188,159,86,164,100,109,198,173,186,3,
                        64,52,217,226,250,124,123,5,202,38,147,118,126,255,82,85,212,207,206,59,227,
                        47,16,58,17,182,189,28,42,223,183,170,213,119,248,152,2,44,154,163,70,221,
                        153,101,155,167,43,172,9,129,22,39,253,19,98,108,110,79,113,224,232,178,185,
                        112,104,218,246,97,228,251,34,242,193,238,210,144,12,191,179,162,241,81,51,145,
                        235,249,14,239,107,49,192,214,31,181,199,106,157,184,84,204,176,115,121,50,45,
                        127,4,150,254,138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,
                        156,180};

        x /= res;
        y /= res;


        x0 = (int) (x);
        y0 = (int) (y);

        ii = x0 & 255;
        jj = y0 & 255;

        gi0 = perm[ii + perm[jj]] % 8;
        gi1 = perm[ii + 1 + perm[jj]] % 8;
        gi2 = perm[ii + perm[jj + 1]] % 8;
        gi3 = perm[ii + 1 + perm[jj + 1]] % 8;

        tmpX = x-x0;
        tmpY = y-y0;
        s = gradient2[gi0].getX()*tmpX + gradient2[gi0].getY()*tmpY;

        tmpX = x-(x0+1);
        tmpY = y-y0;
        t = gradient2[gi1].getX()*tmpX + gradient2[gi1].getY()*tmpY;

        tmpX = x-x0;
        tmpY = y-(y0+1);
        u = gradient2[gi2].getX()*tmpX + gradient2[gi2].getY()*tmpY;

        tmpX = x-(x0+1);
        tmpY = y-(y0+1);
        v = gradient2[gi3].getX()*tmpX + gradient2[gi3].getY()*tmpY;

        tmp = x-x0;
        Cx = 3 * tmp * tmp - 2 * tmp * tmp * tmp;

        Li1 = s + Cx*(t-s);
        Li2 = u + Cx*(v-u);

        tmp = y - y0;
        Cy = 3 * tmp * tmp - 2 * tmp * tmp * tmp;

        return Li1 + Cy*(Li2-Li1);
    }

    @Override
    public void paintComponent(Graphics g){
    }


    public int getPas() {
        return pas;
    }


    public int getTaille() {
        return size;
    }

    public Graphics getGraph() {
        return graph;
    }

    public Point[][] getSommets() {
        return sommets;
    }

    public Point[][] getsommetsProjete() {
        return sommetsProjete;
    }

    public Point getPointOfView() {
        return pointOfView;
    }

    public void setPointOfView(Point pointOfView) {
        this.pointOfView = pointOfView;
    }
}
