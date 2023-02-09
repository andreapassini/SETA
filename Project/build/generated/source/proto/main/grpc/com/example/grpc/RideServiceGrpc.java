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
public final class RideServiceGrpc {

  private RideServiceGrpc() {}

  public static final String SERVICE_NAME = "com.example.grpc.RideService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.grpc.RIdeService.GreetingsRequest,
      com.example.grpc.RIdeService.GreetingsResponse> getGreetingsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "greetings",
      requestType = com.example.grpc.RIdeService.GreetingsRequest.class,
      responseType = com.example.grpc.RIdeService.GreetingsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.RIdeService.GreetingsRequest,
      com.example.grpc.RIdeService.GreetingsResponse> getGreetingsMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.RIdeService.GreetingsRequest, com.example.grpc.RIdeService.GreetingsResponse> getGreetingsMethod;
    if ((getGreetingsMethod = RideServiceGrpc.getGreetingsMethod) == null) {
      synchronized (RideServiceGrpc.class) {
        if ((getGreetingsMethod = RideServiceGrpc.getGreetingsMethod) == null) {
          RideServiceGrpc.getGreetingsMethod = getGreetingsMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.GreetingsRequest, com.example.grpc.RIdeService.GreetingsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "greetings"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.GreetingsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.GreetingsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RideServiceMethodDescriptorSupplier("greetings"))
              .build();
        }
      }
    }
    return getGreetingsMethod;
  }

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
    if ((getReserveRideMethod = RideServiceGrpc.getReserveRideMethod) == null) {
      synchronized (RideServiceGrpc.class) {
        if ((getReserveRideMethod = RideServiceGrpc.getReserveRideMethod) == null) {
          RideServiceGrpc.getReserveRideMethod = getReserveRideMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.RideRequest, com.example.grpc.RIdeService.RideResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "reserveRide"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RideRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RideResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RideServiceMethodDescriptorSupplier("reserveRide"))
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
    if ((getChangeDistrictMethod = RideServiceGrpc.getChangeDistrictMethod) == null) {
      synchronized (RideServiceGrpc.class) {
        if ((getChangeDistrictMethod = RideServiceGrpc.getChangeDistrictMethod) == null) {
          RideServiceGrpc.getChangeDistrictMethod = getChangeDistrictMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.TaxiDistrictRequest, com.example.grpc.RIdeService.TaxiDistrictResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "changeDistrict"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.TaxiDistrictRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.TaxiDistrictResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RideServiceMethodDescriptorSupplier("changeDistrict"))
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
    if ((getReserveRechargerMethod = RideServiceGrpc.getReserveRechargerMethod) == null) {
      synchronized (RideServiceGrpc.class) {
        if ((getReserveRechargerMethod = RideServiceGrpc.getReserveRechargerMethod) == null) {
          RideServiceGrpc.getReserveRechargerMethod = getReserveRechargerMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.RechargeRequest, com.example.grpc.RIdeService.RechargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "reserveRecharger"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RechargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RechargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RideServiceMethodDescriptorSupplier("reserveRecharger"))
              .build();
        }
      }
    }
    return getReserveRechargerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RechargeResponse,
      com.example.grpc.RIdeService.RechargeResponse> getReleaseRechargerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "releaseRecharger",
      requestType = com.example.grpc.RIdeService.RechargeResponse.class,
      responseType = com.example.grpc.RIdeService.RechargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RechargeResponse,
      com.example.grpc.RIdeService.RechargeResponse> getReleaseRechargerMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.RIdeService.RechargeResponse, com.example.grpc.RIdeService.RechargeResponse> getReleaseRechargerMethod;
    if ((getReleaseRechargerMethod = RideServiceGrpc.getReleaseRechargerMethod) == null) {
      synchronized (RideServiceGrpc.class) {
        if ((getReleaseRechargerMethod = RideServiceGrpc.getReleaseRechargerMethod) == null) {
          RideServiceGrpc.getReleaseRechargerMethod = getReleaseRechargerMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.RechargeResponse, com.example.grpc.RIdeService.RechargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "releaseRecharger"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RechargeResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.RechargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RideServiceMethodDescriptorSupplier("releaseRecharger"))
              .build();
        }
      }
    }
    return getReleaseRechargerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.RIdeService.ExitRequest,
      com.example.grpc.RIdeService.ExitResponse> getLeavingSystemMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "leavingSystem",
      requestType = com.example.grpc.RIdeService.ExitRequest.class,
      responseType = com.example.grpc.RIdeService.ExitResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.RIdeService.ExitRequest,
      com.example.grpc.RIdeService.ExitResponse> getLeavingSystemMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.RIdeService.ExitRequest, com.example.grpc.RIdeService.ExitResponse> getLeavingSystemMethod;
    if ((getLeavingSystemMethod = RideServiceGrpc.getLeavingSystemMethod) == null) {
      synchronized (RideServiceGrpc.class) {
        if ((getLeavingSystemMethod = RideServiceGrpc.getLeavingSystemMethod) == null) {
          RideServiceGrpc.getLeavingSystemMethod = getLeavingSystemMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.ExitRequest, com.example.grpc.RIdeService.ExitResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "leavingSystem"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.ExitRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.ExitResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RideServiceMethodDescriptorSupplier("leavingSystem"))
              .build();
        }
      }
    }
    return getLeavingSystemMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.grpc.RIdeService.HearthBeatRequest,
      com.example.grpc.RIdeService.HearthBeatResponse> getHearthBeatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "hearthBeat",
      requestType = com.example.grpc.RIdeService.HearthBeatRequest.class,
      responseType = com.example.grpc.RIdeService.HearthBeatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.RIdeService.HearthBeatRequest,
      com.example.grpc.RIdeService.HearthBeatResponse> getHearthBeatMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.RIdeService.HearthBeatRequest, com.example.grpc.RIdeService.HearthBeatResponse> getHearthBeatMethod;
    if ((getHearthBeatMethod = RideServiceGrpc.getHearthBeatMethod) == null) {
      synchronized (RideServiceGrpc.class) {
        if ((getHearthBeatMethod = RideServiceGrpc.getHearthBeatMethod) == null) {
          RideServiceGrpc.getHearthBeatMethod = getHearthBeatMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.RIdeService.HearthBeatRequest, com.example.grpc.RIdeService.HearthBeatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "hearthBeat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.HearthBeatRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.RIdeService.HearthBeatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RideServiceMethodDescriptorSupplier("hearthBeat"))
              .build();
        }
      }
    }
    return getHearthBeatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RideServiceStub newStub(io.grpc.Channel channel) {
    return new RideServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RideServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RideServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RideServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RideServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class RideServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void greetings(com.example.grpc.RIdeService.GreetingsRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.GreetingsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGreetingsMethod(), responseObserver);
    }

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

    /**
     */
    public void releaseRecharger(com.example.grpc.RIdeService.RechargeResponse request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RechargeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getReleaseRechargerMethod(), responseObserver);
    }

    /**
     */
    public void leavingSystem(com.example.grpc.RIdeService.ExitRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.ExitResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getLeavingSystemMethod(), responseObserver);
    }

    /**
     */
    public void hearthBeat(com.example.grpc.RIdeService.HearthBeatRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.HearthBeatResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getHearthBeatMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGreetingsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.RIdeService.GreetingsRequest,
                com.example.grpc.RIdeService.GreetingsResponse>(
                  this, METHODID_GREETINGS)))
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
          .addMethod(
            getReleaseRechargerMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.RIdeService.RechargeResponse,
                com.example.grpc.RIdeService.RechargeResponse>(
                  this, METHODID_RELEASE_RECHARGER)))
          .addMethod(
            getLeavingSystemMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.RIdeService.ExitRequest,
                com.example.grpc.RIdeService.ExitResponse>(
                  this, METHODID_LEAVING_SYSTEM)))
          .addMethod(
            getHearthBeatMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.grpc.RIdeService.HearthBeatRequest,
                com.example.grpc.RIdeService.HearthBeatResponse>(
                  this, METHODID_HEARTH_BEAT)))
          .build();
    }
  }

  /**
   */
  public static final class RideServiceStub extends io.grpc.stub.AbstractStub<RideServiceStub> {
    private RideServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RideServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RideServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RideServiceStub(channel, callOptions);
    }

    /**
     */
    public void greetings(com.example.grpc.RIdeService.GreetingsRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.GreetingsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGreetingsMethod(), getCallOptions()), request, responseObserver);
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

    /**
     */
    public void releaseRecharger(com.example.grpc.RIdeService.RechargeResponse request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RechargeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReleaseRechargerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void leavingSystem(com.example.grpc.RIdeService.ExitRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.ExitResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getLeavingSystemMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void hearthBeat(com.example.grpc.RIdeService.HearthBeatRequest request,
        io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.HearthBeatResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getHearthBeatMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RideServiceBlockingStub extends io.grpc.stub.AbstractStub<RideServiceBlockingStub> {
    private RideServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RideServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RideServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RideServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.grpc.RIdeService.GreetingsResponse greetings(com.example.grpc.RIdeService.GreetingsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGreetingsMethod(), getCallOptions(), request);
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

    /**
     */
    public com.example.grpc.RIdeService.RechargeResponse releaseRecharger(com.example.grpc.RIdeService.RechargeResponse request) {
      return blockingUnaryCall(
          getChannel(), getReleaseRechargerMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.grpc.RIdeService.ExitResponse leavingSystem(com.example.grpc.RIdeService.ExitRequest request) {
      return blockingUnaryCall(
          getChannel(), getLeavingSystemMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.grpc.RIdeService.HearthBeatResponse hearthBeat(com.example.grpc.RIdeService.HearthBeatRequest request) {
      return blockingUnaryCall(
          getChannel(), getHearthBeatMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RideServiceFutureStub extends io.grpc.stub.AbstractStub<RideServiceFutureStub> {
    private RideServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RideServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RideServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RideServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.RIdeService.GreetingsResponse> greetings(
        com.example.grpc.RIdeService.GreetingsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGreetingsMethod(), getCallOptions()), request);
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

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.RIdeService.RechargeResponse> releaseRecharger(
        com.example.grpc.RIdeService.RechargeResponse request) {
      return futureUnaryCall(
          getChannel().newCall(getReleaseRechargerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.RIdeService.ExitResponse> leavingSystem(
        com.example.grpc.RIdeService.ExitRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getLeavingSystemMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.RIdeService.HearthBeatResponse> hearthBeat(
        com.example.grpc.RIdeService.HearthBeatRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getHearthBeatMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GREETINGS = 0;
  private static final int METHODID_RESERVE_RIDE = 1;
  private static final int METHODID_CHANGE_DISTRICT = 2;
  private static final int METHODID_RESERVE_RECHARGER = 3;
  private static final int METHODID_RELEASE_RECHARGER = 4;
  private static final int METHODID_LEAVING_SYSTEM = 5;
  private static final int METHODID_HEARTH_BEAT = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RideServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RideServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GREETINGS:
          serviceImpl.greetings((com.example.grpc.RIdeService.GreetingsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.GreetingsResponse>) responseObserver);
          break;
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
        case METHODID_RELEASE_RECHARGER:
          serviceImpl.releaseRecharger((com.example.grpc.RIdeService.RechargeResponse) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.RechargeResponse>) responseObserver);
          break;
        case METHODID_LEAVING_SYSTEM:
          serviceImpl.leavingSystem((com.example.grpc.RIdeService.ExitRequest) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.ExitResponse>) responseObserver);
          break;
        case METHODID_HEARTH_BEAT:
          serviceImpl.hearthBeat((com.example.grpc.RIdeService.HearthBeatRequest) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.RIdeService.HearthBeatResponse>) responseObserver);
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

  private static abstract class RideServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RideServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.grpc.RIdeService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RideService");
    }
  }

  private static final class RideServiceFileDescriptorSupplier
      extends RideServiceBaseDescriptorSupplier {
    RideServiceFileDescriptorSupplier() {}
  }

  private static final class RideServiceMethodDescriptorSupplier
      extends RideServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RideServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (RideServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RideServiceFileDescriptorSupplier())
              .addMethod(getGreetingsMethod())
              .addMethod(getReserveRideMethod())
              .addMethod(getChangeDistrictMethod())
              .addMethod(getReserveRechargerMethod())
              .addMethod(getReleaseRechargerMethod())
              .addMethod(getLeavingSystemMethod())
              .addMethod(getHearthBeatMethod())
              .build();
        }
      }
    }
    return result;
  }
}
