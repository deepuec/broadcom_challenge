package com.broadcom.challenge.repository;

import com.broadcom.challenge.dto.UserDTO;
import com.broadcom.challenge.entity.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @Sql("/insert_users.sql")
    void when_lastname_return_users(){
        String lastname = "Russel";
        Pageable page = PageRequest.of(0, 10, Sort.by("id"));
        Page<User> users = userRepository.findByLastName(lastname, page);
        assertNotNull(users);
        //Expect 5 users
        assertEquals(5, users.getTotalElements());
        //expect all last name in the list matches
        Set<String> lastNames = users.getContent().parallelStream().map(u -> u.getLastName()).collect(Collectors.toSet());
        //Check the ordering of user data
        List<Long> ids = users.getContent().parallelStream().map(u -> u.getId()).collect(Collectors.toList());
        assertTrue(Arrays.asList(1L,100L,103L,104L,105L).equals(ids));
        //Make sure pulled result matches request lastname
        assertEquals(1, lastNames.size());
        assertTrue(lastNames.contains(lastname));
    }

    @Test
    @Sql("/insert_users.sql")
    public void when_lastname_age_return_users() throws Exception{
        String lastname = "Russel";
        int age = 23;
        Pageable page = PageRequest.of(0, 10, Sort.by("id"));
        Page<User> users = userRepository.findByLastNameAndAge(lastname, age, page);
        assertNotNull(users);
        //Expect 1 user
        assertEquals(1, users.getTotalElements());
        //Make sure pulled result matches request lastname and age
        assertEquals(age, users.getContent().get(0).getAge());
        assertEquals(lastname, users.getContent().get(0).getLastName());
    }

    @Test
    @Sql("/insert_users.sql")
    public void when_age_sort_id_asc_return_users() throws Exception{
        int age = 23;
        Pageable page = PageRequest.of(0, 10, Sort.by("id"));
        Page<User> users = userRepository.findByAge(age, page);
        assertNotNull(users);
        //Expect 5 users
        assertEquals(4, users.getTotalElements());
        //expect all last name in the list matches
        Set<Integer> ages = users.getContent().parallelStream().map(u -> u.getAge()).collect(Collectors.toSet());
        //Check the ordering of user data
        List<Long> ids = users.getContent().stream().map(u -> u.getId()).collect(Collectors.toList());
        assertTrue(Arrays.asList(1L,3L,6L,18L).equals(ids));
        //Make sure pulled result matches request lastname
        assertEquals(1, ages.size());
        assertTrue(ages.contains(age));
    }

    @Test
    @Sql("/insert_users.sql")
    public void when_age_sort_id_desc_return_users() throws Exception{
        int age = 23;
        Pageable page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,"id"));
        Page<User> users = userRepository.findByAge(age, page);
        assertNotNull(users);
        //Expect 5 users
        assertEquals(4, users.getTotalElements());
        //Check the ordering of user data
        List<Long> ids = users.getContent().stream().map(u -> u.getId()).collect(Collectors.toList());
        assertTrue(Arrays.asList(18L,6L,3L,1L).equals(ids));
    }

    @Test
    @Sql("/insert_users.sql")
    public void when_no_filter_sort_lastname_asc_return_users() throws Exception{
        Pageable page = PageRequest.of(0, 5, Sort.by("last_name"));
        List<UserDTO> users = userRepository.findAllUsers(page);
        assertNotNull(users);
        //Expect 5 users
        assertEquals(5, users.size());
        //Check the ordering of user data
        List<String> lastNames = users.stream().map(u -> u.getLastName()).collect(Collectors.toList());
        assertTrue(Arrays.asList("Blanda","Blick","Dibbert","Feeney","Funk").equals(lastNames));

    }

    @Test
    @Sql("/insert_users.sql")
    public void when_no_filter_sort_lastname_desc_return_users() throws Exception{
        Pageable page = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "last_name"));
        List<UserDTO> users = userRepository.findAllUsers(page);
        assertNotNull(users);
        //Expect 5 users
        assertEquals(5, users.size());
        //Check the ordering of user data
        List<String> lastNames = users.stream().map(u -> u.getLastName()).collect(Collectors.toList());
        assertTrue(Arrays.asList("eWehner","dKoss","cKling","bTowne","aReichel").equals(lastNames));

    }

}
