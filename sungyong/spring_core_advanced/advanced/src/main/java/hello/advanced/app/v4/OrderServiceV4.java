package hello.advanced.app.v4;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.templatemethod.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {
        // [TIP]: 제네릭에서 반환할 내용이 없다면 Void타입을 설정한다.
        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
            @Override
            protected Void call() {
                orderRepository.save(itemId);
                return null;
            }
        };
        template.execute("OrderServiceV4.orderItem()");
    }
}
