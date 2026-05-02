package com.trains.repository.impl.memory;

import com.trains.model.Station;
import com.trains.repository.StationRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryStationRepository implements StationRepository {

  private final Map<Long, Station> store = new HashMap<>();
  private final AtomicLong idSeq = new AtomicLong(1);

  @Override
  public List<Station> findByRouteId(long routeId) {
    return store.values().stream()
        .filter(s -> s.getRouteId() == routeId)
        .sorted((a, b) -> Integer.compare(a.getOrderIndex(), b.getOrderIndex()))
        .collect(Collectors.toList());
  }

  @Override
  public Station findById(long id) {
    return store.get(id);
  }

  @Override
  public Station save(Station station) {
    if (station.getId() == 0) {
      station.setId(idSeq.getAndIncrement());
    }
    store.put(station.getId(), station);
    return station;
  }

  @Override
  public Station update(Station station) {
    store.put(station.getId(), station);
    return station;
  }

  @Override
  public void delete(long id) {
    store.remove(id);
  }
}
