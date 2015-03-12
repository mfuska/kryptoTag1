package ECC;
import java.math.BigInteger;

/**
 * Created by mike on 06.03.15.
 */
public class ECC {

    protected static BigInteger TWO = new BigInteger("2");
    protected static BigInteger DREI = new BigInteger("3");

    private BigInteger a;
    private BigInteger b;
    private BigInteger p;

    public ECC(BigInteger a, BigInteger b, BigInteger p) {
        this.a = a;
        this.b = b;
        this.p = p;
    }

    protected Point newPoint(BigInteger x, BigInteger y) {
        return new Point(x,y);
    }

    protected class Point {

        private BigInteger x;
        private BigInteger y;

        protected Point() {
            this.x = BigInteger.ZERO;
            this.y = BigInteger.ZERO;
            //System.out.println("Point x:" + x.toString(16) + " y:" + y.toString(16));
        }
        protected Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
            //System.out.println("Point x:" + x.toString(16) + " y:" + y.toString(16));
        }
        public BigInteger getX() {
            return this.x;
        }
        public BigInteger getY() {
            return this.y;
        }

        protected Point add(Point q) {
            // if (p1 != p2) lamda = (y2 - y1) / (x2 - x1)
            // else lamda = (3 * x1 * x1 + a)/ ( 2 * y1 )

            if (this.x.equals(q.x) && this.y.equals(q.y)) {
                return this.doubled();
            } else if (q.x.equals(BigInteger.ZERO) && q.y.equals(BigInteger.ZERO)) {
                return this;
            } else {
                //x3 = (( ( (y2 - y1) / (x2 - x1) )^2 ) - x1 -x2 ) mod p
                //y3 = (( ( (y2 - y1) / (x2 - x1) ) * ( x1 - x3 ) ) - y1 ) mod p

                // lamda =  ( (y2 -y1) / (x2 -x1) )
                // lamda = (q.y - this.y) *  (q.x - this.x).modInverse(p)
                BigInteger lamda = (q.y.subtract(this.y).multiply(q.x.subtract(this.x).modInverse(ECC.this.p)));

                //x3 = (( ( (y2 - y1) / (x2 - x1) )^2 ) - x1 -x2 ) mod p
                //x3 = ( lamda * lamda ) - x1 -x2
                BigInteger qx = (lamda.pow(2).subtract(this.x).subtract(q.x)).mod(ECC.this.p);

                //y3 = ( ( (y2 - y1) / (x2 - x1) ) * ( x1 - x3 ) ) - y1
                //y3 = ( ( lamda ) * ( x1 - x3) ) - y1
                BigInteger qy = (lamda.multiply(this.x.subtract(qx)).subtract(this.y)).mod(ECC.this.p);

                return new Point(qx, qy);
            }
        }
        protected Point doubled() {

            //lamda =  (( (3 * px * px ) + this.a )  / ( 2 * py )) % p;
            BigInteger lamda = ( this.x.pow(2).multiply( ECC.DREI ) ).add(ECC.this.a).multiply(this.y.multiply(ECC.TWO).modInverse(ECC.this.p));

            //x3 = ( lamda ^2 ) - x1 -x2
            BigInteger qx = ( lamda.pow(2).subtract(this.x.multiply( ECC.TWO ) ) ).mod(ECC.this.p);

            //y3 = ( ( (y2 - y1) / (x2 - x1) ) * ( x1 - x3 ) ) - y1
            BigInteger qy = (( lamda.multiply(this.x.subtract(qx)) ).subtract(this.y)).mod(ECC.this.p);

            return new Point(qx,qy);
        }
        protected Point scalar(BigInteger n) {

            Point p = new Point(this.getX(), this.getY());
            Point r = new Point();

            for (int i = 0; i < n.bitLength(); i++) {
                if (n.testBit(i)) {
                    r = p.add(r);
                }
                p = p.doubled();
            }
            return r;
        }
        protected Boolean verify() {
            // y ^ 2 = (x^3 + ax + b) mod p
            BigInteger tmp =  y.pow(2).mod(ECC.this.p);
            BigInteger tmp1 = ( this.x.pow(3).add( ECC.this.a.multiply(this.x) ).add( ECC.this.b ) ).mod(ECC.this.p) ;
            return tmp.equals(tmp1);
        }
    }
}

