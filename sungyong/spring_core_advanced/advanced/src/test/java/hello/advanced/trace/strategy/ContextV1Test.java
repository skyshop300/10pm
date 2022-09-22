package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.StrategyLogic1;
import hello.advanced.trace.strategy.code.StrategyLogic2;
import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * TODO: Template Method Pattern and Callback - 26
 */
@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    private void logic1() {
    }

    private void logic2() {
    }

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1() {
        Strategy strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1);    // 생성자를 통해 stategy를 주입한다.
        context1.execute();

        Strategy strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);    // 생성자를 통해 stategy를 주입한다.
        context2.execute();
    }

    /**
     * 전략 패턴 익명 내부 클래스
     */
    @Test
    void strategyV2() {
        Strategy strategyLogic1 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("strategyLogic1={}", strategyLogic1.getClass());
        ContextV1 context_1 = new ContextV1(strategyLogic1);
        context_1.execute();

        Strategy strategyLogic2 = new StrategyLogic2() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        log.info("strategyLogic2={}", strategyLogic2.getClass());
        ContextV1 context_2 = new ContextV1(strategyLogic2);
        context_2.execute();
    }

    /**
     * 전략 패턴 익명 내부 클래스
     */
    @Test
    void strategyV4() {
        ContextV1 context_1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context_1.execute();

//        ContextV1 context_1 = new ContextV1(new Strategy() {
//            @Override
//            public void call() {
//                log.info("비즈니스 로직1 실행")
//            }
//        });
    }
}
