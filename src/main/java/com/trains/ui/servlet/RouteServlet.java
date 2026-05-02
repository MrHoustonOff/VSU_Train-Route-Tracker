package com.trains.ui.servlet;

import com.trains.model.Route;
import com.trains.repository.impl.jdbc.JdbcRouteRepository;
import com.trains.repository.impl.jdbc.JdbcStationRepository;
import com.trains.service.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/routes")
public class RouteServlet extends HttpServlet {

    private RouteService routeService;

    @Override
    public void init() throws ServletException {
        // Ручное внедрение зависимостей, как и в Main.java
        this.routeService = new RouteService(new JdbcRouteRepository(), new JdbcStationRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            routeService.deleteRoute(id);
            resp.sendRedirect("routes");
            return;
        }

        List<Route> routes = routeService.getAllRoutes();
        req.setAttribute("routes", routes);
        req.getRequestDispatcher("/WEB-INF/jsp/routes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String trainNumber = req.getParameter("trainNumber");
        String name = req.getParameter("name");
        
        if (trainNumber != null && name != null) {
            routeService.createRoute(trainNumber, name);
        }
        resp.sendRedirect("routes");
    }
}
