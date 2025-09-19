package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.entity.subscription.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByOrderId(String orderId);
    Optional<Subscription> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
