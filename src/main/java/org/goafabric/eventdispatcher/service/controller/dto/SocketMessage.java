package org.goafabric.eventdispatcher.service.controller.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage {
    private String message;
}
