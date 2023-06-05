package org.demo.controller;


import org.apache.dubbo.config.annotation.DubboReference;
import org.demo.proto.HelloRequest;
import org.demo.proto.HelloResponse;
import org.demo.proto.ProducerService;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping(value = "index")
public class HelloController {
    @DubboReference
    private ProducerService producerService;

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String sayHello(@RequestParam String name) {
        try {
            HelloRequest req = HelloRequest.newBuilder().setName(name).build();
            HelloResponse resp = producerService.hello(
                    req
            );

            return resp.getResult();
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
