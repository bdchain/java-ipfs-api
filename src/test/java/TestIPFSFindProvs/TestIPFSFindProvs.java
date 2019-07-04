package TestIPFSFindProvs;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class TestIPFSFindProvs {
    @Test
    public void main() throws  Exception {
        IPFS ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
        IPFS.httpTimeout = 2000;
        try {
            FileInputStream in = new FileInputStream("drill-test.json");
            byte[] contents = ByteStreams.toByteArray(in);
            MerkleNode node = ipfs.object.patch(
                    Multihash.fromBase58("QmdfTbBqBPQ7VNxZEYEj14VmRuZBkqFbiwReogJgS1zR1n"),
                    "set-data",
                    Optional.of(contents),
                    Optional.empty(),
                    Optional.empty()
            );
            System.out.println(node.toJSONString());


        }
        catch (Exception e) {
            throw e;
        }
    }
}
