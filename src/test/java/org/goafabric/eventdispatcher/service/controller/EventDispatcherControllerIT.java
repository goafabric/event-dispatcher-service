package org.goafabric.eventdispatcher.service.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.goafabric.eventdispatcher.service.test.WebsocketTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EventDispatcherControllerIT {

    private WebsocketTestUtils websocketTestUtils;

    @Autowired private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    private final String websocketResultTopic = "/public";

    @BeforeEach
    public void beforeEach() throws ExecutionException, InterruptedException, TimeoutException {
        if (websocketTestUtils == null) {
            websocketTestUtils = new WebsocketTestUtils(port);
        }
    }

    @Test
    void restCallShouldResultInMessage() throws Exception {
        websocketTestUtils.subscribeToTopic(websocketResultTopic);

        mockMvc.perform(MockMvcRequestBuilders.get(getHttpPath() + "/events/createpatient")).andExpect(status().is2xxSuccessful());

        await()
            .atMost(5, SECONDS)
            .untilAsserted(() -> assertThat(websocketTestUtils.getReceivedMessagesForTopic(websocketResultTopic)).isNotEmpty());
        assertThat(websocketTestUtils.getReceivedMessagesForTopic(websocketResultTopic).getFirst().message() ).isEqualTo("patient created");
    }

    @Test
    void incomingMessageShouldResultInOutgoingMessage() {
        websocketTestUtils.subscribeToTopic(websocketResultTopic);

        websocketTestUtils.sendMessageToTopic("/events/createpatient", "");

        await()
            .atMost(5, SECONDS)
            .untilAsserted(() -> assertThat(websocketTestUtils.getReceivedMessagesForTopic(websocketResultTopic)).isNotEmpty());
        assertThat(websocketTestUtils.getReceivedMessagesForTopic(websocketResultTopic).getFirst().message() ).isEqualTo("patient created");
    }

    private String getHttpPath() {
        return String.format("http://localhost:%d/", port);
    }
}
