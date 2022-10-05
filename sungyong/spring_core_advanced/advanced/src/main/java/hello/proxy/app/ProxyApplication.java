package hello.proxy.app;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Import: 클래스를 Spring Bean으로 등록함.
 * - 일반적으로 @Configuration 같은 설정 파일을 등록할 때 사용.
 * @SpringBootApplication(scanBasePackages = "hello.proxy.app")
 * - @ComponentScan의 기능과 동일하다.
 *      - 컴포넌트 스캔을 시작할 위치를 정한다. 해당 패키지를 포함한 하위 패키지를 스캔한다.
 *      - ++ 점진적으로 Bean 등록 방식을 변경할 예정
 * - @Configuration은 내부에 @Component를 포함하고 있기에 ComponentScan의 대상이 된다.
 *      - 컴포넌트 스캔에 의해 `hello.proxy.config` 위치의 설정 파일들이 스프링 빈으로 자동 등록되지 않도록
 *        컴포넌트 스캔의 시작위치를 `scanBasePackages=hello.proxy.app`으로 설정해야한다.
 *        `hello.proxy.config` X / `hello.proxy.app` O
 */
@Import({AppV1Config.class, AppV2Config.class})
@SpringBootApplication(scanBasePackages = "hello.proxy.app")
public class ProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }
}