package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DeleteCandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        PsqlStore.instOf().deleteCandidateById(Integer.parseInt(id));
        for (File file : new File("C:\\images\\").listFiles()) {
            String fileName = file.getName();
            if (id.equals(fileName.substring(0, fileName.indexOf(".")))) {
                file.delete();
                break;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
