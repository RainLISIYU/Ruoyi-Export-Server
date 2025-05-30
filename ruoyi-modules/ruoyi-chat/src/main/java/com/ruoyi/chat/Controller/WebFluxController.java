package com.ruoyi.chat.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

/**
 * @author lsy
 * @description webflux控制器
 * @date 2025/5/19
 */
@Controller
@RequestMapping("/api/flux")
public class WebFluxController {

    @GetMapping("/test")
    @ResponseBody
    public Flux<String> test() {
        return Flux.fromStream(Stream.of("Tom", "jerry", "sofia", "joker"));
    }

}
