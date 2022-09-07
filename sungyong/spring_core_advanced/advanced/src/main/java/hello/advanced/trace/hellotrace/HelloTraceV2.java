package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * [V1]
 * - 실제 로그를 시작하고 종료할 수 있다.
 * - 로그를 출력하고 실행시간을 측정할 수 있다.
 * [V2]
 * - 메서드 호출의 깊이 표현
 * - HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 구분
 * ! 하지만 V2에도 한계가 있다.
 *   - HTTP 요청 구분 및 깊이 표현을 위해 TraceId 동기화 필요 (beginSync 메소드)
 *   - TraceId의 동기화를 위해 관련 메서드 파라미터 수정 필요
 *      - 인터페이스가 존재한다면 인터페이스도 수정 필요
 *   - 최초 로그 시작 시 begin(), 이후 beginSync() 호출 필요
 *      - Controller가 아닌 다른 곳에서 Service를 첫 호출하면, 전달할 TraceId가 없음.
 * - TraceId의 대안은 무엇이 있을까?
 */
@Slf4j
@Component  // 스프링 빈으로 등록. 싱글톤으로 사용.
public class HelloTraceV2 {
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    /**
     * [V2 추가 코드]
     * - 기존 TraceId에서 createNextId() 를 통해 다음의 ID를 구한다.
     * - createNextId() - TraceId 생성로직
     *   - 트랜잭션ID는 기존과 동일 유지
     *   - 깊이를 표현하는 Level을 1 증가시킨다.
     */
    public TraceStatus beginSync(TraceId beforeTraceId, String message) {
        TraceId nextId = beforeTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[" + nextId.getId() + "] " + addSpace(START_PREFIX, nextId.getLevel()) + message);
        return new TraceStatus(nextId,startTimeMs, message);
    }

    public void end(TraceStatus status) {
        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete (TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {    // 정상 동작 시 처리
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs);
        } else {    // 오류 발생 시 처리
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs,
                    e.toString());
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

}
