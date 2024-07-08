package org.goafabric.eventdispatcher.service.controller.dto;


public record ChangeEvent (
    String id,
    String tenantId,
    String referenceId,
    String type,
    DbOperation operation,
    String origin,
    Object payload
) {}
