package org.goafabric.eventdispatcher.consumer;

import lombok.extern.slf4j.Slf4j;
import org.goafabric.eventdispatcher.listener.PatientListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Calendar2Adapter implements PatientListener {
    @Override
    public void createPatient(String id) {
        log.info("!! create patient");
    }

    @Override
    public void updatePatient(String id) {
        log.info("!! update patient");
    }

    @Override
    public void deletePatient(String id) {
        log.info("!! delete patient");
    }
}
