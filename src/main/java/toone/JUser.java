package toone;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author ArvikV
 * @version 1.1
 * @since 09.01.2022
 */
@Entity
@Table(name = "j_user")
public class JUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public static JUser of(String name) {
        JUser user = new JUser();
        user.name = name;
        return user;
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
        JUser user = (JUser) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
