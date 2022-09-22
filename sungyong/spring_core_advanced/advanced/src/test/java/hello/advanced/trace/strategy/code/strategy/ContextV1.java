package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO: Template Method Pattern and Callback - 24
 * [Strategy Pattern - 필드에 전략을 보관하는 방식]
 * ContextV1은 변하지 않는 로직을 가지고 있는 Template 역할을 수행.
 * - 전략패턴에서는 이러한 Template을 Context(문맥)라고 한다.
 * - Context(문맥)은 변하지 않지만 context 내부에서 Strategy(전략)의 구현을 통해 일부 전략이 변경되는 패턴이다.
 *
 * [Context의 구조]
 * Context 내부에는 Strategy strategy 필드가 존재.
 * Strategy에 구현체를 주입하여 사용한다.
 *
 * [Strategy 핵심]
 * Context는 Strategy의 인터페이스만 의존한다.
 * 즉, 어떤 Strategy가 사용될지는 신경 쓰지 않는다. Context에서는 구현체가 무엇인지 정하지 않는다.
 * - 구현체를 외부에서 주입받아서 사용한다.
 * - Strategy의 구현체를 변경하거나 새로 만들어도 Context 코드에는 영향을 주지 않는다.
 * - == 스프링에서의존관계 주입에서 사용하는 방식
 *
 * [Context 작동원리]
 * 1) Clinet 코드에서 Context에 원하는 Strategy 구현체 주입 // 생성자 주입
 * 2) Client가 Context 실행
 * 3) context 로직 시작
 * 4) context 로직 중간에 strategy.call()을 호출해서 주입받은 strategy 로직을 실행한다.
 *
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;      // 전략

    public ContextV1(Strategy strategy) {   // 생성자를 통해 strategy를 주입 받는다.
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        strategy.call();    // 위임
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }

}
