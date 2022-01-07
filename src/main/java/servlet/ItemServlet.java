package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Item;
import store.ItemStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author ArvikV
 * @version 1.0
 * @since 07.01.2022
 */
@WebServlet(urlPatterns = "/items")
public class ItemServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String json = GSON.toJson(ItemStore.instOf().findAll());
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    /**
     * вносим изменения
     * @param req запрос
     * @param resp ответ
     * @throws ServletException ловим
     * @throws IOException ловим
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Item item = GSON.fromJson(req.getReader(), Item.class);
        ItemStore.instOf().add(item);
        resp.setContentType("application/json; charset=utf-8");
        writeJson(resp, item);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Item tempItem = GSON.fromJson(req.getReader(), Item.class);
        int id = tempItem.getId();
        boolean done = tempItem.isDone();
        Item item = ItemStore.instOf().findById(id);
        if (item != null) {
            item.setDone(done);
            ItemStore.instOf().updateItem(item);
            writeJson(resp, item);
        }
    }

    private void writeJson(HttpServletResponse resp, Item item) throws IOException {
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(item);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

}
