package lk.ijse.skillworker_backend.entity.subscription;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.auth.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private BigDecimal amount;

    private String currency; // LKR

    private String orderId; // PayHere Order ID

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createdAt;
}
