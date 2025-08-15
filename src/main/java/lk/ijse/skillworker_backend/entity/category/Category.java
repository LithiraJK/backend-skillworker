package lk.ijse.skillworker_backend.entity.category;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.worker.Worker;
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

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Worker> workers = new ArrayList<>();
}
