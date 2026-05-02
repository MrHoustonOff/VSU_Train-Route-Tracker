package com.trains.repository;

import com.trains.model.Station;
import java.util.List;

public interface StationRepository {
  List<Station> findByRouteId(long routeId);
  Station findById(long id);
  Station save(Station station);
  Station update(Station station);
  void delete(long id);
}
