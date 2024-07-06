package org.goafabric.eventdispatcher.consumer;


import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.Aspect;
import org.goafabric.eventdispatcher.Application;
import org.springframework.aop.support.AopUtils;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@ImportRuntimeHints(NatsListenerAspect.ApplicationRuntimeHints.class)
public class NatsListenerAspect {

    private final NatsSubscription natsSubscription;
    private final ApplicationContext applicationContext;

    @Autowired
    public NatsListenerAspect(NatsSubscription natsSubscription, ApplicationContext applicationContext) {
        this.natsSubscription = natsSubscription;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Arrays.stream(applicationContext.getBeanDefinitionNames())
                .filter(beanName -> !beanName.equals("natsListenerAspect"))
                .forEach(beanName -> {
                    var bean = applicationContext.getBean(beanName);
                    Arrays.stream(AopUtils.getTargetClass(bean).getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(NatsListener.class))
                            .forEach(method -> setupSubscription(bean, method, method.getAnnotation(NatsListener.class)));
                });

    }

    private void setupSubscription(Object bean, Method method, NatsListener natsListener) {
        Arrays.stream(natsListener.subjects()).forEach(subject -> {
            natsSubscription.create(natsListener.consumerName(), subject, (msg, eventData) -> {
                ReflectionUtils.invokeMethod(method, bean, msg.getSubject(), eventData);
            });
        });
    }

    static class ApplicationRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(NatsListenerAspect.class, MemberCategory.INVOKE_DECLARED_METHODS);

            var scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
            scanner.findCandidateComponents(Application.class.getPackage().getName()).forEach(beanDefinition -> {
                try {
                    Arrays.stream(ClassUtils.forName(beanDefinition.getBeanClassName(), classLoader).getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(NatsListener.class))
                            .forEach(method -> hints.reflection().registerMethod(method, ExecutableMode.INVOKE));
                } catch (ClassNotFoundException e) { throw new IllegalStateException(e); }
            });
        }

    }
}
