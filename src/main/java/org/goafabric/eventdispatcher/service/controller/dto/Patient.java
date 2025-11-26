package org.goafabric.eventdispatcher.service.controller.dto;

import java.time.LocalDate;

public record Patient(
    String id,
    String givenName,
    String familyName,

    String gender,
    LocalDate birthDate

) {}