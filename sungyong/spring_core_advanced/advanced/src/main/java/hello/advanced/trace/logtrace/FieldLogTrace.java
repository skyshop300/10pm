package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * [FieldLogTrace]
 * HelloTraceV2의 다음 버전.
 * HelloTrace와 다르게 traceId를 파라미터를 통한 전달이 아닌 traceIdHolder `Field`에 저장하여 사용.
 * - traceHolder를 이용하여 파라미터가 필요하지 않다.
 *      - 불필요하게 TraceId를 파라미터로 전달하지 않아도 된다.
 * - 애플리케이션의 메서드 파라미터도 변경하지 않아도 된다.
 * [ISSUE] 
 * 동시성 이슈를 가지고 있다.
 * FieldLogTrace는 싱글톤으로 등록된 Spring Bean이다.
 * 따라서 이 객체의 인스턴스는 애플리케이션에 1개만 존재한다.
 * 이렇게 하나만 있는 인스턴스의 FieldLogTrace.traceIdHolder Field를 여러 쓰레드가 동시에 접근하기 때문에 문제가 발생한다.
 * - 아래의 로직을 1초에 2회 호출 시 다음과 같이 결과가 출력된다.
 * ```
 * [ae4d57a6] OrderControllerV3.request()
 * [ae4d57a6] |-->OrderServiceV3.orderItem()
 * [ae4d57a6] |   |-->OrderRepositoryV3.save()
 * [ae4d57a6] |   |   |-->OrderControllerV3.request()
 * [ae4d57a6] |   |   |   |-->OrderServiceV3.orderItem()
 * [ae4d57a6] |   |   |   |   |-->OrderRepositoryV3.save()
 * [ae4d57a6] |   |<--OrderRepositoryV3.save() time=1005ms
 * [ae4d57a6] |<--OrderServiceV3.orderItem() time=1009ms
 * [ae4d57a6] OrderControllerV3.request() time=1010ms
 * [ae4d57a6] |   |   |   |   |<--OrderRepositoryV3.save() time=1004ms
 * [ae4d57a6] |   |   |   |<--OrderServiceV3.orderItem() time=1005ms
 * [ae4d57a6] |   |   |<--OrderControllerV3.request() time=1006ms
 * ```
 *
 */
@Slf4j
public class FieldLogTrace implements LogTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";
    // traceId를 파라미터를 통한 전달이 아닌 traceIdHolder Field에 저장하여 사용.
    private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder;
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
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
     * 로그를 시작할 때 호출
     */
    private void syncTraceId() {
        if (traceIdHolder == null) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    /**
     * 로그를 끝낼 때 호출
     */
    private void releaseTraceId() {
        if(traceIdHolder.isFirstLevel()) {
            traceIdHolder =  null;  // destroy
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
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
