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
@RegisterReflection(classes = KafkaListenerInterceptor.class, memberCategories = MemberCategory.INVOKE_DECLARED_METHODS)
public class KafkaListenerInterceptor {

    @Around("@annotation(kafkaListener) && args(..,eventData)")
    public Object resolveTenantInfo(ProceedingJoinPoint joinPoint, KafkaListener kafkaListener, EventData eventData) throws Throwable {
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
