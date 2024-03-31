package vistar.practice.demo.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Before("vistar.practice.demo.aspects.CommonPointcuts.isMapper()")
    public void addLoggingToMappersBefore (JoinPoint joinPoint) {
        log.info("input data in {} object: {}",joinPoint.getTarget(), joinPoint.getArgs());
    }

    @AfterReturning(value = "vistar.practice.demo.aspects.CommonPointcuts.isMapper()", returning = "result")
    public void addLoggingToMappersAfterReturning (JoinPoint joinPoint, Object result) {
        log.info("output data from {} object: {}", joinPoint.getTarget(), result);
    }

    @AfterThrowing(value = "vistar.practice.demo.aspects.CommonPointcuts.isMapper()", throwing = "ex")
    public void addLoggingToMappersAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.info("Exception {} in {} object", ex, joinPoint.getTarget());
    }
}
