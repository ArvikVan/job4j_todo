package store;

import models.Item;
import models.User;

import java.util.List;

/**
 * @author ArvikV
 * @version 1.0
 * @since 07.01.2022
 */
public interface Store {
    Item add(Item item);

    List<Item> findAll();

    Item findById(int id);

    void done(int id);

    boolean update(int id, Item item);

    boolean updateItem(Item item);

    List<Item> findByDone();

    User findUserByName(String name);

    User addUser(User user);
}