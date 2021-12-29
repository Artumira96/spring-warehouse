package nl.averageflow.springwarehouse.services;

import nl.averageflow.springwarehouse.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserServiceContract {
    Page<User> getUsers(final Pageable pageable);

    Optional<User> getUserByUid(final UUID uid);

    void deleteUserByUid(final UUID uid);
}
