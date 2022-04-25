package org.goafabric.eventdispatcher.adapter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class FHIRAdapter {
    private IGenericClient client = null;

    @Value("${adapter.fhir.url}")
    private String fhirUrl;

    @PostConstruct
    public void init() {
        client = FhirContext.forR4().newRestfulGenericClient(fhirUrl);
    }

    public Patient getPatient(String id) {
        log.info("retrieving patient via FHIR");
        try {
            final Patient patient =
                    client.read()
                            .resource(Patient.class)
                            .withId(id).execute();

            log.info(FhirContext.forR4().newJsonParser().setPrettyPrint(false).encodeResourceToString(patient));
            return patient;
        } catch (Exception e) {
            log.error("error during retrieval of patient", e);
            return null;
        }
    }

    public Practitioner getPractitioner(String id) {
        try {
            final Practitioner practitioner =
                    client.read()
                            .resource(Practitioner.class)
                            .withId(id).execute();

            log.info("retrieving practitioner via FHIR");
            log.info(FhirContext.forR4().newJsonParser().setPrettyPrint(false).encodeResourceToString(practitioner));
            return practitioner;
        } catch (Exception e) {
            log.error("error during retrieval of practitioner", e);
            return null;
        }
    }
}
