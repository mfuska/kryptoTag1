import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;

/**
 * Created by mike on 06.03.15.
 */
public class ECC {
    /*
    private BigInteger a;
    private BigInteger b;
    private BigInteger p;
    private EllipticCurve curve;

    private ECC(EllipticCurve curve, BigInteger a, BigInteger b, BigInteger p) {
        this.curve = curve;
        this.a = a;
        this.b = b;
        this.p = p;
    }
     */

    public static class Point {

        private BigInteger x;
        private BigInteger y;

        public Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
            System.out.println("Point x:" + x + " y:" + y);
        }

        private Point add(Point q) {
            //x3 = (( ( (y2 - y1) / (x2 - x1) )^2 ) - x1 -x2 ) mod p
            //y3 = (( ( (y2 - y1) / (x2 - x1) ) * ( x1 - x3 ) ) - y1 ) mod p

            // if (p1 != p2) lamda = (y2 - y1) / (x2 - x1)
            // else lamda = (3 * x1 * x1 + a)/ ( 2 * y1 )

            System.out.println("this.y:" + this.y );
            System.out.println("q.y:" + q.y );
            System.out.println("this.x:" + this.x );
            System.out.println("q.x:" + q.x );
            System.out.println("q.y - y1:" + q.y.subtract(this.y) );
            System.out.println("q.x - x1:" + q.x.subtract(this.x) );

            // lamda = (y2 - y1) * (x2 - x1) ^ (-1)
            // this^(-1) mod m
            // modInverse(m)
            BigInteger lamda = ((q.y.subtract(this.y).modInverse(q.x.subtract(this.x))));
            //BigInteger lamda = ((q.x.subtract(this.x))).modInverse(q.x.subtract(this.x));

            //x3 = ( ( (y2 - y1) / (x2 - x1) )^2 ) - x1 -x2
            //x3 = ( lamda * lamda ) - x1 -x2
            BigInteger tmp = lamda.multiply(lamda);
            BigInteger qx = tmp.subtract(this.x).subtract(q.x);

            //y3 = ( ( (y2 - y1) / (x2 - x1) ) * ( x1 - x3 ) ) - y1
            //y3 = ( ( lamda ) * ( x1 - x3) ) - y1
            tmp = this.x.subtract(qx);
            BigInteger qy = lamda.multiply(tmp).subtract(this.y);

            return new Point(qx, qy);
        }

        public BigInteger getY() {
            return this.y;
        }

        public BigInteger getX() {
            return this.x;
        }
    }

    /* private Point multiply(Point p1, int n) {
         // if (p1 != p2) lamda = (y2 - y1) / (x2 - x1) mod p
         // else lamda = ((3 * x1 * x1 + a)/ ( 2 * y1 ) ) mod p
         double px = p1.x;
         double py = p1.y;
         double lamda;
         double qx;
         double qy;

         //lamda = (( (3 * px * px ) + this.a ) % this.p) / (( 2 * py ) % this.p);
         lamda =  (( (3 * px * px ) + this.a )  / ( 2 * py )) % this.p;
         System.out.println("( 3 * " + px + " * " + px + " ) + " + this.a + " / ( 2 * " + py);
         System.out.println("lamda:" + lamda);

         //x3 = ( lamda ^2 ) - x1 -x2
          qx = (lamda * lamda) - px - px;

          //y3 = ( ( (y2 - y1) / (x2 - x1) ) * ( x1 - x3 ) ) - y1
          qy = ( lamda * (px - qx) ) - py;
          System.out.println("qx:" + qx + " qy:" + qy);
          return new Point(qx,qy);
     }
 */
    public static void main(String[] args) {
        // (y * y) = (x * x * x) + (a * x) + b
        //EllipticCurve myCurve = new EllipticCurve(new ECFieldFp(new BigInteger("23")), new BigInteger("1"), new BigInteger("1"));
        //ECC ecc = new ECC(myCurve, new BigInteger("1"), new BigInteger("1"), new BigInteger("23") );
        Point a = new Point(new BigInteger("4"), new BigInteger("0"));
        Point b = new Point(new BigInteger("3"), new BigInteger("10"));

        Point c = a.add(b);

        //Point d = ecc.multiply(b, 1);
        //Point f = ecc.multiply(b, 2);
    }
}

