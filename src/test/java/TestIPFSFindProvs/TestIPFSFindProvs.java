package TestIPFSFindProvs;

import io.ipfs.api.IPFS;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TestIPFSFindProvs {
    @Test
    public void main() throws  Exception {
        IPFS ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
        IPFS.httpTimeout = 2000;
        try {
            List<String> providers;
            providers = (List<String>) ipfs.dht.findprovsListTimeout(
                    Multihash.fromBase58("QmTdgoyYtVx53EnEJamMvK9P6yrFKQLyHBhs6XdaWhRPrZ"),
                    3,
                    3);
            System.out.println(providers);

            List<String> addrs;
            for(String provider : providers) {
                addrs = ipfs.dht.findpeerListTimeout(
                        Multihash.fromBase58(provider),
                        5
                );
                System.out.println(addrs);
            }


        }
        catch (Exception e) {
            throw e;
        }
    }
}
