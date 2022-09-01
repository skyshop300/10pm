package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace;

    @GetMapping("/v1/request")
    public String request(String itemId) {
//        orderService.orderItem(itemId);
//        trace.end(status);
        TraceStatus status = null;
        try {
            status = trace.begin("OrderControllerV1.request()");    // 예외가 발생할 수 있기 때문에 try/catch문 내부에 넣음.
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            // 애플리케이션의 흐름을 변경하면 안됨 ->  예외가 발생하면 예외를 던지도록 해야한다. 예외가 발생하면 정상 흐름을 탈 수 없도록해야 함.
            trace.exception(status, e); // 예외를 먹어버림.
            throw e;
        }
    }
}

