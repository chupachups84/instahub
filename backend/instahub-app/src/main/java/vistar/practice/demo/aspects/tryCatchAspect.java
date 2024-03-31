package vistar.practice.demo.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;



@Component
@Aspect
@Slf4j
public class tryCatchAspect {

    @Around("vistar.practice.demo.aspects.CommonPointcuts.isMapper()")
    public Object addTryCatch(ProceedingJoinPoint joinPoint){
        try {
            return joinPoint.proceed();
        }
        catch (Throwable ex){
            throw new RuntimeException(ex);
        }
    }
}
