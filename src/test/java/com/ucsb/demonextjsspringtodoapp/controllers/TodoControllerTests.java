package com.ucsb.demonextjsspringtodoapp.controllers;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucsb.demonextjsspringtodoapp.models.Todo;
import com.ucsb.demonextjsspringtodoapp.repositories.TodoRepository;
import com.ucsb.demonextjsspringtodoapp.services.Auth0Service;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@WebMvcTest(value = TodoController.class)
@WithMockUser
public class TodoControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  TodoRepository mockTodoRepository;

  @MockBean
  private Auth0Service mockAuth0Service;

  @Test
  public void testGetTodos() throws Exception {
    String userToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlRlc3QgVXNlciIsImlhdCI6MTUxNjIzOTAyMn0.drDhO00ywU1JZtnkHkIkI0Dni1d3HZ1mtPTf3PLfyeY";
    List<Todo> expectedTodos = new ArrayList<Todo>();
    expectedTodos.add(new Todo(1L, "todo 1", false, "123456"));

    when(mockTodoRepository.findByUserId(any(String.class))).thenReturn(expectedTodos);
    MvcResult response = mockMvc
        .perform(
            get("/api/todos").contentType("application/json").header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
        .andExpect(status().isOk()).andReturn();

    String responseString = response.getResponse().getContentAsString();
    List<Todo> actualTodos = objectMapper.readValue(responseString, new TypeReference<List<Todo>>() {
    });
    assertThat(expectedTodos, containsInAnyOrder(actualTodos));
  }
}
