package lk.ijse.skillworker_backend.entity.review;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.entity.worker.Worker;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Data @NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // 1..5

    @Column(length = 2000)
    private String comment;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = true)
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = true)
    private Ad ad;




}
