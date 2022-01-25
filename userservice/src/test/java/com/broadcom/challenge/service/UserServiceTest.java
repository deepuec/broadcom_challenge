package com.broadcom.challenge.service;

import com.broadcom.challenge.TestUtil;
import com.broadcom.challenge.entity.User;
import com.broadcom.challenge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void when_lastname_age_return_users() throws Exception{
        String lastname = "Aaa";
        int age = 10;
        Pageable page = PageRequest.of(0, 10, Sort.by("id"));
        when(userRepository.findByLastNameAndAge(lastname, age, page)).thenReturn(TestUtil.getUserPage(1));
        Page<User> users = userService.getUsers(lastname, age, 0, page);
        assertNotNull(users);
        assertEquals(1, users.getTotalElements());
    }

    @Test
    public void when_lastname_return_users() throws Exception{
        String lastname = "Aaa";
        Pageable page = PageRequest.of(0, 10, Sort.by("id"));
        when(userRepository.findByLastName(lastname, page)).thenReturn(TestUtil.getUserPage(1));
        Page<User> users = userService.getUsers(lastname, 0, 0, page);
        assertNotNull(users);
        assertEquals(1, users.getTotalElements());
    }

    @Test
    public void when_age_return_users() throws Exception{
        int age = 10;
        Pageable page = PageRequest.of(0, 10, Sort.by("id"));
        when(userRepository.findByAge(age, page)).thenReturn(TestUtil.getUserPage(1));
        Page<User> users = userService.getUsers(null, age, 0, page);
        assertNotNull(users);
        assertEquals(1, users.getTotalElements());
    }

    @Test
    public void when_no_filter_sort_lastname_asc_return_users() throws Exception{
        Pageable page = PageRequest.of(0, 10, Sort.by("lastname"));
        when(userRepository.findAllUsers(page)).thenReturn(TestUtil.getUserDTOList(1));
        Page<User> users = userService.getUsers(null, 0, 0, page);
        assertNotNull(users);
        assertEquals(1, users.getTotalElements());
    }
    @Test
    public void when_no_filter_sort_id_asc_return_users() throws Exception{
        int offset = 10;
        Pageable page = PageRequest.of(0, 10, Sort.by("id"));
        when(userRepository.findByIdGreaterThan(anyInt(), any(PageRequest.class))).thenReturn(TestUtil.getUserList(1));
        when(userRepository.getCount()).thenReturn(1L);
        Page<User> users = userService.getUsers(null, 0, 0, page);
        assertNotNull(users);
        assertEquals(1, users.getTotalElements());
    }

    @Test
    public void when_no_filter_sort_id_desc_return_users() throws Exception{
        int offset = 0;
        Pageable page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        when(userRepository.findByIdLessThan(anyInt(), any(PageRequest.class))).thenReturn(TestUtil.getUserList(1));
        when(userRepository.getCount()).thenReturn(1L);
        Page<User> users = userService.getUsers(null, 0, 0, page);
        assertNotNull(users);
        assertEquals(1, users.getTotalElements());
    }


}
