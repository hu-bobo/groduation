package graprojection.jpa.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="JPA_TEST")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Data
public class JpaTestInfo {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "ID",unique = true, nullable = false)
    private String id;
    private Date age;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getAge() {
        return age;
    }

    public void setAge(Date age) {
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
