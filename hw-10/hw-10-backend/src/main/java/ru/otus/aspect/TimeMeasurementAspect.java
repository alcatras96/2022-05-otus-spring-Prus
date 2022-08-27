package ru.otus.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ru.otus.service.io.api.OutputService;

@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${time.measurement.aspect.enabled}")
public class TimeMeasurementAspect {

    private final OutputService<String> outputService;

    @Around("@annotation(ru.otus.annotation.LogExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        var stopWatch = new StopWatch();
        stopWatch.start();
        var proceed = joinPoint.proceed();
        stopWatch.stop();
        outputService.output(joinPoint.getSignature() + " executed in " + stopWatch.getLastTaskTimeMillis() + "ms");
        return proceed;
    }
}
