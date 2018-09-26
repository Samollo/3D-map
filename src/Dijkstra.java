import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

//Pas = 61

public class Dijkstra {
    private Point[][] matrice;
    private Point[][] sommetBase;
    private Graphics graphics;
    private Point depart;
    private Point arrive;
    private int pas;

    private HashMap<Point, ArrayList<Point>> neighbour;
    private HashMap<String, Integer> distance;
    private HashMap<Point, Integer> cost;
    private HashMap<Point, Point> predecessor;
    private HashMap<Point, Point> coordonnes;
    private ArrayList<Point> elements;


    public Dijkstra(Matrice ma, Graphics graph) {
        matrice = ma.getsommetsProjete();
        sommetBase = ma.getSommets();
        pas = ma.getPas();
        graphics = graph;
        neighbour = new HashMap<>();
        distance = new HashMap<>();
        cost = new HashMap<>();
        predecessor = new HashMap<>();
        coordonnes = new HashMap<>();

        elements = new ArrayList<>();
        initElements();
    }

    private Point extractMinimal() {
        Point valueMin = null;
        for (int i = 0; i < elements.size(); i++) {
            if (valueMin == null)
                valueMin = elements.get(i);
            else {
                if (getCost(elements.get(i)) < getCost(valueMin))
                    valueMin = elements.get(i);
            }
        }
        return valueMin;
    }

    public void calcul(Point dep, Point arr) {
        initElements();
        depart = nearestPoint(dep);
        arrive = nearestPoint(arr);

        if (depart != null && arrive != null) {

            cost.put(depart, 0);

            while (!elements.isEmpty()) {
                Point q = extractMinimal();
                elements.remove(q);
                for (int i = 0; i < neighbour.get(q).size(); i++) {
                    setDistance(q);
                }
            }

            afficher();
        }
    }

    public void afficher() {
        if (depart != null && arrive != null) {

            Point n = arrive;
            Graphics2D g = (Graphics2D) graphics;
            Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(5));

            while (n != depart) {
                g.setColor(Color.RED);
                int x1 = (int) coordonnes.get(n).getX();
                int y1 = (int) coordonnes.get(n).getY();

                n = predecessor.get(n);
                if (n != depart) {
                    int x2 = (int) coordonnes.get(n).getX();
                    int y2 = (int) coordonnes.get(n).getY();
                    graphics.drawLine((int) matrice[x1][y1].getX(), (int) matrice[x1][y1].getY(), (int) matrice[x2][y2].getX(), (int) matrice[x2][y2].getY());
                }
            }
            g.setStroke(s);
        }
    }

    public void setMatrice(Point[][] matrice) {
        this.matrice = matrice;
    }

    private Integer distanceBetween(Point depart, Point arrive) {
        return distance.get(depart.toString() + arrive.toString());
    }

    private Integer getCost(Point point) {
        return cost.get(point);
    }

    private ArrayList<Point> getNeighbour(Point point) {
        return neighbour.get(point);
    }

    private void setDistance(Point point) {
        ArrayList<Point> nextPoint = getNeighbour(point);
        for (int i = 0; i < nextPoint.size(); i++) {
            if (distanceBetween(point, nextPoint.get(i)) != null) {
                if (getCost(nextPoint.get(i)) > getCost(point) + distanceBetween(point, nextPoint.get(i))) {
                    cost.put(nextPoint.get(i), cost.get(point) + distance.get(point.toString() + nextPoint.get(i).toString()));
                    predecessor.put(nextPoint.get(i), point);
                }
            }
        }
    }

    private void initElements() {
        for (int x = 0; x < matrice.length; x++) {
            for (int y = 0; y < matrice[0].length; y++) {
                coordonnes.put(matrice[x][y], new Point(x, y, 0));
                elements.add(matrice[x][y]);
                cost.put(matrice[x][y], Integer.MAX_VALUE);
                ArrayList<Point> array = new ArrayList<>();
                try {
                    array.add(matrice[x - 1][y]);
                    if (matrice[x][y].getZ() < 0 || matrice[x - 1][y].getZ() < 0) {
                        distance.put(matrice[x][y].toString() + matrice[x - 1][y].toString(), 10);
                        distance.put(matrice[x - 1][y].toString() + matrice[x][y].toString(), 10);
                    } else {
                        distance.put(matrice[x][y].toString() + matrice[x - 1][y].toString(), (int) Math.abs(matrice[x - 1][y].getZ() - matrice[x][y].getZ()));
                        distance.put(matrice[x - 1][y].toString() + matrice[x][y].toString(), (int) Math.abs(matrice[x][y].getZ() - matrice[x - 1][y].getZ()));
                    }
                } catch (Exception e) {
                }
                try {
                    array.add(matrice[x][y - 1]);
                    if (matrice[x][y].getZ() < 0 || matrice[x][y - 1].getZ() < 0) {
                        distance.put(matrice[x][y].toString() + matrice[x][y - 1].toString(), 10);
                        distance.put(matrice[x][y - 1].toString() + matrice[x][y].toString(), 10);

                    } else {
                        distance.put(matrice[x][y].toString() + matrice[x][y - 1].toString(), (int) Math.abs(matrice[x][y - 1].getZ() - matrice[x][y].getZ()));
                        distance.put(matrice[x][y - 1].toString() + matrice[x][y].toString(), (int) Math.abs(matrice[x][y].getZ() - matrice[x][y - 1].getZ()));
                    }
                } catch (Exception e) {
                }
                try {
                    array.add(matrice[x + 1][y]);
                    if (matrice[x][y].getZ() < 0 || matrice[x + 1][y].getZ() < 0) {
                        distance.put(matrice[x][y].toString() + matrice[x + 1][y].toString(), 10);
                        distance.put(matrice[x + 1][y].toString() + matrice[x][y].toString(), 10);
                    } else {
                        distance.put(matrice[x][y].toString() + matrice[x + 1][y].toString(), (int) Math.abs(matrice[x + 1][y].getZ() - matrice[x][y].getZ()));
                        distance.put(matrice[x + 1][y].toString() + matrice[x][y].toString(), (int) Math.abs(matrice[x][y].getZ() - matrice[x + 1][y].getZ()));
                    }
                } catch (Exception e) {
                }
                try {
                    array.add(matrice[x][y + 1]);
                    if (matrice[x][y].getZ() < 0 || matrice[y + 1][y].getZ() < 0) {
                        distance.put(matrice[x][y].toString() + matrice[x][y + 1].toString(), 10);
                        distance.put(matrice[x][y + 1].toString() + matrice[x][y].toString(), 10);

                    } else {
                        distance.put(matrice[x][y].toString() + matrice[x][y + 1].toString(), (int) Math.abs(matrice[x][y + 1].getZ() - matrice[x][y].getZ()));
                        distance.put(matrice[x][y + 1].toString() + matrice[x][y].toString(), (int) Math.abs(matrice[x][y].getZ() - matrice[x][y + 1].getZ()));
                    }
                } catch (Exception e) {
                    //       }
                }
                neighbour.put(matrice[x][y], array);
            }
        }
    }


    public Point nearestPoint(Point at) {
        int res = 0;
        int x = 0;
        int y = 0;


        for (x = 0; x < matrice.length; x++) {
            for (y = 0; y < matrice[0].length; y++) {
                int difx = (int) at.getX() - (int) matrice[x][y].getX();
                int dify = (int) at.getY() - (int) matrice[x][y].getY();

                if (difx < pas && difx > 0 && dify < pas && dify > 0) {
                    res = 1;
                    break;
                }
            }
            if (res == 1) {
                break;
            }
        }

        if (x < matrice.length && y < matrice[0].length)
            return matrice[x][y];
        else
            return null;
    }

}
