package org.goafabric.event;


import org.goafabric.eventdispatcher.service.extensions.UserContext;

import java.util.Map;

public record EventData(
    String type,
    String referenceId,
    String operation, //CREATE, UPDATE, DELETE
    Object payload,
    Map<String, String> tenantInfos
) {
    public EventData {
        UserContext.setContext(tenantInfos); //little hacky, if the object is created on deserialization the tenantcontext will be set
    }
}
