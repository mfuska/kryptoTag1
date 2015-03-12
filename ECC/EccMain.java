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
        ECC.Point x = basisP.scalar(ECC.DREI);
        System.out.println(x.verify());
    }
}
