package ru.job4j.dream.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadImageServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String imageId = req.getParameter("id");
//        for (File name : new File("C:\\images\\").listFiles()) {
//            String fileName = name.getName().substring(0, name.getName().indexOf("."));
//            if (imageId.equals(fileName)) {
//
//        }
//        req.setAttribute("images", images);
//        RequestDispatcher dispatcher = req.getRequestDispatcher("/upload.jsp");
//        dispatcher.forward(req, resp);
//    }
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
