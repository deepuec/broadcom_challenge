package com.broadcom.challenge.web.controller;

import com.broadcom.challenge.entity.User;
import com.broadcom.challenge.service.IUserService;
import com.broadcom.challenge.web.error.BadRequestAlertException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    /**
     * Controller method responsible to expose API url and several request parameters
     * @param lastName
     * @param age
     * @param offset This is not mandatory field, will be used only during default search without any filters i.e., lastName and age
     *               to improve DB performance. As page number increases, DB performance decreases.
     *               So introduced this field to work as id filter.
     *               Idea is, when searching without filter client will populate this field with id value of last user populated in previous result.
     *               And same value will be used to filter data with id which greater or less than the offset based on sorting direction.
     * @param pageable
     * @return
     * @throws BadRequestAlertException
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false)String lastName,
                                                  @RequestParam(required = false, defaultValue = "0") @Min(value=0) int age,
                                                  @RequestParam(required = false, defaultValue = "0") @Min(value=0) long offset,
                                                  @PageableDefault(sort = "id") Pageable pageable) throws BadRequestAlertException {

        Page<User> users = userService.getUsers(lastName, age, offset, pageable);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Total-Count", String.valueOf(users.getTotalElements()));
        return new ResponseEntity<List<User>>(users.getContent(),httpHeaders, HttpStatus.OK);
    }

}
