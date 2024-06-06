package com.cwunder.recipe._shared;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public abstract class AppEntity {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(min = 12, max = 12)
    private String publicId;

    protected void initialize() {
        // Sets a default value for the public Id
        // You must call this function in subtype constructors in order to get this
        // behaviour if you do not set the publicId somehow else.
        setPublicId(IdGenerator.genId());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppEntity)) {
            return false;
        }
        AppEntity appEntity = (AppEntity) o;
        return Objects.equals(this.id, appEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
