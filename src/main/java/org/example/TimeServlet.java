package org.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Отримуємо параметр timezone з запиту. Якщо він не переданий, то використовуємо UTC
        String timezone = req.getParameter("timezone");
        if (timezone == null) {
            timezone = "UTC";
        }

        try {
            // Отримуємо поточний час в заданому часовому поясі
            ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(timezone));

            // Форматуємо час до рядка
            String time = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

            // Відправляємо HTML сторінку з часом
            resp.getWriter().println("<html><body><h1>" + time + "</h1></body></html>");
        } catch (DateTimeException e) {
            // Якщо переданий часовий пояс недійсний, повертаємо помилку
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("<html><body><h1>Invalid timezone</h1></body></html>");
        }

    }
}
