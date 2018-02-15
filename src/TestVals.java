import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class TestVals {

    static Map<BigInteger, String> docMap() {
        Map<BigInteger, String> map = new HashMap<BigInteger, String>();

        map.put(new BigInteger("1"), "hello");
        map.put(new BigInteger("2"), "world");
        map.put(new BigInteger("3"), "hal");
        map.put(new BigInteger("4"), "daisy");

        System.out.println(map.get(new BigInteger("1")));

        return map;
    }
}
