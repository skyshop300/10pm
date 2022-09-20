package hello.advanced.trace.logtrace;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * [ThreadLocal (V3)]
 * HelloTraceV2의 다음 버전.
 * HelloTrace와 다르게 traceId를 파라미터를 통한 전달이 아닌 traceIdHolder `Field`에 저장하여 사용.
 * - traceHolder를 이용하여 파라미터가 필요하지 않다.
 *      - 불필요하게 TraceId를 파라미터로 전달하지 않아도 된다.
 * - 애플리케이션의 메서드 파라미터도 변경하지 않아도 된다.
 *
 * [ISSUE1 - ThreadLocal.remove()의 필요성.]
 * 쓰레드 로컬의 값을 사용하고 제거하지 않고 그냥 두면 WAS처럼 `쓰레드풀`을 사용할 경우 이슈가 발생할 수 있다.
 * 보통 WAS는 쓰레드를 제거하지 않고, 쓰레드 풀을 통해 쓰레드를 재사용한다. 이때 사용한 쓰레드는 제거되지 않고 쓰레드풀에 살아있을 수 있다.
 * => 따라서 ThreadLocal의 값을 ThreadLocal.remove()를 통해 꼭 제거해줘야한다.
 * ex)
 * 이때 UserA가 HTTP요청을 통해 사용한 쓰레드가 쓰레드풀에 살아있을 경우, UserB가 HTTP 요청을 통해 동일한 쓰레드가 할당되었다고 가정하자.
 * 이때 데이터의 조회를 수행하면 UserA의 데이터가 반환된다.
 * 결과적으로 UserB는 UserA의 데이터를 조회하게 된다.
 *
 * [ISSUE2 - 부가기능 코드가 핵심기능 코드에 추가 됨]
 * 로그 추적기를 입하는 동안 핵심기능 이외의 부가기능 코드가 추가되었다.
 * - 코드 중복
 * - 단일책임원칙 위배
 * => 변하는 것과 변하지 않는 것을 분리하여 모듈화한다. 템플릿메서드패턴
 */
@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";


    // traceId를 ThreadLocal에 저장
//    private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>(); // traceId 동기화, 동시성 이슈 발생

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    /**
     * 로그를 시작할 때 호출
     */
    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        }
        releaseTraceId();
    }

    /**
     * 로그를 끝낼 때 호출
     */
    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if(traceId.isFirstLevel()) {
            traceIdHolder.remove();  // destroy // threadLocal을 사용한 후에는 threadLocal애 저장되어 있는 값을 제거해줘야한다.
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
