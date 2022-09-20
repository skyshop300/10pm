package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * [V3: Field, ThreadLocal 방식 적용]
 */
@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;
    private final LogTrace trace;

    @GetMapping("/v3/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderControllerV3.request()");    // 예외가 발생할 수 있기 때문에 try/catch문 내부에 넣음.
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            // 애플리케이션의 흐름을 변경하면 안됨 ->  예외가 발생하면 예외를 던지도록 해야한다. 예외가 발생하면 정상 흐름을 탈 수 없도록해야 함.
            trace.exception(status, e); // 예외를 먹어버림.
            throw e;    // 예외를 꼭 던져줘야한다.
        }
    }
}

