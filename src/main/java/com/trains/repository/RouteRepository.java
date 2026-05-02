package com.trains.repository;

import com.trains.model.Route;
import java.util.List;

public interface RouteRepository {
  List<Route> findAll();
  Route findById(long id);
  Route save(Route route);
  void delete(long id);
}
