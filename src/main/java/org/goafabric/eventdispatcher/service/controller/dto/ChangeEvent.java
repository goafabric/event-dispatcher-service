package org.goafabric.eventdispatcher.service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEvent {
    private String id;
    private String tenantId;
    private String referenceId;
    private String type;
    private DbOperation operation;
    private String origin;
}
