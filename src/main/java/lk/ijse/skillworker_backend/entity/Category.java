package lk.ijse.skillworker_backend.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
