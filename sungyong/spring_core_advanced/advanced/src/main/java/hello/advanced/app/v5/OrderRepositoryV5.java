package hello.advanced.app.v5;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.templatecallback.TraceTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate traceTemplate;

    public OrderRepositoryV5 (LogTrace trace) {
        this.traceTemplate = new TraceTemplate(trace);
    }

    public void save(String itemId) {
        traceTemplate.execute("OrderRepositoryV5.save()", () -> {
            if(itemId.equals("ex")) {
                throw new IllegalStateException();
            }
            sleep(1000);
            return null;
        });
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}