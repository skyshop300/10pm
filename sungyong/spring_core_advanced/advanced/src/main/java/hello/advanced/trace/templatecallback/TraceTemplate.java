package hello.advanced.trace.templatecallback;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceStatus;

/**
 * Template 역할 수행
 * templatemethod.AbstractTemplate와 유사
 * - Template Method Pattern
 * - Strategy Pattern
 * - Template Callback Pattern
 *
 * [Issue]
 * 로그추적기를 적용하기 위해서는 원본 코드를 수정해야한다.
 * - 클래스가 수백개면, 수백개를 수정해야한다.
 * - 원본코드를 수정하지 않고 로그 추적기를 사용해야한다.
 * => 프록시패턴 사용
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
