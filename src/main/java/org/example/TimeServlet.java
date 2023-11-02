package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = new TemplateEngine();
        // Створюємо об'єкт шаблонного двигуна Thymeleaf.

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("C:\\Диск D\\java\\Clon_Dev_11_Servlet\\src\\main\\resources\\templates\\");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        templateEngine.addTemplateResolver(resolver);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Отримуємо параметр timezone з запиту. Якщо він не переданий, то використовуємо UTC
        String timezone = req.getParameter("timezone");
        if (timezone == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lastTimezone".equals(cookie.getName())) {
                        timezone = cookie.getValue();
                        break;
                    }
                }
            }
            if (timezone == null) {
                timezone = "UTC";
            }
        }

        try {
            // Отримуємо поточний час в заданому часовому поясі
            ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(timezone));

            // Форматуємо час до рядка
            String time = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

            Context context = new Context();
            context.setVariable("time", time);

            resp.setContentType("text/html;charset=UTF-8");

            templateEngine.process("time", context, resp.getWriter());

        } catch (DateTimeException e) {
            // Якщо переданий часовий пояс недійсний, повертаємо помилку
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("<html><body><h1>Invalid timezone</h1></body></html>");
        }
    }
}
