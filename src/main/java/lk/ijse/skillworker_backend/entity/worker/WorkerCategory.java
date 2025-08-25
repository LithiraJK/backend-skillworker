package lk.ijse.skillworker_backend.entity.worker;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.category.Category;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "worker_category")
public class WorkerCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id" , nullable = false)
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "category_id" , nullable = false)
    private Category category;
}
