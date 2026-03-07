package com.aguardientes.azarcafetero.user_service.infrastructure.adapter.out.persistence;

import com.aguardientes.azarcafetero.user_service.domain.model.User;
import com.aguardientes.azarcafetero.user_service.domain.port.out.ProfileRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProfileRepositoryAdapter implements ProfileRepository {

    private final UserJpaRepository userJpaRepository;

    public ProfileRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userJpaRepository.findById(userId)
                .map(UserMapper::toDomain);
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(UserMapper.toEntity(user));
    }
}
