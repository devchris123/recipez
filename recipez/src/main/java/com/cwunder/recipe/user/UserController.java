package com.cwunder.recipe.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

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

    private final RestTemplate restTemplate;

    private final String CAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify?secret={secret}&response={response}";
    private final String CAPTCHA_SECRET_KEY = "";

    private final SmartValidator validator;

    UserController(UserRepository repo, UserModelAssembler assembler, AuthorityRepository authRepo,
            PasswordEncoder pwEnc, RestTemplate restTemplate, SmartValidator validator) {
        this.repo = repo;
        this.assembler = assembler;
        this.authRepo = authRepo;
        this.pwEnc = pwEnc;
        this.restTemplate = restTemplate;
        this.validator = validator;
    }

    @GetMapping()
    public String getUserForm() {
        return "userform";
    }

    @PostMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public @ResponseBody ResponseEntity<?> createUser(@RequestBody MultiValueMap<String, String> newUserData)
            throws Exception {
        // validate form body
        var userFormData = validateUserFormData(newUserData);
        // validate captcha
        validateCaptcha(userFormData);
        // create user
        var newUser = doCreateUser(userFormData);
        // build response
        EntityModel<User> user = assembler.toModel(newUser);
        return ResponseEntity.created(user.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(user);
    }

    private UserFormData validateUserFormData(MultiValueMap<String, String> newUserData)
            throws MethodArgumentNotValidException, NoSuchMethodException, SecurityException {
        var userFormData = parseUserFormData(newUserData);
        var result = new BeanPropertyBindingResult(userFormData, "UserData");
        validator.validate(userFormData, result);
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethod("createUser", UserController.class), 0), result);
        }
        return userFormData;
    }

    private UserFormData parseUserFormData(MultiValueMap<String, String> userData) {
        var username = userData.getFirst("username");
        var password = userData.getFirst("password");
        var gRecaptchaResponse = userData.getFirst("g-recaptcha-response");
        return new UserFormData(username, password, gRecaptchaResponse);
    }

    private void validateCaptcha(UserFormData formData) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("secret", CAPTCHA_SECRET_KEY);
        uriVariables.put("response", formData.getGrecaptchaResponse());
        CaptchaData resp = restTemplate.postForObject(CAPTCHA_VERIFY_URL, null, CaptchaData.class, uriVariables);
        if (resp == null) {
            throw new RuntimeException("Captcha not successfull");
        }
        if (!resp.isSuccess()) {
            throw new IllegalArgumentException("Captcha not successfull");
        }
    }

    private User doCreateUser(UserFormData userFormData) {
        var newUser = new User(userFormData.getUsername(), userFormData.getPassword(), true);
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
        return newUser;
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
