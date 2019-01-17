package github.com.crazyStrongboy.client;

import github.com.crazyStrongboy.node.DataSyncServiceGrpc;
import github.com.crazyStrongboy.node.SyncRequest;
import github.com.crazyStrongboy.node.SyncResponse;
import github.com.crazyStrongboy.stream.StreamRequest;
import github.com.crazyStrongboy.stream.StreamResponse;
import github.com.crazyStrongboy.stream.StreamServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BootstrapClient {

    private final ManagedChannel channel;
    // 普通调用可以用阻塞性的sub
    private DataSyncServiceGrpc.DataSyncServiceBlockingStub blockingStub;
    // 流式调用必须要异步的sub
    private StreamServiceGrpc.StreamServiceStub serviceStub;

    public BootstrapClient(String hostname, int port) {
        channel = ManagedChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build();
        blockingStub = DataSyncServiceGrpc.newBlockingStub(channel);
        serviceStub = StreamServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sync(String name) {
        try {
            SyncRequest request = SyncRequest.newBuilder().setApply(name).build();
            SyncResponse response = blockingStub.sync(request);
            System.out.println("response:" + response.getVersion());
            streamTest(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void streamTest(String name) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<StreamResponse> streamObserver = new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse value) {
                System.err.println(value.getReceive());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };
        StreamObserver<StreamRequest> requestStream = serviceStub.streamRpc(streamObserver);

        for (int i = 0; i < 100; i++) {
            requestStream.onNext(StreamRequest.newBuilder().setSend(name).build());
            Thread.sleep(1000);
        }
        requestStream.onCompleted();
        if (!latch.await(1, TimeUnit.MINUTES)) {
            System.out.println("can not finish within 1 minutes");
        }
    }


    public static void main(String[] args) throws Exception {
        BootstrapClient client = new BootstrapClient("localhost", 42420);
        String name = args.length > 0 ? args[0] : "unknown";

        try {
            client.sync(name);
            client.streamTest(name);
        } finally {
            client.shutdown();
        }
    }
}
