package hello.advanced.trace.templatemethod;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.LogTrace;

/**
 * [Template Method Pattern (V4)]
 * Template Method Pattern에서 부모 클래스. Template의 역할을 한다.
 * - 제네릭을 활용하여 반환타입 정의
 * - call(): 비즈니스로직. 즉, 변하는 코드를 상속으로 구현해야한다.
 * - try/catch문 까지 Tempalte에서 처리할 수 있다.
 *
 * [V0, V3, V4 비교]
 * V0: 핵심 기능만 존재
 * V3: 핵심 기능과 부가 기능이 섞여있다.
 * V4: 핵심 기능과 템플릿을 호출하는 코드가 섞여있다.
 *
 * [좋은 설계?]
 * 좋은 설계는 변경이 일어날 때 드러난다.
 * - 로그 로직을 모듈화하여 비즈니스 로직과 분리하였다.
 * - 만약 로그 로직을 변경해야한다면, 로그 로직인 AbstractTemplate만 변경하면 된다.
 * - 만약 Template이 없는 설계에서 로그를 남기는 로직을 변경해야한다면,
 *   로그 로직이 적용되어 있는 모든 클래스를 찾아 수정해야한다.
 *   = 단일 책임 원칙 (SRP)
 *
 * [Template Method Pattern?]
 * 상속과 오버라이딩을 이용한 다형성을 활용.
 * 부모 클래스에 알고리즘의 골격 Template을 정의.
 * 일부 변경되는 로직은 자식 클래스에 정의.
 * - 자식 클래스가 로직의 특정 부분만을 재정의하여 사용할 수 있다. 로직을 변경할 때에 알고리즘의 전체 구조를 변경하지 않아도 된다.
 *
 * [ISSUE - 상속의 한계]
 * - 상속은 특정 부모 클래스에 의존하는 것.
 *   - 자식 클래스에 `extends 부모클래스` 명시해야 함.
 *   - 부모클래스를 기능 사용 여부와 관계 없이 부모 클래스에 강하게 의존. 자식클래스는 부모클래스에 대해 알고 있어야한다.
 *   - 부모클래스를 수정하면 자식 클래스에도 영향을 줄 수 있다.
 * - 상속 구조를 사용하기 때문에, 별도의 클래스나 익명 내부 클래스를 만들어야한다.
 *   => 전략 패턴
 *
 * @param <T>
 */
public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            // 비즈니스로직 호출
            T result = call();  // 변하는 코드. 핵심로직. 비즈니스로직
            // 비즈니스로직 끝

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    // 제네릭을 활용하여 return 타입 정의
    protected abstract T call();
}
