package toone;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ArvikV
 * @version 1.1
 * @since 09.01.2022
 */
@Entity
@Table(name = "j_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JUser> jUsers = new ArrayList<>();

    public static Role of(String name) {
        Role role = new Role();
        role.name = name;
        return role;
    }

    public List<JUser> getjUsers() {
        return jUsers;
    }

    public void setjUsers(List<JUser> jUsers) {
        this.jUsers = jUsers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return id == role.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
