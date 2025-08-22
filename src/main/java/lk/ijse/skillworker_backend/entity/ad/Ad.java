package lk.ijse.skillworker_backend.entity.ad;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.entity.worker.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    @Column(name = "starting_price", precision = 12, scale = 2)
    private BigDecimal starting_price; // starting price

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
