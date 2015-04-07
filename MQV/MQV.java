package MQV;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by mike on 19.03.15.
 */
public class MQV {
    private BigInteger S;
    private ECC ecc;

    private BigInteger Q_private_key;
    private ECC.Point  Q_publicKey;

    private ECC.Point foreign_public_key;
    private ECC.Point senmetric_key;

    public MQV() {
        SecureRandom random = new SecureRandom();
        this.Q_private_key = new BigInteger(160, random);
        this.ecc = new ECC();
        // X Server
        // Y Client
        this.Q_publicKey = ecc.getBasePoint().scalar(this.Q_private_key);
    }
    private BigInteger firstLBits(BigInteger x) {
          //  (int) Math.ceil((double)divident / divisor);
        // ceil((foor(log2(n)) + 1) / 2)
        int L = (int) Math.ceil(((double) this.ecc.getR().bitLength()) / 2) ;
        //(x mod 2^L) + 2^L
        BigInteger tmp = ECC.TWO.pow(L);
        return (x.mod(  tmp   ) ).add( tmp );
    }
    private void calculateServer_S() {
        // Sa = x + X' * a
        //this.S = this.private_key.add(ecc.getA().multiply(this.firstLBits(this.getPublicKey().getX())));
        BigInteger Xstrich = this.firstLBits(this.publicKey.getX());
        BigInteger term2 = ecc.getA().multiply(Xstrich);
        this.S = this.private_key.add(term2);

    }
    private void calculateClient_S() {
        // Sb = y + Y' * b
        //this.S = this.private_key.add(ecc.getB().multiply(this.firstLBits(this.getPublicKey().getX())));
        BigInteger Ystrich = this.firstLBits(this.publicKey.getX());
        BigInteger term2 = ecc.getB().multiply(Ystrich);
        this.S = this.private_key.add(term2);
    }
    protected void setForeign_public_key(ECC.Point point) {
        this.foreign_public_key = point;
    }
    protected ECC.Point getForeign_public_key() {
        return this.foreign_public_key;
    }
    protected void generateClientKey() {
        this.calculateClient_S();
        // K = h * Sb * (X + X' * A)
        // X' = firstLBits(Xx)
        BigInteger Xstrich = this.firstLBits(this.foreign_public_key.getX());
        // A = a * Basepoint
        ECC.Point A = ecc.getBasePoint().scalar(ecc.getA());
        //point  = X' * A
        ECC.Point point = A.scalar(Xstrich);
        // point1 = X + point
        ECC.Point point1 = this.foreign_public_key.add(point);
        // semmetric_key = H * Sb * point1
        this.senmetric_key = (point1.scalar(this.S)).scalar(this.ecc.getH());
        System.out.println("A" + A.verify());
        System.out.println("point" + point.verify());
        System.out.println("point1" + point1.verify());
        System.out.println("senmetricy key" + senmetric_key.verify());
    }
    protected void generateServerKey() {
        this.calculateServer_S();
        // K = h * Sa * (Y + Y' * B)
        // Y' = firstLBits(Yx)
        BigInteger Ystrich = this.firstLBits(this.foreign_public_key.getX());
        // B = b * Basepoint
        ECC.Point B = ecc.getBasePoint().scalar(ecc.getB());
        //point  = Y' * B
        ECC.Point point = B.scalar(Ystrich);
        // point1 = Y + point
        ECC.Point point1 = this.foreign_public_key.add(point);
        // semmetric_key = H * Sa * point1
        this.senmetric_key = (point1.scalar(this.S)).scalar(this.ecc.getH());
        System.out.println("B" + B.verify());
        System.out.println("point" + point.verify());
        System.out.println("point1" + point1.verify());
        System.out.println("senmetricy key" + senmetric_key.verify());
    }
    protected ECC.Point getSemmetric_key() {
        return this.senmetric_key;
    }
    public BigInteger getPrivate_key() {
        return this.private_key;
    }
    public ECC.Point getPublicKey() {
        return this.publicKey;
    }
}
