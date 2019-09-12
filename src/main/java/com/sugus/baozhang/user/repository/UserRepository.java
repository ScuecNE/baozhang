package com.sugus.baozhang.user.repository;

import com.sugus.baozhang.user.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findUserByEmail(String email);

}
