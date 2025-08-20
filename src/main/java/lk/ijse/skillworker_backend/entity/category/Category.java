package lk.ijse.skillworker_backend.entity.category;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.worker.WorkerCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category entity in the system.
 * Each category has a unique identifier.
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkerCategory> workerCategories = new ArrayList<>();
}
