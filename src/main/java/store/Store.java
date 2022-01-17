package store;

import models.Category;
import models.Item;
import models.User;

import java.io.Serializable;
import java.util.List;

/**
 * @author ArvikV
 * @version 1.0
 * @since 07.01.2022
 */
public interface Store {
    Serializable add(Item item);

    List<Item> findAll();

    Item findById(int id);

    void done(int id);

    /*boolean update(int id, Item item);*/

    boolean update(int id, Item item, Category category);

    boolean updateItem(Item item);

    List<Item> findByDone();

    User findUserByName(String name);

    Serializable addUser(User user);

    List<Category> findAllCategory();

    Category findCategoryById(int id);
}