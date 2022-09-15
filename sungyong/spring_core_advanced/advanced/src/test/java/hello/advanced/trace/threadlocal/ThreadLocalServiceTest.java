package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * [ThreadLocal - 동시성 문제 예제]
 * ThreadLocalService test를 위한 test 코드
 * - ThreadLocal 별로 별도의 데이터 저장소를 가지게 되어서 동시성 문제가 해결되었다.
 *
 * [ThreadLocal로 사용으로 인한 동시성 이슈 미발생]
 * - sleep(100) 적용
 * ```
 * [Test worker]- main start
 * [thread-A] name=userA -> nameStore=null
 * [thread-B] name=userB -> nameStore=null      // userA가 저장한 데이터가 오염되지 않는다.
 * [thread-A] nameStore=userA
 * [thread-B] nameStore=userB
 * [Test worker]- main exit
 * ```
 * [동시성 문제의 해결책]
 * ThreadLocal
 */
@Slf4j
public class ThreadLocalServiceTest {
    private ThreadLocalService threadLocalService = new ThreadLocalService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> {
            threadLocalService.logic("userA");
        };
        Runnable userB = () -> {
            threadLocalService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB =  new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();        // A 실행
//        sleep(2000);      // 동시성 문제 발생 X
        sleep(100);       // 동시성 문제 발생 O
        threadB.start();        // B 실행

        sleep(3000);      // 메인 스레드 종료 대기
        log.info("main exit");

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
