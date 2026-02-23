package com.example.mongo_crud.repo;

import com.example.mongo_crud.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByName(String name);
    List<User> findByAge(int age);
    List<User> findByAgeGreaterThanEqual(int age);
}