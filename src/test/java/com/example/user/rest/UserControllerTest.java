package com.example.user.rest;

import com.example.user.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.user.util.TestUtils.readStringFromResource;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class UserControllerTest {

    private static final String EXISTING_USER_ID = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11";

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
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
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

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void update_shouldReturn200_whenRequestIsValid() throws Exception {
        var request = readStringFromResource("fixture/user/update/request/success_request.json");
        var expectedResponse = readStringFromResource("fixture/user/update/response/success_response.json");

        var response = mockMvc.perform(put("/api/users/" + EXISTING_USER_ID)
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void update_shouldReturn409_whenUserAlreadyExistsWithSuchEmail() throws Exception {
        var request = readStringFromResource("fixture/user/update/request/email_conflict_request.json");
        var expectedResponse = readStringFromResource("fixture/user/update/response/conflict_response.json");

        var response = mockMvc.perform(put("/api/users/" + EXISTING_USER_ID)
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void update_shouldReturn409_whenUserAlreadyExistsWithSuchNickname() throws Exception {
        var request = readStringFromResource("fixture/user/update/request/nickname_conflict_request.json");
        var expectedResponse = readStringFromResource("fixture/user/update/response/conflict_response.json");

        var response = mockMvc.perform(put("/api/users/" + EXISTING_USER_ID)
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void update_shouldReturn400_whenRequestIsInvalid() throws Exception {
        var request = readStringFromResource("fixture/user/update/request/bad_request.json");
        var expectedResponse = readStringFromResource("fixture/user/update/response/bad_request_response.json");

        var response = mockMvc.perform(put("/api/users/" + EXISTING_USER_ID)
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    void update_shouldReturn404_whenUserDoesNotExist() throws Exception {
        var request = readStringFromResource("fixture/user/update/request/success_request.json");

        mockMvc.perform(put("/api/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void delete_shouldReturn204_whenUserExists() throws Exception {
        mockMvc.perform(delete("/api/users/" + EXISTING_USER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void search_shouldReturn200_whenCountryIsUK() throws Exception {
        var expectedResponse = readStringFromResource("fixture/user/search/response/success_uk_response.json");

        var response = mockMvc.perform(get("/api/users?country=UK&sort=createdAt,desc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void search_shouldReturn200_whenCountryIsUKAndPageableIsSet() throws Exception {
        var expectedResponse = readStringFromResource("fixture/user/search/response/success_uk_paginated_response.json");

        var response = mockMvc.perform(get("/api/users?country=UK&page=0&size=5&sort=createdAt,desc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void search_shouldReturn200_whenCountryIsNotSet() throws Exception {
        var expectedResponse = readStringFromResource("fixture/user/search/response/success_all_users_response.json");

        var response = mockMvc.perform(get("/api/users?sort=createdAt,desc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }

    @Test
    @Sql(scripts = "/db/users/insert_user.sql", executionPhase = BEFORE_TEST_METHOD)
    void search_shouldReturn200AndEmptyList_whenNoUsersFromCountry() throws Exception {
        var expectedResponse = readStringFromResource("fixture/user/search/response/empty_response.json");

        var response = mockMvc.perform(get("/api/users?country=DE&sort=createdAt,desc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(response).isEqualTo(expectedResponse);
    }
} 