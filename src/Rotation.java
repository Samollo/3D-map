public class Rotation {

    public double m00,m01;
    public double m10,m11;
    double x1, y1;

    public Rotation(Point centre, double angle) {
//        axis = axis.normalized();

        x1 = centre.getX();
        y1 = centre.getY();
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);

        m00 = cosA;	m01 = -sinA;
        m10 = sinA;	m11 = cosA;
    }

    public Vecteur transform(double x, double y) {
        return new Vecteur(
                m00*(x-x1) + m01*(y-y1),
                m10*(x-x1) + m11*(y-y1),
                0
        );
    }
}