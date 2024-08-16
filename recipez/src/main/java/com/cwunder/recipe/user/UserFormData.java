package com.cwunder.recipe.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class UserFormData {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private boolean enabled = true;

    @JsonProperty(value = "g-recaptcha-response")
    @NotBlank
    private String grecaptchaResponse;

    public UserFormData() {

    }

    public UserFormData(@NotBlank String username, @NotBlank String password, @NotBlank String grecaptchaResponse) {
        this.username = username;
        this.password = password;
        this.grecaptchaResponse = grecaptchaResponse;
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

    public boolean getEnabled() {
        return enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrecaptchaResponse() {
        return grecaptchaResponse;
    }

    @JsonProperty(value = "g-recaptcha-response")
    public void setGrecaptchaResponse(String grecaptchaResponse) {
        this.grecaptchaResponse = grecaptchaResponse;
    }
}
