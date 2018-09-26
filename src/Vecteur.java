public class Vecteur extends Point {

    public Vecteur(double x, double y, double z){
        super(x,y,z);
    }

    public Vecteur normalized() {
        Vecteur norm = this;
        float length = (float) Math.sqrt( this.getX()*this.getX() + this.getY()*this.getY() + this.getZ()*this.getZ() );
        norm.setX(norm.getX()/length);
        norm.setY(norm.getY()/length);
        norm.setZ(norm.getZ()/length);
        return norm;
    }

}
