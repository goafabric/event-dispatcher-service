package org.goafabric.eventdispatcher.websocket;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;


@RegisterReflectionForBinding(MySocketMessage.class)
public record MySocketMessage(
    String message
) {}
