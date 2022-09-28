package hello.advanced.app.v5;

import hello.advanced.trace.templatecallback.TraceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [V5: Template Callback Pattern적용]
 */
@RestController
@RequiredArgsConstructor
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    @GetMapping("/v5/request")
    public String request(String itemId) {

        // 비즈니스 로직 구현
        return template.execute("OrderControllerV5.request()", () -> {
            orderService.orderItem(itemId);
            return "ok";
        });
    }
}