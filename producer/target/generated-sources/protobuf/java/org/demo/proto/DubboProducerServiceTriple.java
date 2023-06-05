/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

    package org.demo.proto;

import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.PathResolver;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.ServerService;
import org.apache.dubbo.rpc.TriRpcStatus;
import org.apache.dubbo.rpc.model.MethodDescriptor;
import org.apache.dubbo.rpc.model.ServiceDescriptor;
import org.apache.dubbo.rpc.model.StubMethodDescriptor;
import org.apache.dubbo.rpc.model.StubServiceDescriptor;
import org.apache.dubbo.rpc.stub.BiStreamMethodHandler;
import org.apache.dubbo.rpc.stub.ServerStreamMethodHandler;
import org.apache.dubbo.rpc.stub.StubInvocationUtil;
import org.apache.dubbo.rpc.stub.StubInvoker;
import org.apache.dubbo.rpc.stub.StubMethodHandler;
import org.apache.dubbo.rpc.stub.StubSuppliers;
import org.apache.dubbo.rpc.stub.UnaryStubMethodHandler;

import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.concurrent.CompletableFuture;

public final class DubboProducerServiceTriple {

    public static final String SERVICE_NAME = ProducerService.SERVICE_NAME;

    private static final StubServiceDescriptor serviceDescriptor = new StubServiceDescriptor(SERVICE_NAME,ProducerService.class);

    static {
        org.apache.dubbo.rpc.protocol.tri.service.SchemaDescriptorRegistry.addSchemaDescriptor(SERVICE_NAME,ProducerServiceProto.getDescriptor());
        StubSuppliers.addSupplier(SERVICE_NAME, DubboProducerServiceTriple::newStub);
        StubSuppliers.addSupplier(ProducerService.JAVA_SERVICE_NAME,  DubboProducerServiceTriple::newStub);
        StubSuppliers.addDescriptor(SERVICE_NAME, serviceDescriptor);
        StubSuppliers.addDescriptor(ProducerService.JAVA_SERVICE_NAME, serviceDescriptor);
    }

    @SuppressWarnings("all")
    public static ProducerService newStub(Invoker<?> invoker) {
        return new ProducerServiceStub((Invoker<ProducerService>)invoker);
    }

    private static final StubMethodDescriptor helloMethod = new StubMethodDescriptor("Hello",
    org.demo.proto.HelloRequest.class, org.demo.proto.HelloResponse.class, serviceDescriptor, MethodDescriptor.RpcType.UNARY,
    obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(), org.demo.proto.HelloRequest::parseFrom,
    org.demo.proto.HelloResponse::parseFrom);




    public static class ProducerServiceStub implements ProducerService{
        private final Invoker<ProducerService> invoker;

        public ProducerServiceStub(Invoker<ProducerService> invoker) {
            this.invoker = invoker;
        }

        @Override
        public org.demo.proto.HelloResponse hello(org.demo.proto.HelloRequest request){
            return StubInvocationUtil.unaryCall(invoker, helloMethod, request);
        }

        @Override
        public void hello(org.demo.proto.HelloRequest request, StreamObserver<org.demo.proto.HelloResponse> responseObserver){
            StubInvocationUtil.unaryCall(invoker, helloMethod , request, responseObserver);
        }



    }

    public static abstract class ProducerServiceImplBase implements ProducerService, ServerService<ProducerService> {

        @Override
        public final Invoker<ProducerService> getInvoker(URL url) {
            PathResolver pathResolver = url.getOrDefaultFrameworkModel()
            .getExtensionLoader(PathResolver.class)
            .getDefaultExtension();
            Map<String,StubMethodHandler<?, ?>> handlers = new HashMap<>();

            pathResolver.addNativeStub( "/" + SERVICE_NAME + "/Hello" );

            BiConsumer<org.demo.proto.HelloRequest, StreamObserver<org.demo.proto.HelloResponse>> helloFunc = this::hello;
            handlers.put(helloMethod.getMethodName(), new UnaryStubMethodHandler<>(helloFunc));




            return new StubInvoker<>(this, url, ProducerService.class, handlers);
        }


        @Override
        public org.demo.proto.HelloResponse hello(org.demo.proto.HelloRequest request){
            throw unimplementedMethodException(helloMethod);
        }





        @Override
        public final ServiceDescriptor getServiceDescriptor() {
            return serviceDescriptor;
        }
        private RpcException unimplementedMethodException(StubMethodDescriptor methodDescriptor) {
            return TriRpcStatus.UNIMPLEMENTED.withDescription(String.format("Method %s is unimplemented",
                "/" + serviceDescriptor.getInterfaceName() + "/" + methodDescriptor.getMethodName())).asException();
        }
    }

}
