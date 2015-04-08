package MQV;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by mike on 08.04.15.
 */
public class MessageObj implements Serializable {
    private ECC.Point Q;
    private ECC.Point R;
    private BigInteger hashWert;


    public MessageObj(ECC.Point Q, ECC.Point R) {
        this.Q = Q;
        this.R = R;
    }
    public MessageObj(ECC.Point Q, ECC.Point R, BigInteger hashWert) {
        this.Q = Q;
        this.R = R;
        this.hashWert = hashWert;
    }
    public MessageObj(BigInteger hashWert) {
        this.hashWert = hashWert;
    }

    public BigInteger getHashWert() {
        return hashWert;
    }
    public ECC.Point getQ() {
        return Q;
    }
    public ECC.Point getR() {
        return R;
    }
}
