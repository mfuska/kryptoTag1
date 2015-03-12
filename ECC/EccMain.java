package ECC;

import java.math.BigInteger;

/**
 * Created by mike on 12.03.15.
 */
public class EccMain {

    public static void main(String[] args) {
        // (y * y) = (x * x * x) + (a * x) + b
        // Setting for Curve P-224
        BigInteger a = new BigInteger("-3");
        BigInteger b = new BigInteger("b4050a850c04b3abf54132565044b0b7d7bfd8ba270b39432355ffb4", 16);
        BigInteger p = new BigInteger("26959946667150639794667015087019630673557916260026308143510066298881");

        ECC ecc = new ECC(a,b,p);

        BigInteger gX = new BigInteger("b70e0cbd6bb4bf7f321390b94a03c1d356c21122343280d6115c1d21" ,16);
        BigInteger gY = new BigInteger("bd376388b5f723fb4c22dfe6cd4375a05a07476444d5819985007e34", 16);

        ECC.Point basisP = ecc.newPoint(gX, gY);

        ECC.Point x = basisP.scalar(BigInteger.ONE);
        if (x.verify()) {
            System.out.println("x ist auf der Kurve");
        } else {
            System.out.println("x ist nicht auf der Kurve");
        }
        System.out.println(x.getX().toString(16));
        System.out.println(x.getY().toString(16));

        ECC.Point y = basisP.scalar(ECC.TWO);
        if (y.verify()) {
            System.out.println("y ist auf der Kurve");
        } else {
            System.out.println("y ist nicht auf der Kurve");
        }
        System.out.println(y.getX().toString(16));
        System.out.println(y.getY().toString(16));

        ECC.Point z = basisP.scalar(new BigInteger("26959946667150639794667015087019625940457807714424391721682722368060"));
        if (z.verify()) {
            System.out.println("z ist auf der Kurve");
        } else {
            System.out.println("z ist nicht auf der Kurve");
        }
        System.out.println(z.getX().toString(16));
        System.out.println(z.getY().toString(16));
    }
}
