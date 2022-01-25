package com.broadcom.challenge.service;

import com.broadcom.challenge.dto.UserDTO;
import com.broadcom.challenge.entity.User;
import com.broadcom.challenge.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Service class responsible to communicate with data layer and get required information
 */
@Service
public class UserService implements IUserService{

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository userRepository;
    @Override
    public Page<User> getUsers(String lastName, int age, long offset, Pageable pageable) {
        if(StringUtils.hasText(lastName) && age > 0){
            //Filter users based on last name and age
            return userRepository.findByLastNameAndAge(lastName, age, pageable);
        }else if(StringUtils.hasText(lastName)){
            //filter users based on last name
            return userRepository.findByLastName(lastName, pageable);
        }else if(age>0){
            //filter based on user age
            return userRepository.findByAge(age, pageable);
        }
        return getUsersWithOffset(offset, pageable);
    }

    private PageImpl<User> getUsersWithOffset(long offset, Pageable pageable) {
        //default search, use id as filter to improve DB performance
        Sort.Order sortOrder = pageable.getSort().stream().findFirst().get();
        if(!sortOrder.getProperty().equalsIgnoreCase("id")){
            return new PageImpl<>(mapToEntity(userRepository.findAllUsers(pageable)), pageable, userRepository.getCount());
        }else {
            Pageable tempPageable = pageable.withPage(0);//Initialize page to zero
            if (null == pageable.getSort().getOrderFor("id") || pageable.getSort().getOrderFor("id").isAscending()) {
                return new PageImpl<>(userRepository.findByIdGreaterThan(offset, tempPageable), pageable, userRepository.getCount());
            } else {
                return new PageImpl<>(userRepository.findByIdLessThan(offset == 0 ? Integer.MAX_VALUE : offset, tempPageable), pageable, userRepository.getCount());
            }
        }
    }

    private List<User> mapToEntity(List<UserDTO> users) {
        return users.stream().map(dto -> new User(dto.getId(),dto.getFirstName(),dto.getLastName(),dto.getAge())).collect(Collectors.toList());
    }

}
