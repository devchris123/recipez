package com.cwunder.recipe._shared;

import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AppEntityRepository<T> {
    Optional<T> findByPublicId(String publicId);

    void deleteByPublicId(String publicId);
}
