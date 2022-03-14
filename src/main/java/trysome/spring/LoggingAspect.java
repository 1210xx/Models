package trysome.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    //在UseService的每个public方法前doAccessCheck
    @Before("execution(public * trysome.spring.service.UserService.*(..))")
    public void doAccessCheck(){
      System.err.println("[Before] do access check....");
    }

    //Around可以决定是否执行目标方法
    //在执行MailService的每个方法前后执行:
    @Around("execution(public * trysome.spring.service.MailService.*(..))")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable{
      System.err.println("[Around] start " + pjp.getSignature());
      //执行方法
      Object retVal = pjp.proceed();
      System.err.println("[Around] done " + pjp.getSignature());
      return retVal;
    }
}
