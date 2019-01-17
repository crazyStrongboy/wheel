package github.com.crazyStrongboy.node;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.16.1)",
    comments = "Source: test_node.proto")
public final class DataSyncServiceGrpc {

  private DataSyncServiceGrpc() {}

  public static final String SERVICE_NAME = "data_node.DataSyncService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<github.com.crazyStrongboy.node.SyncRequest,
      github.com.crazyStrongboy.node.SyncResponse> getSyncMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sync",
      requestType = github.com.crazyStrongboy.node.SyncRequest.class,
      responseType = github.com.crazyStrongboy.node.SyncResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<github.com.crazyStrongboy.node.SyncRequest,
      github.com.crazyStrongboy.node.SyncResponse> getSyncMethod() {
    io.grpc.MethodDescriptor<github.com.crazyStrongboy.node.SyncRequest, github.com.crazyStrongboy.node.SyncResponse> getSyncMethod;
    if ((getSyncMethod = DataSyncServiceGrpc.getSyncMethod) == null) {
      synchronized (DataSyncServiceGrpc.class) {
        if ((getSyncMethod = DataSyncServiceGrpc.getSyncMethod) == null) {
          DataSyncServiceGrpc.getSyncMethod = getSyncMethod = 
              io.grpc.MethodDescriptor.<github.com.crazyStrongboy.node.SyncRequest, github.com.crazyStrongboy.node.SyncResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "data_node.DataSyncService", "sync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  github.com.crazyStrongboy.node.SyncRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  github.com.crazyStrongboy.node.SyncResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new DataSyncServiceMethodDescriptorSupplier("sync"))
                  .build();
          }
        }
     }
     return getSyncMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DataSyncServiceStub newStub(io.grpc.Channel channel) {
    return new DataSyncServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DataSyncServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DataSyncServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DataSyncServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DataSyncServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class DataSyncServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void sync(github.com.crazyStrongboy.node.SyncRequest request,
        io.grpc.stub.StreamObserver<github.com.crazyStrongboy.node.SyncResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSyncMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSyncMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                github.com.crazyStrongboy.node.SyncRequest,
                github.com.crazyStrongboy.node.SyncResponse>(
                  this, METHODID_SYNC)))
          .build();
    }
  }

  /**
   */
  public static final class DataSyncServiceStub extends io.grpc.stub.AbstractStub<DataSyncServiceStub> {
    private DataSyncServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataSyncServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataSyncServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataSyncServiceStub(channel, callOptions);
    }

    /**
     */
    public void sync(github.com.crazyStrongboy.node.SyncRequest request,
        io.grpc.stub.StreamObserver<github.com.crazyStrongboy.node.SyncResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSyncMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DataSyncServiceBlockingStub extends io.grpc.stub.AbstractStub<DataSyncServiceBlockingStub> {
    private DataSyncServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataSyncServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataSyncServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataSyncServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public github.com.crazyStrongboy.node.SyncResponse sync(github.com.crazyStrongboy.node.SyncRequest request) {
      return blockingUnaryCall(
          getChannel(), getSyncMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DataSyncServiceFutureStub extends io.grpc.stub.AbstractStub<DataSyncServiceFutureStub> {
    private DataSyncServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataSyncServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataSyncServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataSyncServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<github.com.crazyStrongboy.node.SyncResponse> sync(
        github.com.crazyStrongboy.node.SyncRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSyncMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SYNC = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DataSyncServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DataSyncServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SYNC:
          serviceImpl.sync((github.com.crazyStrongboy.node.SyncRequest) request,
              (io.grpc.stub.StreamObserver<github.com.crazyStrongboy.node.SyncResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class DataSyncServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DataSyncServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return github.com.crazyStrongboy.node.TestNodes.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DataSyncService");
    }
  }

  private static final class DataSyncServiceFileDescriptorSupplier
      extends DataSyncServiceBaseDescriptorSupplier {
    DataSyncServiceFileDescriptorSupplier() {}
  }

  private static final class DataSyncServiceMethodDescriptorSupplier
      extends DataSyncServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DataSyncServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DataSyncServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DataSyncServiceFileDescriptorSupplier())
              .addMethod(getSyncMethod())
              .build();
        }
      }
    }
    return result;
  }
}
