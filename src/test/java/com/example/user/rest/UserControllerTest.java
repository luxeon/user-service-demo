package com.example.user.rest;

import com.example.user.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.user.util.TestUtils.readStringFromResource;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create_shouldReturn201_whenRequestIsValid() throws Exception {
        var request = readStringFromResource("fixture/user/create/request/success_request.json");
        var expectedResponse = readStringFromResource("fixture/user/create/response/success_response.json");

        var response = mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    void create_shouldReturn400_whenRequestIsInvalid() throws Exception {
        var request = readStringFromResource("fixture/user/create/request/bad_request.json");
        var expectedResponse = readStringFromResource("fixture/user/create/response/bad_request_response.json");

        var response = mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_shouldReturn409_whenUserAlreadyExists() throws Exception {
        var request = readStringFromResource("fixture/user/create/request/success_request.json");
        var expectedResponse = readStringFromResource("fixture/user/create/response/conflict_response.json");

        var response = mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }
} 