package org.goafabric.eventdispatcher.consumer.extensions;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.goafabric.eventdispatcher.service.extensions.UserContext;
import org.slf4j.MDC;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RegisterReflection(classes = TenantAspect.class, memberCategories = MemberCategory.INVOKE_DECLARED_METHODS)
public class TenantAspect {

    @Around("@annotation(kafkaListener)")
    public Object resolveTenantInfo(ProceedingJoinPoint joinPoint, KafkaListener kafkaListener) throws Throwable {
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.getTenantId());
        MDC.put("tenantId", UserContext.getTenantId());
        Object result = joinPoint.proceed();
        MDC.remove("tenantId");
        return result;
    }

}
