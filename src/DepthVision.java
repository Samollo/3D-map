import java.awt.*;

public class DepthVision {
    private int[][] profondeur;
    private Matrice base, terrain;
    private Color[][] pixelColor;


    public DepthVision(Matrice base, Matrice terrain) {
        this.base = base;
        this.terrain = terrain;
        profondeur = new int[terrain.getTaille()][terrain.getTaille()];
        pixelColor = new Color[terrain.getTaille()][terrain.getTaille()];
        init();
    }

    public void init(){
        for(int i = 0; i < profondeur.length; i++){
            for(int j = 0; j < profondeur.length; j++){
                profondeur[i][j] = profondeur.length;
                pixelColor[i][j] = Color.black;
            }
        }
    }

    public void apply(Graphics g){
        for(int i = 0; i < pixelColor.length; i++){
            for(int j = 0; j < pixelColor.length; j++){
                g.setColor(pixelColor[i][j]);
                g.drawLine(i,j,i,j);
            }
        }
    }

    public boolean comp(int x, int y, Color c){
        return (pixelColor[x][y].toString().equals(c.toString()));
    }

    public boolean add(int x, int y, int depth, Color c) {
        if (depth < profondeur[x][y]) {
            profondeur[x][y] = depth;
            pixelColor[x][y] = c;
            return true;

        }
        return true;
    }
}
