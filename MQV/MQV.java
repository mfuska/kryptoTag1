package MQV;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by mike on 19.03.15.
 */
public class MQV {
    private BigInteger S;
    private ECC ecc;
    //Zertifikat
    private BigInteger q; //  private Key
    private ECC.Point Q; // public Key

    private BigInteger k; //  private Key
    private ECC.Point R; // public Key

    private ECC.Point Q_public; //foreign public Key
    private ECC.Point R_public; //foreign public Key

    private ECC.Point Z;


    public MQV() {
    }
    protected void setQ(ECC.Point Q) {
        this.Q = Q;
        System.out.println("Q x:" + this.Q.getX() + " y:" + this.Q.getY());
    }
    protected void setq(BigInteger q) {
        this.q = q;
        System.out.println("q:" + this.q);
    }
    protected void setECC(ECC ecc) {
        this.ecc = ecc;
    }
    protected void setQ_public(ECC.Point Q_public) {
        this.Q_public = Q_public;
    }
    protected void setR_public(ECC.Point R_public) {
        this.R_public = R_public;
    }
    protected void checkValidyR_public(ECC.Point R_public) throws MQVException {
        //x,y sind nicht unendlich
        if (R_public.getX().equals(BigInteger.ZERO) || R_public.getY().equals(BigInteger.ZERO) ) {
            throw new MQVException("ECC checkValidyR_public ERROR: R is infinite");
        }
        //x < (p-1) && x >= 0
        // compareTo: -1 == less than  0 == equals  1 == greather than
        if ( R_public.getX().compareTo( ecc.getP().subtract(BigInteger.ONE)) == 1 || R_public.getX().compareTo(BigInteger.ZERO) == -1  ) {
            throw new MQVException("ECC checkValidyR_public ERROR: Rx is not the order of Fq");
        }
        if ( R_public.getY().compareTo( ecc.getP().subtract(BigInteger.ONE)) == 1 || R_public.getX().compareTo(BigInteger.ZERO) == -1  ) {
            throw new MQVException("ECC checkValidyR_public ERROR: Ry is not the order of Fq");
        }
        if (! R_public.verify()) {
            throw new MQVException("ECC checkValidyR_public ERROR: R is not a Point of ECC Curve");
        }
    }
    protected ECC.Point getR_public() {
        return R_public;
    }
    protected ECC.Point getQ_public() {
        return Q_public;
    }
    protected ECC.Point getR() {
        return R;
    }
    protected ECC.Point getQ() {
        return Q;
    }
    protected ECC.Point getZ() {
        return this.Z;
    }
    protected void generate2Key() {
        SecureRandom random = new SecureRandom();
        this.k = new BigInteger(160, random);
        this.R = ecc.getBasePoint().scalar(this.k);
        this.calculateS();
    }
    private BigInteger firstLBits(BigInteger x) {
          //  (int) Math.ceil((double)divident / divisor);
        // ceil((foor(log2(n)) + 1) / 2)
        int L = (int) Math.ceil(((double) this.ecc.getR().bitLength()) / 2) ;
        //(x mod 2^L) + 2^L
        BigInteger tmp = ECC.TWO.pow(L);
        return (x.mod(  tmp   ) ).add( tmp );
    }
    private void calculateS() {
        // S = k + R' * q
        //this.S = this.private_key.add(ecc.getA().multiply(this.firstLBits(this.getPublicKey().getX())));
        BigInteger Rstrich = this.firstLBits(this.R.getX());
        BigInteger term2 = this.q.multiply(Rstrich);
        this.S = this.k.add(term2);
    }
    protected void generateSemmetricKey() {
        // Z = h * S * (R_public + R_public' * Q_public)
        // X' = firstLBits(Xx)
        BigInteger Rstrich = this.firstLBits(this.R_public.getX());
        //point  = R' * Q_public
        ECC.Point point = Q_public.scalar(Rstrich);
        // point1 = R_public + point
        ECC.Point point1 = this.R_public.add(point);
        // semmetric_key = H * Sb * point1
        this.Z = (point1.scalar(this.S)).scalar(this.ecc.getH());
        System.out.println("semmetric key x:" + this.Z.getX() + " y:" + this.Z.getY());
        System.out.println("Check:" + this.Z.verify());
    }
}
