package com.trains.ui.servlet;

import com.trains.model.StationType;
import com.trains.repository.impl.jdbc.JdbcStationRepository;
import com.trains.service.StationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

@WebServlet("/stations")
public class StationServlet extends HttpServlet {

    private StationService stationService;

    @Override
    public void init() throws ServletException {
        this.stationService = new StationService(new JdbcStationRepository());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        long routeId = Long.parseLong(req.getParameter("routeId"));

        if ("add".equals(action)) {
            // ... существующий код добавления ...
            String name = req.getParameter("name");
            StationType type = StationType.valueOf(req.getParameter("type"));
            
            LocalTime arrival = (req.getParameter("arrivalTime") != null && !req.getParameter("arrivalTime").isEmpty()) 
                                ? LocalTime.parse(req.getParameter("arrivalTime")) : null;
            LocalTime departure = (req.getParameter("departureTime") != null && !req.getParameter("departureTime").isEmpty()) 
                                ? LocalTime.parse(req.getParameter("departureTime")) : null;

            int dayOffset = Integer.parseInt(req.getParameter("dayOffset"));
            stationService.addStation(routeId, name, arrival, departure, dayOffset, type);

        } else if ("update".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            String name = req.getParameter("name");
            
            LocalTime arrival = (req.getParameter("arrivalTime") != null && !req.getParameter("arrivalTime").isEmpty()) 
                                ? LocalTime.parse(req.getParameter("arrivalTime")) : null;
            LocalTime departure = (req.getParameter("departureTime") != null && !req.getParameter("departureTime").isEmpty()) 
                                ? LocalTime.parse(req.getParameter("departureTime")) : null;
            
            int dayOffset = Integer.parseInt(req.getParameter("dayOffset"));

            stationService.updateStation(id, name, arrival, departure, dayOffset);

        } else if ("delete".equals(action)) {
            long stationId = Long.parseLong(req.getParameter("id"));
            stationService.deleteStation(stationId);
        }

        resp.sendRedirect("route-details?id=" + routeId);
    }
}
