package trysome.spring.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessorOrder;

/**
 * 在使用AOP时，要注意到虽然Spring容器可以把指定的方法通过AOP规则装配到指定的Bean的指定方法前后，
 * 但是，如果自动装配时，因为不恰当的范围，容易导致意想不到的结果，
 * 即很多不需要AOP代理的Bean也被自动代理了，并且，
 * 后续新增的Bean，如果不清楚现有的AOP装配规则，容易被强迫装配。
 */
@Aspect
@Component
public class MetricAspect {
    @Around("@annotation(metricTime)")
    public Object metric(ProceedingJoinPoint joinPoint,MetricTime metricTime) throws Throwable{
        String name = metricTime.value();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        }finally {
            long t = System.currentTimeMillis() - start;
            // 写入日志或发送至JMX:
            System.err.println("[Metrics] " + name +" : " + t + " ms");
        }
    }
}
