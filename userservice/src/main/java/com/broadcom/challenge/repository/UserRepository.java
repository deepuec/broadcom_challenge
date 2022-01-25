package com.broadcom.challenge.repository;

import com.broadcom.challenge.config.CacheConfig;
import com.broadcom.challenge.dto.UserDTO;
import com.broadcom.challenge.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Method to filter based on user age
     * @param age
     * @param pageable
     */
    Page<User> findByAge(int age, Pageable pageable);

    /**
     * Method to filter users based on last name
     * @param lastName
     * @param pageable
     * @return
     */
    Page<User> findByLastName(String lastName, Pageable pageable);

    /**
     * Method to filter users based on last name and age
     * @param lastName
     * @param age
     * @param pageable
     * @return
     */
    Page<User> findByLastNameAndAge(String lastName, int age, Pageable pageable);

    List<User> findByIdGreaterThan(long offsetId, Pageable pageable);

    List<User> findByIdLessThan(long offsetId, Pageable pageable);

    @Query(value = "SELECT id,first_name as firstName,last_name as lastName,age FROM USERS",
            nativeQuery = true)
    List<UserDTO> findAllUsers(Pageable pageable);


    /**
     * As getting count for larger table is time-consuming, cache the count for certain time
     * @return
     */
    @Cacheable(key = "#root.method.name", value = CacheConfig.USER_COUNT_CACHE)
    default long getCount() {

        return count();
    }
}
