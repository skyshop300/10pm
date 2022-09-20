package hello.advanced.trace.templatemethod;

import hello.advanced.trace.templatemethod.code.AbstractTemplate;
import hello.advanced.trace.templatemethod.code.SubClassLogic1;
import hello.advanced.trace.templatemethod.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * [템플릿메소드의 예제 코드]
 * 템플릿메소드 패턴: 이와 같이 다형성을 사용하여 변하는 부분과 불변하는 부분을 분리하는 방법.
 */
@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직
        log.info("비즈니스 로직1 실행");    // 변하는 코드. 핵심기능. 비즈니스로직.
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직
        log.info("비즈니스 로직2 실행");    // 변하는 코드. 핵심기능. 비즈니스로직.
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /**
     *  [Template Method Pattern 호출]
     *  template1.execute()
     *  -> AbstractTemplate.execute()
     *  -> call() // SubClassLogicN이 호출 된다. Overriding.
     */
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }
}
