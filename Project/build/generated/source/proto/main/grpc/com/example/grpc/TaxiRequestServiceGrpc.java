package com.example.grpc;

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
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: RIdeService.proto")
public final class TaxiRequestServiceGrpc {

  private TaxiRequestServiceGrpc() {}

  public static final String SERVICE_NAME = "com.example.grpc.TaxiRequestService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RideRequest,
      com.example.grpc.RIdeService.RideResponse> getReserveRideMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "reserveRide",
      requestType = com.example.grpc.RIdeService.RideRequest.class,
      responseType = com.example.grpc.RIdeService.RideResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RideRequest,
      com.example.grpc.RIdeService.RideResponse> getReserveRideMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RideRequest, com.example.grpc.RIdeService.RideResponse> getReserveRideMethod;
    if ((getReserveRideMethod = TaxiRequestServiceGrpc.getReserveRideMethod) == null) {
      synchronized (TaxiRequestServiceGrpc.class) {
        if ((getReserveRideMethod = TaxiRequestServiceGrpc.getReserveRideMethod) == null) {
          TaxiRequestServiceGrpc.getReserveRideMethod = getReserveRideMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.RideRequest, com.example.grpc.RIdeService.RideResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "reserveRide"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RideRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RideResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiRequestServiceMethodDescriptorSupplier("reserveRide"))
              .build();
        }
      }
    }
    return getReserveRideMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.RIdeService.TaxiDistrictRequest,
      com.example.grpc.RIdeService.TaxiDistrictResponse> getChangeDistrictMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "changeDistrict",
      requestType = com.example.grpc.RIdeService.TaxiDistrictRequest.class,
      responseType = com.example.grpc.RIdeService.TaxiDistrictResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.RIdeService.TaxiDistrictRequest,
      com.example.grpc.RIdeService.TaxiDistrictResponse> getChangeDistrictMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.RIdeService.TaxiDistrictRequest, com.example.grpc.RIdeService.TaxiDistrictResponse> getChangeDistrictMethod;
    if ((getChangeDistrictMethod = TaxiRequestServiceGrpc.getChangeDistrictMethod) == null) {
      synchronized (TaxiRequestServiceGrpc.class) {
        if ((getChangeDistrictMethod = TaxiRequestServiceGrpc.getChangeDistrictMethod) == null) {
          TaxiRequestServiceGrpc.getChangeDistrictMethod = getChangeDistrictMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.TaxiDistrictRequest, com.example.grpc.RIdeService.TaxiDistrictResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "changeDistrict"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.TaxiDistrictRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.TaxiDistrictResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiRequestServiceMethodDescriptorSupplier("changeDistrict"))
              .build();
        }
      }
    }
    return getChangeDistrictMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RechargeRequest,
      com.example.grpc.RIdeService.RechargeResponse> getReserveRechargerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "reserveRecharger",
      requestType = com.example.grpc.RIdeService.RechargeRequest.class,
      responseType = com.example.grpc.RIdeService.RechargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RechargeRequest,
      com.example.grpc.RIdeService.RechargeResponse> getReserveRechargerMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RechargeRequest, com.example.grpc.RIdeService.RechargeResponse> getReserveRechargerMethod;
    if ((getReserveRechargerMethod = TaxiRequestServiceGrpc.getReserveRechargerMethod) == null) {
      synchronized (TaxiRequestServiceGrpc.class) {
        if ((getReserveRechargerMethod = TaxiRequestServiceGrpc.getReserveRechargerMethod) == null) {
          TaxiRequestServiceGrpc.getReserveRechargerMethod = getReserveRechargerMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.RechargeRequest, com.example.grpc.RIdeService.RechargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "reserveRecharger"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RechargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RechargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TaxiRequestServiceMethodDescriptorSupplier("reserveRecharger"))
              .build();
        }
      }
    }
    return getReserveRechargerMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TaxiRequestServiceStub newStub(io.grpc.Channel channel) {
    return new TaxiRequestServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TaxiRequestServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TaxiRequestServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TaxiRequestServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TaxiRequestServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TaxiRequestServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void reserveRide(com.example.grpc.RIdeService.RideRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RideResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getReserveRideMethod(), responseObserver);
    }

    /**
     */
    public void changeDistrict(com.example.grpc.RIdeService.TaxiDistrictRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.TaxiDistrictResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getChangeDistrictMethod(), responseObserver);
    }

    /**
     */
    public void reserveRecharger(com.example.grpc.RIdeService.RechargeRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RechargeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getReserveRechargerMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getReserveRideMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.RIdeService.RideRequest,
                com.example.grpc.RIdeService.RideResponse>(
                  this, METHODID_RESERVE_RIDE)))
          .addMethod(
            getChangeDistrictMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.RIdeService.TaxiDistrictRequest,
                com.example.grpc.RIdeService.TaxiDistrictResponse>(
                  this, METHODID_CHANGE_DISTRICT)))
          .addMethod(
            getReserveRechargerMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.RIdeService.RechargeRequest,
                com.example.grpc.RIdeService.RechargeResponse>(
                  this, METHODID_RESERVE_RECHARGER)))
          .build();
    }
  }

  /**
   */
  public static final class TaxiRequestServiceStub extends io.grpc.stub.AbstractStub<TaxiRequestServiceStub> {
    private TaxiRequestServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiRequestServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiRequestServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiRequestServiceStub(channel, callOptions);
    }

    /**
     */
    public void reserveRide(com.example.grpc.RIdeService.RideRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RideResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReserveRideMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void changeDistrict(com.example.grpc.RIdeService.TaxiDistrictRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.TaxiDistrictResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getChangeDistrictMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reserveRecharger(com.example.grpc.RIdeService.RechargeRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RechargeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReserveRechargerMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TaxiRequestServiceBlockingStub extends io.grpc.stub.AbstractStub<TaxiRequestServiceBlockingStub> {
    private TaxiRequestServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiRequestServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiRequestServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiRequestServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.grpc.RIdeService.RideResponse reserveRide(com.example.grpc.RIdeService.RideRequest request) {
      return blockingUnaryCall(
          getChannel(), getReserveRideMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.grpc.RIdeService.TaxiDistrictResponse changeDistrict(com.example.grpc.RIdeService.TaxiDistrictRequest request) {
      return blockingUnaryCall(
          getChannel(), getChangeDistrictMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.grpc.RIdeService.RechargeResponse reserveRecharger(com.example.grpc.RIdeService.RechargeRequest request) {
      return blockingUnaryCall(
          getChannel(), getReserveRechargerMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TaxiRequestServiceFutureStub extends io.grpc.stub.AbstractStub<TaxiRequestServiceFutureStub> {
    private TaxiRequestServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaxiRequestServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaxiRequestServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaxiRequestServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.RIdeService.RideResponse> reserveRide(
        com.example.grpc.RIdeService.RideRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReserveRideMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.RIdeService.TaxiDistrictResponse> changeDistrict(
        com.example.grpc.RIdeService.TaxiDistrictRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getChangeDistrictMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.RIdeService.RechargeResponse> reserveRecharger(
        com.example.grpc.RIdeService.RechargeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReserveRechargerMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RESERVE_RIDE = 0;
  private static final int METHODID_CHANGE_DISTRICT = 1;
  private static final int METHODID_RESERVE_RECHARGER = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TaxiRequestServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TaxiRequestServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RESERVE_RIDE:
          serviceImpl.reserveRide((com.example.grpc.RIdeService.RideRequest) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RideResponse>) responseObserver);
          break;
        case METHODID_CHANGE_DISTRICT:
          serviceImpl.changeDistrict((com.example.grpc.RIdeService.TaxiDistrictRequest) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.TaxiDistrictResponse>) responseObserver);
          break;
        case METHODID_RESERVE_RECHARGER:
          serviceImpl.reserveRecharger((com.example.grpc.RIdeService.RechargeRequest) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RechargeResponse>) responseObserver);
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

  private static abstract class TaxiRequestServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TaxiRequestServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.grpc.RIdeService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TaxiRequestService");
    }
  }

  private static final class TaxiRequestServiceFileDescriptorSupplier
      extends TaxiRequestServiceBaseDescriptorSupplier {
    TaxiRequestServiceFileDescriptorSupplier() {}
  }

  private static final class TaxiRequestServiceMethodDescriptorSupplier
      extends TaxiRequestServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TaxiRequestServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (TaxiRequestServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TaxiRequestServiceFileDescriptorSupplier())
              .addMethod(getReserveRideMethod())
              .addMethod(getChangeDistrictMethod())
              .addMethod(getReserveRechargerMethod())
              .build();
        }
      }
    }
    return result;
  }
}
