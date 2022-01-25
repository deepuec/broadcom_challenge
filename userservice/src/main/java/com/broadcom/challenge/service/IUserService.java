package com.broadcom.challenge.service;

import com.broadcom.challenge.dto.UserDTO;
import com.broadcom.challenge.entity.User;
import com.broadcom.challenge.web.error.BadRequestAlertException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    /**
     * This method will take optional lastName and age parameters and pull matching users
     * @param lastName
     * @param age
     * @param offset
     * @param pageable
     * @return Page of result
     */
    public Page<User> getUsers(String lastName, int age, long offset, Pageable pageable);
}
