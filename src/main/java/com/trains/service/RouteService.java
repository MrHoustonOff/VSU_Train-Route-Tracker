package com.trains.service;

import com.trains.model.Route;
import com.trains.model.Station;
import com.trains.repository.RouteRepository;
import com.trains.repository.StationRepository;
import java.util.List;

public class RouteService {

  private final RouteRepository routeRepo;
  private final StationRepository stationRepo;

  public RouteService(RouteRepository routeRepo, StationRepository stationRepo) {
    this.routeRepo = routeRepo;
    this.stationRepo = stationRepo;
  }

  public Route createRoute(String trainNumber, String name) {
    Route route = new Route();
    route.setTrainNumber(trainNumber);
    route.setName(name);
    return routeRepo.save(route);
  }

  public void deleteRoute(long id) {
    // удаляем все станции маршрута перед удалением самого маршрута
    List<Station> stations = stationRepo.findByRouteId(id);
    for (Station s : stations) {
      stationRepo.delete(s.getId());
    }
    routeRepo.delete(id);
  }

  public List<Route> getAllRoutes() {
    return routeRepo.findAll();
  }

  public Route getRouteWithStations(long id) {
    Route route = routeRepo.findById(id);
    if (route == null) {
      return null;
    }
    route.setStations(stationRepo.findByRouteId(id));
    return route;
  }
}
