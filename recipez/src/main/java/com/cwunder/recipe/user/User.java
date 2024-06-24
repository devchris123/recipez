package com.cwunder.recipe.user;

import java.util.Set;

import com.cwunder.recipe._shared.AppEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class User extends AppEntity {
    @NotBlank
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean enabled = true;

    @JsonIgnore()
    @OneToMany(mappedBy = "user", targetEntity = Authority.class, fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    public User() {
        initialize();
    }

    public User(@NotBlank String username, @NotBlank String password, boolean enabled) {
        initialize();
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User(@NotBlank String publicId, @NotBlank String username, @NotBlank String password, boolean enabled) {
        this(username, password, enabled);
        this.setPublicId(publicId);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
