package com.gmail.petrikov05.app.repository;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {

    User getUserByEmail(String email);

    User getUserById(Long id);

    List<User> getUsersByPage(int startPosition, int countOfUserByPage);

}
