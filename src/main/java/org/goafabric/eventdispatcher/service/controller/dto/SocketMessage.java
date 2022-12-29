package org.goafabric.eventdispatcher.service.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SocketMessage {
    private String message;
}
