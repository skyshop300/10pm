package hello.advanced.trace.templatecallback;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceStatus;

/**
 * templatemethod.AbstractTemplate와 유사
 */
public class TraceTemplate {
    private final LogTrace trace;

    public TraceTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public <T> T execute(String message, TraceCallback<T> callback) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            // 비즈니스로직 호출
            T result = callback.call();  // 변하는 코드. 핵심로직. 비즈니스로직
            // 비즈니스로직 끝

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    // Template Method Pattern에서 사용
    // 제네릭을 활용하여 return 타입 정의
//    protected abstract T call();
}
