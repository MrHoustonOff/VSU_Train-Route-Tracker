package com.trains.repository.impl.memory;

import com.trains.model.Route;
import com.trains.repository.RouteRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryRouteRepository implements RouteRepository {

  private final Map<Long, Route> store = new HashMap<>();
  private final AtomicLong idSeq = new AtomicLong(1);

  @Override
  public List<Route> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public Route findById(long id) {
    return store.get(id);
  }

  @Override
  public Route save(Route route) {
    if (route.getId() == 0) {
      route.setId(idSeq.getAndIncrement());
    }
    store.put(route.getId(), route);
    return route;
  }

  @Override
  public void delete(long id) {
    store.remove(id);
  }
}
