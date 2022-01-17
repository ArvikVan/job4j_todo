package servlet; /**
 * @author ArvikV
 * @version 1.0
 * @since 16.01.2022
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Category;
import models.Item;
import store.ItemStore;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "CategoryServlet", value = "/category")
public class CategoryServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        OutputStream output = response.getOutputStream();
        String json = GSON.toJson(ItemStore.instOf().findAllCategory());
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Item tempItem = GSON.fromJson(req.getReader(), Item.class);
        List<Category> categories = tempItem.getCategories();
        Item item = ItemStore.instOf().findById(tempItem.getId());
        if (item != null) {
            item.setCategories(categories);
            ItemStore.instOf().updateItem(item);
            OutputStream output = resp.getOutputStream();
            String json = GSON.toJson(item);
            output.write(json.getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();
        }
    }
}
