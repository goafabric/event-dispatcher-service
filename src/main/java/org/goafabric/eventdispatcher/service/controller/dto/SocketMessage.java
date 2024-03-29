package org.goafabric.eventdispatcher.service.controller.dto;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;


@RegisterReflectionForBinding(SocketMessage.class)
public record SocketMessage (
    String message
) {}
