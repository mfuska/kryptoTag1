package MQV;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by mike on 08.04.15.
 */
public class MessageObj implements Serializable {
    private ECC.Point Q;
    private ECC.Point R;
    private BigInteger[] hashWert;
    private Boolean debug = true;

    public MessageObj(ECC.Point Q, ECC.Point R) {
        this.Q = Q;
        this.R = R;
        if (debug)System.out.println("packet size:" + (this.Q.getX().bitLength() + this.Q.getY().bitLength() + this.R.getX().bitLength() + this.R.getY().bitLength()));
    }
    public MessageObj(ECC.Point Q, ECC.Point R, BigInteger[] hashWert) {
       this(Q,R);
       this.hashWert = hashWert;
       if (debug) System.out.println("size hash" + hashWert[0].bitLength() + hashWert[1].bitLength());
    }
    public MessageObj(BigInteger[] hashWert) {
        this.hashWert = hashWert;
    }
    public BigInteger[] getHashWert() {
        return hashWert;
    }
    public ECC.Point getQ() {
        return Q;
    }
    public ECC.Point getR() {
        return R;
    }
}
