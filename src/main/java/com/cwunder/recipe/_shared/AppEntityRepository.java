package com.cwunder.recipe._shared;

import java.util.Optional;

public interface AppEntityRepository<T> {
    Optional<T> findByPublicId(String publicId);

    void deleteByPublicId(String publicId);
}
