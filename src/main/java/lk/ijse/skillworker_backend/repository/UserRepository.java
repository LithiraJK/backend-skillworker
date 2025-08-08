package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.entity.Role;
import lk.ijse.skillworker_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByRole(Role role);
}
