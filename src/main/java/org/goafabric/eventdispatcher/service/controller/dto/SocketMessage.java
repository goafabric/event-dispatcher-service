package org.goafabric.eventdispatcher.service.controller.dto;

import lombok.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterReflectionForBinding(SocketMessage.class)
public class SocketMessage {
    private String message;
}
