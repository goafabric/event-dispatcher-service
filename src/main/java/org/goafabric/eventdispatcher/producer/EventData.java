package org.goafabric.eventdispatcher.producer;

import java.util.Map;

public record EventData (
    Map<String, String> tenantInfos,
    String referenceId,
    Object payload
) {}
