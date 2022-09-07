package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class HelloTraceV2Test {
    @Test
    void begin_end_level2() {
        HelloTraceV2 trace = new HelloTraceV2();
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception_level2() {
        HelloTraceV2 trace = new HelloTraceV2();
        TraceStatus status1 = trace.begin("hello");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.exception(status2, new IllegalAccessException());
        trace.exception(status2, new IllegalAccessException());
    }

    /**
     * [a75a98a3] hello
     * [a75a98a3] |-->hello2
     * [a75a98a3] |<X-hello2 time=3ms ex=java.lang.IllegalAccessException
     * [a75a98a3] |<X-hello2 time=3ms ex=java.lang.IllegalAccessException
     * [51cbc958] hello1
     * [51cbc958] |-->hello2
     * [51cbc958] |<--hello2 time=0ms
     * [51cbc958] hello1 time=0ms
     */
}
