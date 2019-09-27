package ir.amv.snippets.springrepoimitator;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TestEntity {

    @Id
    private Long id;
    private String firstName;
}
