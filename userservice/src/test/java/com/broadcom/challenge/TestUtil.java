package com.broadcom.challenge;

import com.broadcom.challenge.dto.UserDTO;
import com.broadcom.challenge.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static Page<User> getUserPage(int size) {
        if(size==0){
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(0,20), 0);
        }
        return new PageImpl<>(Arrays.asList(new User(1)), PageRequest.of(0,20), size);
    }
    public static List<User> getUserList(int size) {

        if(size==0){
            return new ArrayList<>();
        }
        List<User> list = Arrays.asList(new User(10, "test", "test", 10));
        return list;
    }


    public static List<UserDTO> getUserDTOList(int size) {
        if(size==0){
            return new ArrayList<>();
        }
        return Arrays.asList(new UserDTO() {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public String getFirstName() {
                return "test";
            }

            @Override
            public String getLastName() {
                return "test";
            }

            @Override
            public int getAge() {
                return 10;
            }
        });
    }
}
