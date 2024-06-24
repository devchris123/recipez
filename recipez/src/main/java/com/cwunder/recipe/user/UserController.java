package com.cwunder.recipe.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import com.cwunder.recipe._shared.ExistsException;
import com.cwunder.recipe._shared.NotFoundException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    private final UserModelAssembler assembler;

    private final AuthorityRepository authRepo;

    private final PasswordEncoder pwEnc;

    UserController(UserRepository repo, UserModelAssembler assembler, AuthorityRepository authRepo,
            PasswordEncoder pwEnc) {
        this.repo = repo;
        this.assembler = assembler;
        this.authRepo = authRepo;
        this.pwEnc = pwEnc;
    }

    @PostMapping()
    public @ResponseBody ResponseEntity<?> createUser(@Valid @RequestBody User newUser) {
        // encode password
        if (repo.existsByUsername(newUser.getUsername())) {
            throw generateExistsException();
        }
        newUser.setPassword(pwEnc.encode(newUser.getPassword()));
        var created = repo.save(newUser);
        var auth = new Authority();
        auth.setUser(created);
        auth.setAuthority("ROLE_USER");
        authRepo.save(auth);
        EntityModel<User> user = assembler.toModel(newUser);
        return ResponseEntity.created(user.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(user);
    }

    @GetMapping("/{id}")
    public @ResponseBody EntityModel<User> getUser(@PathVariable String id) {
        User user = repo.findByPublicId(id)
                .or(() -> repo.findByUsernameAuth(id))
                .orElseThrow(this::generateNotFoundException);
        return assembler.toModel(user);
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateUser(@Valid @RequestBody User newUser, @PathVariable String id) {
        var updUser = repo.findByPublicId(id)
                .or(() -> repo.findByUsernameAuth(id))
                .orElseThrow(this::generateNotFoundException);
        updUser.setUsername(newUser.getUsername());
        var _updUser = repo.save(updUser);
        return ResponseEntity.ok().body(assembler.toModel(_updUser));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteUser(@PathVariable String id) {
        repo.deleteByPublicId(id);
        return ResponseEntity.noContent().build();
    }

    private NotFoundException generateNotFoundException() {
        return new NotFoundException("User");
    }

    private ExistsException generateExistsException() {
        return new ExistsException("User");
    }
}
