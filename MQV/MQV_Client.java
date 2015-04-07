package MQV;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by mike on 07.04.15.
 */
public class MQV_Client extends MQV {

    public MQV_Client() {
        SecureRandom random = new SecureRandom();
        ECC ecc = new ECC();
        setECC(ecc);
        BigInteger qa = new BigInteger(160, random);
        setq(qa); //qa
        setQ(ecc.getBasePoint().scalar(qa));
        generate2Key();
    }
}
