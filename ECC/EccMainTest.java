package ECC;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class EccMainTest {
    ECC ecc;
    ECC.Point g;

    @Before
    public void setUp() throws Exception {
        BigInteger a = new BigInteger("-3");
        BigInteger b = new BigInteger("b4050a850c04b3abf54132565044b0b7d7bfd8ba270b39432355ffb4", 16);
        BigInteger p = new BigInteger("26959946667150639794667015087019630673557916260026308143510066298881");

        ecc = new ECC(a,b,p);

        BigInteger gX = new BigInteger("b70e0cbd6bb4bf7f321390b94a03c1d356c21122343280d6115c1d21" ,16);
        BigInteger gY = new BigInteger("bd376388b5f723fb4c22dfe6cd4375a05a07476444d5819985007e34", 16);

        g = ecc.newPoint(gX, gY);
    }


    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test1() throws Exception {
        BigInteger n = new BigInteger("1");
        BigInteger x = new BigInteger("B70E0CBD6BB4BF7F321390B94A03C1D356C21122343280D6115C1D21",16);
        BigInteger y = new BigInteger("BD376388B5F723FB4C22DFE6CD4375A05A07476444D5819985007E34",16);

        checkPointsOnCurve(n, x, y);
    }
    @Test
    public void test2() throws Exception {
        BigInteger n = new BigInteger("2");
        BigInteger x = new BigInteger("706A46DC76DCB76798E60E6D89474788D16DC18032D268FD1A704FA6",16);
        BigInteger y = new BigInteger("1C2B76A7BC25E7702A704FA986892849FCA629487ACF3709D2E4E8BB",16);

        checkPointsOnCurve(n, x, y);
    }


    @Test
    public void test3() throws Exception {
        BigInteger n = new BigInteger("9");
        BigInteger x = new BigInteger("2FDCCCFEE720A77EF6CB3BFBB447F9383117E3DAA4A07E36ED15F78D",16);
        BigInteger y = new BigInteger("371732E4F41BF4F7883035E6A79FCEDC0E196EB07B48171697517463",16);

        checkPointsOnCurve(n, x, y);
    }
    public void test4() throws Exception {
        BigInteger n = new BigInteger("13479966930919337728895168462090683249159702977113823384618282123295");
        BigInteger x = new BigInteger("EF353BF5C73CD551B96D596FBC9A67F16D61DD9FE56AF19DE1FBA9CD",16);
        BigInteger y = new BigInteger("21771B9CDCE3E8430C09B3838BE70B48C21E15BC09EE1F2D7945B91F",16);

        checkPointsOnCurve(n, x, y);
    }

    public void test5() throws Exception {
        BigInteger n = new BigInteger("26959946667150639794667015087019625940457807714424391721682722368060");
        BigInteger x = new BigInteger("B70E0CBD6BB4BF7F321390B94A03C1D356C21122343280D6115C1D21",16);
        BigInteger y = new BigInteger("42C89C774A08DC04B3DD201932BC8A5EA5F8B89BBB2A7E667AFF81CD",16);

        checkPointsOnCurve(n, x, y);
    }

    private void checkPointsOnCurve(BigInteger n, BigInteger erX, BigInteger erY) {
        ECC.Point result = this.g.scalar(n);

        assertEquals(erX, result.getX());
        assertEquals(erY, result.getY());
        assertEquals(Boolean.TRUE , result.verify());
    }
}