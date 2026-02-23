package com.example.mongo_crud.controller;

import com.example.mongo_crud.model.User;
import com.example.mongo_crud.repo.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        user.setId(null); // чтобы случайно не перезаписать существующего
        return repo.save(user);
    }

    // READ ALL
    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public User update(@PathVariable String id, @Valid @RequestBody User user) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
        user.setId(id);
        return repo.save(user);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
        repo.deleteById(id);
    }

    // SEARCH
    // /api/users/search?name=Ivan
    // /api/users/search?age=20
    @GetMapping("/search")
    public List<User> search(@RequestParam(required = false) String name,
                             @RequestParam(required = false) Integer age) {

        if (name != null) return repo.findByName(name);
        if (age != null) return repo.findByAge(age);

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide 'name' or 'age'");
    }
}
