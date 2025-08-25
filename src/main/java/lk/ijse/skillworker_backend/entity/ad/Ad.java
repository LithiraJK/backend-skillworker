package lk.ijse.skillworker_backend.entity.ad;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.entity.worker.Worker;
import lombok.*;

import java.math.BigDecimal;


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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AdStatus status = AdStatus.PENDING;

    //Relationships

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;


}
