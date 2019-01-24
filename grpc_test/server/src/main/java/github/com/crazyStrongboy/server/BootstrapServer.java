package github.com.crazyStrongboy.server;

import github.com.crazyStrongboy.node.DataSyncServiceGrpc;
import github.com.crazyStrongboy.node.SyncRequest;
import github.com.crazyStrongboy.node.SyncResponse;
import github.com.crazyStrongboy.stream.StreamRequest;
import github.com.crazyStrongboy.stream.StreamResponse;
import github.com.crazyStrongboy.stream.StreamServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class BootstrapServer {

    private int port = 42420;
    private Server server;

    private void start() throws Exception {

        server = ServerBuilder.forPort(port)
                .addService(new DataSyncImpl())
                .addService(new StreamServiceImpl())
                .build()
                .start();
        System.err.println("start listen ........port: " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** JVM is shutting down. Turning off grpc server as well ***");
            BootstrapServer.this.stop();
            System.err.println("*** shutdown complete ***");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }


    public static void main(String[] args) throws Exception {
        final BootstrapServer syncServer = new BootstrapServer();

        syncServer.start();
        syncServer.blockUntilShutdown();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private class DataSyncImpl extends DataSyncServiceGrpc.DataSyncServiceImplBase {

        @Override
        public void sync(SyncRequest request, StreamObserver<SyncResponse> responseObserver) {
            System.err.println("request:" + request.getApply());
            SyncResponse response = SyncResponse.newBuilder().setVersion("dadada").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private class StreamServiceImpl extends StreamServiceGrpc.StreamServiceImplBase {
        @Override
        public StreamObserver<StreamRequest> streamRpc(StreamObserver<StreamResponse> responseObserver) {
//            responseObserver.onCompleted();
            responseObserver.onNext(StreamResponse.newBuilder().setReceive("xxxxxxxxxxxxxxxxxx").build());
            return new StreamObserver<StreamRequest>() {
                @Override
                public void onNext(StreamRequest request) {
                    String send = request.getSend();
                    System.err.println("server receive ,: " + send);
                    responseObserver.onNext(StreamResponse.newBuilder().setReceive("dadadada").build());
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }

    }

}
