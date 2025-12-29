package in.krish.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name; // e.g. ROLE_ADMIN, ROLE_USER

    @Column(name = "description")
    private String description;

    // getters & setters
}
