package com.broadcom.challenge.web.controller;

import com.broadcom.challenge.entity.User;
import com.broadcom.challenge.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    private static final String BASE_URI = "http://localhost:8080/api/users";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    IUserService userService;

    /**
     * Expect empty array when no user found
     * @throws Exception
     */
    @Test
    public void when_user_not_found_then_return_empty() throws Exception {
        Mockito.when(userService.getUsers(null, 0, 0, PageRequest.of(0,10, Sort.by("id")))).thenReturn(getUserPage(0));
            mockMvc.perform(get(BASE_URI).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

    }

    /**
     * Expect array of users when found
     * @throws Exception
     */
    @Test
    public void when_user_found_return_user() throws Exception {
        Mockito.when(userService.getUsers(null, 0, 0, PageRequest.of(0,20, Sort.by("id")))).thenReturn(getUserPage(1));
        mockMvc.perform(get(BASE_URI).param("size", "20").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(header().longValue("X-Total-Count", 1));
    }


    /**
     * Age cannot be less than 0
     * @throws Exception
     */
    @Test
    public void when_invalid_age_return_bad_request() throws Exception {
        mockMvc.perform(get(BASE_URI).param("age","-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    /**
     * Offset cannot be less than 0
     * @throws Exception
     */
    @Test
    public void when_invalid_offset_return_bad_request() throws Exception {
        mockMvc.perform(get(BASE_URI).param("offset","-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
    private Page<User> getUserPage(int size) {
        if(size==0){
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(0,20), 0);
        }
        return new PageImpl<>(Arrays.asList(new User(1)), PageRequest.of(0,20), size);
    }
}
