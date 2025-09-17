package lk.ijse.skillworker_backend.entity.ad;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.entity.review.Review;
import lk.ijse.skillworker_backend.entity.worker.Worker;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private BigDecimal startingPrice;

    private LocalDate createdDate ;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AdStatus status = AdStatus.PENDING;

    @Column(name = "avg_rating")
    private Double avgRating = 0.0;

    @Column(name = "reviews_count")
    private Long reviewsCount = 0L;

    //Relationships

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<Review> reviews;

}
