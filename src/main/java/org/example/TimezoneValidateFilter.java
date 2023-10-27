package org.example;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimeZone;


public class TimezoneValidateFilter implements javax.servlet.Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Отримуємо параметр timezone з запиту
        String timezone = req.getParameter("timezone");

        if (timezone != null && TimeZone.getTimeZone(timezone).getID().equals("GMT")) {
            // Якщо переданий некоректний часовий пояс, то повертаємо помилку 400 і сторінку з повідомленням про помилку
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("<html><body><h1>Invalid timezone</h1></body></html>");
        } else {
            // Якщо все добре, то передаємо обробку наступному фільтру або сервлету
            chain.doFilter(request, response);
        }
    }


}
