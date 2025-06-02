package org.goafabric.event;

import org.goafabric.eventdispatcher.service.extensions.UserContext;

import java.util.Map;

public record EventData (
    Map<String, String> tenantInfos,
    String referenceId,
    String operation,
    Object payload
) {
    public EventData {
        UserContext.setContext(tenantInfos); //little hacky, if the object is created on deserialization the tenantcontext will be set
    }
}
