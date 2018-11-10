package TestIPFSFindProvs;

import io.ipfs.api.IPFS;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;

import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestIPFSFindProvs {
    public static void main(String[] args) throws  Exception {
        IPFS ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
        IPFS.httpTimeout = 2000;
        try {
            List<String> providers;
            ExecutorService executorService = Executors.newCachedThreadPool();
            FutureTask<List<String>> task = new FutureTask<>(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    return ipfs.dht.findprovsList(Multihash.fromBase58("QmNiJiXwWE3kRhZrC5ej3kSjWHm337pYfhjLGSCDNKJP2s"), 10);
                }
            });

            executorService.execute(task);
            try {
                providers = task.get(5, TimeUnit.SECONDS);

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                task.cancel(true);
                providers = Collections.emptyList();
            }
            //List<String> providers = ipfs.dht.findprovsList(Multihash.fromBase58("QmNiJiXwWE3kRhZrC5ej3kSjWHm337pYfhjLGSCDNKJP2s"), 3);
            /*Stream<String> providersStream = ipfs.dht.findprovsListStream(
                    Multihash.fromBase58("QmNiJiXwWE3kRhZrC5ej3kSjWHm337pYfhjLGSCDNKJP2s"),
                    3
            );
            List<String> providers = providersStream.limit(3).collect(Collectors.toList());*/

            System.out.println(providers);

            for(String provider: providers) {
                Multihash providerHash = Multihash.fromBase58(provider);
                try {
                    List<String> addrs;
                    task = new FutureTask<>(new Callable<List<String>>() {
                        @Override
                        public List<String> call() throws Exception {
                            return ipfs.dht.findpeerList(providerHash);
                        }
                    });
                    executorService.execute(task);

                    try {
                        addrs = task.get(5, TimeUnit.SECONDS);
                    }
                    catch (InterruptedException | ExecutionException | TimeoutException e) {
                        task.cancel(true);
                        addrs = Collections.emptyList();
                    }
                    System.out.println(addrs);
                }
                catch (Exception e) {
                    System.out.println(e);
                    continue;
                }
            }
            executorService.shutdownNow();
            while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {

            }
        }
        catch (Exception e) {
            throw e;
        }
    }
}
