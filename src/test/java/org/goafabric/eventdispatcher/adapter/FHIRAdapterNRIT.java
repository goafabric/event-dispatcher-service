package org.goafabric.eventdispatcher.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FHIRAdapterNRIT {
    @Autowired
    private FHIRAdapter fhirAdapter;

    @Test
    public void readPatient() {
        fhirAdapter.getPatient("K3U3KHZ6UPEBQUSWS6G1HYE98JKAS2ZN");
    }

}