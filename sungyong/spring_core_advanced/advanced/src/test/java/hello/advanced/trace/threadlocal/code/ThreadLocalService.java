package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

/**
 * [ThreadLocal - 동시성 문제 예제]
 * ThreadLocal 적용
 * - nameStore 필드가 일반 String 타입이 아닌 ThreadLocal을 사용.
 * | ThreadLocal을 사용하고 나면 ThreadLocal.remove()로 ThreadLocal에 저장되어 있는 값을 제거해줘야한다.
 */
@Slf4j
public class ThreadLocalService {

    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
