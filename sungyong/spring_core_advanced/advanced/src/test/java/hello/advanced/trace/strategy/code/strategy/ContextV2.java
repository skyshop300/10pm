package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * [Strategy Pattern 2 - 전략을 파라미터로 전달 받는 방식]
 * - 실핼할 때마다 전략을 계속 지정해줘야한다.
 * - Context를 실행할 때마다 Strategy를 파라미터로 전달.
 * - 코드 조각을 파라미터로 넘긴다고 생각하자.
 *
 * 파라미터 전달 방식 과정
 * 1. 클라이언트는 Context를 실행시키면서 Strategy를 전달한다.
 * 2. Context는 context.execute()로직을 실행.
 * 3. context 파라미터로 넘어온 strategy.call() 로직을 실행.
 * 4. context의 execute() 로직이 종료됨.
 *
 * [템플릿]
 * - 현재 적용 전략은 변하는/불변하는 부분을 분리하는것.
 * - 변하지 않는 템플릿, 그 템플릿 안에서 원하는 부분만 살짝 다른 코드를 실행하고자 함.
 *  => ContextV2: 실행 시점에 유연하게 실행 코드 조각을 전달하는 ContextV2가 더 적합하다.
 */
@Slf4j
public class ContextV2 {
    public void execute(Strategy strategy) {

        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        strategy.call();    // 위임
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);

    }
}
