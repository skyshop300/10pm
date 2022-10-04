package hello.advanced.app.v5;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.templatecallback.TraceTemplate;
import org.springframework.stereotype.Service;
@Service
//@RequiredArgsConstructor
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate traceTemplate;

    // TraceTemplate Bean 등록: TraceTemplate을 Bean으로 등록 + @RequiredArgsConstructor와 조합하여 사용하거나,
    // TraceTemplate Bean 미등록: 아래의 생성자를 생성하여 사용
    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.traceTemplate = new TraceTemplate(trace);
    }

    public void orderItem(String itemId) {
        // [TIP]: 제네릭에서 반환할 내용이 없다면 Void타입을 설정한다.
//        AbstractTemplate<Void> template = new AbstractTemplate<>(template) {
//            @Override
//            protected Void call() {
//                orderRepository.save(itemId);
//                return null;
//            }
//        };
        traceTemplate.execute("OrderServiceV5.request()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}
