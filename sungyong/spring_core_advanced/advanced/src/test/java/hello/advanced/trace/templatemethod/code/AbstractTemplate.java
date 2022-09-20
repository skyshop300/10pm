package hello.advanced.trace.templatemethod.code;

import lombok.extern.slf4j.Slf4j;

/**
 * [템플릿메소드의 기본 구조 - 추상 클래스]
 * 부모 클래스
 * 변하는 부분은 자식 클래스에 두고 Overrding하여 사용한다.
 */
@Slf4j
public abstract class AbstractTemplate {

    // 불변하는 코드. 부가기능. 횡단관심
    public void execute() {
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        call(); // 상속 수행
        // 비즈니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = startTime - endTime;
        log.info("resultTime={}", resultTime);
    }
    // 변하는 코드. 핵심기능. 비즈니스로직
    protected abstract void call();
}
