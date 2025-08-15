package lk.ijse.skillworker_backend.entity.worker;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_category")
public class WorkerCategory {

    @EmbeddedId
    private WorkerCategoryID workerCategoryID;

    @ManyToOne
    @MapsId("worker_id")
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne
    @MapsId("category_id")
    @JoinColumn(name = "category_id")
    private Category category;
}
