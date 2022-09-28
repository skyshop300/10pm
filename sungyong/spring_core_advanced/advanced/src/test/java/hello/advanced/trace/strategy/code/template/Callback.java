package hello.advanced.trace.strategy.code.template;

/**
 * [Template Callback Pattern]
 * ContextV2와 같은 전략패턴을 Template Callback Pattern이라고 한다.
 *  - Context: Template 역할
 *  - Strategy: Callback 역할
 *
 * Template Callback Pattern 이름
 * GOF 패턴은 아니지만, Spring에서 많이 사용되기 때문에 Spring 내에서만 이렇게 칭한다.
 * - 전략패턴에서 Template과 Callback이 강조된 패턴이다.
 *
 * Spring에서의 예시
 * - xxxTemplate은 대개 TemplateCallback Pattern으로 구성
 * - ex) JdbcTemplate RestTemplate, TransacitonTemplate, RedisTemplate
 * - ++) 참고 30, 33
 *
 * [Callback]
 * ContextV2는 변하지 않는 템플릿 역할.
 * Strategy는 변하는 부분이며 파라미터로 넘온다. 이는 Callback이다.
 *
 * Callback: 다른 코드의 파라미터로서 넘겨주는 실행 가능한 코드
 * - Call: 코드가 호출(call)은 된다.
 * - Back: 그런데 코드를 넘겨준 다음(back)에 실행
 *
 * Strategy 패턴에서의 Callback
 * - Strategy가 Callback이다.
 * - Client에서 Strategy를 직접 실행하는 것이 아님.
 * - Client가 ContextV2.execute()를 실행할 때 Strategy를 넘김
 * - ContextV2 뒤(back)에서 실행된다.
 *
 * Java에서의 콜백
 * 자바에서 실행가능한 코드를 파라미터로 넘기기 위해서는 객체가 필요.
 * - Java8 이후: Lambda Expression 사용
 * - Java8 이전: `1개`의 메소드를 가진 인터페이스 구현. 익명 클래스 사용
 *
 */
public interface Callback {
    void call();
}
