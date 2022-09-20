package hello.advanced.trace.templatemethod.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClassLogic1 extends AbstractTemplate {

    // 변하는 코드. 핵심기능. 비즈니스로직을 Override 재정의해서 사용
    @Override
    protected void call() {
        log.info("비즈니스 로직 수행1");
    }

}
