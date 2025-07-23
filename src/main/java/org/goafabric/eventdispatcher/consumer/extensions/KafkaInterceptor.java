package org.goafabric.eventdispatcher.consumer.extensions;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.goafabric.event.EventData;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.MDC;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RegisterReflection(classes = KafkaInterceptor.class, memberCategories = MemberCategory.INVOKE_DECLARED_METHODS)
public class KafkaInterceptor {

    @Around("@annotation(kafkaListener)")
    public Object resolveTenantInfo(ProceedingJoinPoint joinPoint, KafkaListener kafkaListener) throws Throwable {
        EventData eventData = (EventData) joinPoint.getArgs()[1];
        UserContext.setContext(eventData.tenantInfos());

        configureLogsAndTracing();
        Object result = joinPoint.proceed();
        afterCompletion();

        return result;
    }

    private static void configureLogsAndTracing() {
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.getTenantId());
        MDC.put("tenantId", UserContext.getTenantId());
    }

    private static void afterCompletion() {
        UserContext.removeContext();
        MDC.remove("tenantId");
    }


}
