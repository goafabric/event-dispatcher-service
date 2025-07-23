package org.goafabric.eventdispatcher.service.controller.dto;

import java.time.LocalDate;

public record Practitioner(
    String id,
    String organizationId,
    String givenName,
    String familyName,

    String gender,
    LocalDate birthDate

) {}