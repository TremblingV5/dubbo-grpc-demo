package org.demo.handler;

import org.apache.dubbo.config.annotation.DubboService;
import org.demo.proto.HelloRequest;
import org.demo.proto.HelloResponse;
import org.demo.proto.ProducerService;
@DubboService
public class ProducerServiceImpl implements ProducerService {

    @Override
    public HelloResponse hello(HelloRequest request) {
        HelloResponse resp = HelloResponse.newBuilder().setResult("Hello " + request.getName()).build();
        return resp;
    }
}
