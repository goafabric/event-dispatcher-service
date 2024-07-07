package org.goafabric.eventdispatcher.producer;

import org.goafabric.eventdispatcher.service.extensions.TenantContext;

import java.util.Map;

public record EventData (
    Map<String, String> tenantInfos,
    String referenceId,
    Object payload
) {
    public EventData {
        TenantContext.setContext(tenantInfos); //little hacky, if the object is created on deserialization the tenantcontext will be set
    }
}
