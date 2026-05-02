package com.trains.service;

import com.trains.model.Station;
import com.trains.model.StationType;
import com.trains.repository.StationRepository;
import java.time.LocalTime;
import java.util.List;

public class StationService {

  private final StationRepository stationRepo;

  public StationService(StationRepository stationRepo) {
    this.stationRepo = stationRepo;
  }

  public Station addStation(long routeId, String name, LocalTime arrivalTime,
      LocalTime departureTime, int dayOffset, StationType type) {
    List<Station> existing = stationRepo.findByRouteId(routeId);
    int nextIndex = existing.size(); // следующий порядковый номер

    Station station = new Station();
    station.setRouteId(routeId);
    station.setName(name);
    station.setArrivalTime(arrivalTime);
    station.setDepartureTime(departureTime);
    station.setOrderIndex(nextIndex);
    station.setDayOffset(dayOffset);
    station.setType(type);
    return stationRepo.save(station);
  }

  public Station updateStation(long stationId, String name,
      LocalTime arrivalTime, LocalTime departureTime, int dayOffset) {
    Station station = stationRepo.findById(stationId);
    if (station == null) {
      return null;
    }
    station.setName(name);
    station.setArrivalTime(arrivalTime);
    station.setDepartureTime(departureTime);
    station.setDayOffset(dayOffset);
    return stationRepo.update(station);
  }

  public void deleteStation(long stationId) {
    Station target = stationRepo.findById(stationId);
    if (target == null) {
      return;
    }
    stationRepo.delete(stationId);

    // пересчитываем orderIndex у оставшихся станций маршрута
    List<Station> remaining = stationRepo.findByRouteId(target.getRouteId());
    for (int i = 0; i < remaining.size(); i++) {
      remaining.get(i).setOrderIndex(i);
      stationRepo.update(remaining.get(i));
    }
  }

  public List<Station> getStationsForRoute(long routeId) {
    return stationRepo.findByRouteId(routeId);
  }
}
