package com.trains.util;

import com.trains.model.Route;
import com.trains.model.Station;
import com.trains.model.StationType;
import com.trains.repository.impl.jdbc.DatabaseConnection;
import com.trains.repository.impl.jdbc.JdbcRouteRepository;
import com.trains.repository.impl.jdbc.JdbcStationRepository;
import java.time.LocalTime;

public class DataSeeder {

  public static void main(String[] args) {
    System.out.println("Наполнение базы данных тестовыми данными...");
    DatabaseConnection.initDatabase();

    JdbcRouteRepository routeRepo = new JdbcRouteRepository();
    JdbcStationRepository stationRepo = new JdbcStationRepository();

    // 1. Москва - Санкт-Петербург (Красная Стрела)
    Route r1 = new Route(0, "001А", "Красная Стрела (Москва — С-Петербург)", null);
    routeRepo.save(r1);
    addStation(stationRepo, r1.getId(), "Москва Окт.", null, LocalTime.of(23, 55), 0, 0, StationType.DEPARTURE, 55.7766, 37.6556);
    addStation(stationRepo, r1.getId(), "С-Петербург Глав.", LocalTime.of(7, 55), null, 1, 1, StationType.ARRIVAL, 59.9290, 30.3622);

    // 2. Москва - Владивосток (Россия) - Супер-тест на дни пути
    Route r2 = new Route(0, "062М", "Россия (Москва — Владивосток)", null);
    routeRepo.save(r2);
    addStation(stationRepo, r2.getId(), "Москва Яросл.", null, LocalTime.of(23, 45), 0, 0, StationType.DEPARTURE, 55.7761, 37.6575);
    addStation(stationRepo, r2.getId(), "Владимир", LocalTime.of(2, 30), LocalTime.of(2, 50), 1, 1, StationType.INTERMEDIATE, 56.1291, 40.4070);
    addStation(stationRepo, r2.getId(), "Нижний Новгород", LocalTime.of(6, 10), LocalTime.of(6, 25), 2, 1, StationType.INTERMEDIATE, 56.3269, 44.0059);
    addStation(stationRepo, r2.getId(), "Екатеринбург", LocalTime.of(3, 40), LocalTime.of(4, 10), 3, 2, StationType.INTERMEDIATE, 56.8389, 60.6057);
    addStation(stationRepo, r2.getId(), "Новосибирск", LocalTime.of(0, 30), LocalTime.of(1, 0), 4, 3, StationType.INTERMEDIATE, 55.0084, 82.9357);
    addStation(stationRepo, r2.getId(), "Иркутск", LocalTime.of(6, 40), LocalTime.of(7, 10), 5, 4, StationType.INTERMEDIATE, 52.2870, 104.3050);
    addStation(stationRepo, r2.getId(), "Владивосток", LocalTime.of(6, 10), null, 6, 7, StationType.ARRIVAL, 43.1155, 131.8855);

    // 3. Москва - Адлер (Премиум)
    Route r3 = new Route(0, "102М", "Премиум (Москва — Адлер)", null);
    routeRepo.save(r3);
    addStation(stationRepo, r3.getId(), "Москва Казан.", null, LocalTime.of(14, 40), 0, 0, StationType.DEPARTURE, 55.7744, 37.6583);
    addStation(stationRepo, r3.getId(), "Рязань", LocalTime.of(17, 10), LocalTime.of(17, 15), 1, 0, StationType.INTERMEDIATE, 54.6292, 39.7361);
    addStation(stationRepo, r3.getId(), "Воронеж", LocalTime.of(21, 45), LocalTime.of(21, 50), 2, 0, StationType.INTERMEDIATE, 51.6608, 39.2003);
    addStation(stationRepo, r3.getId(), "Ростов-на-Дону", LocalTime.of(5, 40), LocalTime.of(5, 55), 3, 1, StationType.INTERMEDIATE, 47.2225, 39.7188);
    addStation(stationRepo, r3.getId(), "Сочи", LocalTime.of(13, 20), LocalTime.of(13, 30), 4, 1, StationType.INTERMEDIATE, 43.5855, 39.7231);
    addStation(stationRepo, r3.getId(), "Адлер", LocalTime.of(14, 0), null, 5, 1, StationType.ARRIVAL, 43.4382, 39.9112);

    System.out.println("Готово! База данных наполнена.");
  }

  private static void addStation(JdbcStationRepository repo, long routeId, String name, 
                                 LocalTime arr, LocalTime dep, int index, int day, 
                                 StationType type, double lat, double lon) {
    repo.save(new Station(0, routeId, name, arr, dep, index, day, type, lat, lon));
  }
}
