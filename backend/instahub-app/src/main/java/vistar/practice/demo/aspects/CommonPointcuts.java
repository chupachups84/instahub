package vistar.practice.demo.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CommonPointcuts {
    @Pointcut("within(vistar.practice.demo.mappers.*Impl)")
    public void isMapper(){
    }
}
