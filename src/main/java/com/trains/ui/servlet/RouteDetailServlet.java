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

@WebServlet("/route-details")
public class RouteDetailServlet extends HttpServlet {

    private RouteService routeService;

    @Override
    public void init() throws ServletException {
        this.routeService = new RouteService(new JdbcRouteRepository(), new JdbcStationRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect("routes");
            return;
        }

        long id = Long.parseLong(idStr);
        Route route = routeService.getRouteWithStations(id);

        if (route == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Маршрут не найден");
            return;
        }

        req.setAttribute("route", route);
        req.getRequestDispatcher("/WEB-INF/jsp/route-detail.jsp").forward(req, resp);
    }
}
