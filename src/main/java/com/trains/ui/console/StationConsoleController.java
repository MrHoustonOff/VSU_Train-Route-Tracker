package com.trains.ui.console;

import com.trains.model.Station;
import com.trains.model.StationType;
import com.trains.service.RouteService;
import com.trains.service.StationService;
import java.util.List;
import java.util.Scanner;

public class StationConsoleController {

  private final StationService stationService;
  private final RouteService routeService;
  private final Scanner scanner;

  public StationConsoleController(StationService stationService,
      RouteService routeService, Scanner scanner) {
    this.stationService = stationService;
    this.routeService = routeService;
    this.scanner = scanner;
  }

  public void manage() {
    System.out.print("\n  ID маршрута: ");
    long routeId = readLong();
    var route = routeService.getRouteWithStations(routeId);
    if (route == null) {
      System.out.printf("%n  [!] Маршрут с ID %d не найден.%n", routeId);
      return;
    }

    while (true) {
      System.out.println();
      System.out.println("╔══════════════════════════════════════╗");
      System.out.println("║        УПРАВЛЕНИЕ СТАНЦИЯМИ          ║");
      System.out.println("╚══════════════════════════════════════╝");
      System.out.printf("%n  Маршрут: %s (Поезд №%s)%n%n", route.getName(), route.getTrainNumber());
      System.out.println("  1. Добавить станцию");
      System.out.println("  2. Изменить станцию");
      System.out.println("  3. Удалить станцию");
      System.out.println("  0. Назад");
      System.out.println();
      System.out.print("Введите команду: ");

      String cmd = scanner.nextLine().trim();
      switch (cmd) {
        case "1" -> addStation(routeId);
        case "2" -> updateStation(routeId);
        case "3" -> deleteStation(routeId);
        case "0" -> { return; }
        default -> System.out.println("\n  [!] Неизвестная команда.");
      }
    }
  }

  private void addStation(long routeId) {
    System.out.println("\n  Добавление станции");
    System.out.print("  Название: ");
    String name = scanner.nextLine().trim();

    System.out.println("  Тип станции: 1-Отправление  2-Промежуточная  3-Прибытие");
    System.out.print("  Выбор: ");
    StationType type = parseType(scanner.nextLine().trim());

    String arrivalTime = null;
    String departureTime = null;

    if (type != StationType.DEPARTURE) {
      System.out.print("  Время прибытия (HH:mm): ");
      arrivalTime = scanner.nextLine().trim();
    }
    if (type != StationType.ARRIVAL) {
      System.out.print("  Время отправления (HH:mm): ");
      departureTime = scanner.nextLine().trim();
    }

    stationService.addStation(routeId, name, arrivalTime, departureTime, type);
    System.out.println("\n  [✓] Станция добавлена.");
  }

  private void updateStation(long routeId) {
    printStationList(routeId);
    System.out.print("\n  ID станции для изменения: ");
    long id = readLong();
    System.out.print("  Новое название: ");
    String name = scanner.nextLine().trim();
    System.out.print("  Время прибытия (HH:mm, Enter — оставить): ");
    String arr = scanner.nextLine().trim();
    System.out.print("  Время отправления (HH:mm, Enter — оставить): ");
    String dep = scanner.nextLine().trim();

    var updated = stationService.updateStation(id,
        name,
        arr.isEmpty() ? null : arr,
        dep.isEmpty() ? null : dep);

    if (updated == null) {
      System.out.printf("%n  [!] Станция с ID %d не найдена.%n", id);
    } else {
      System.out.println("\n  [✓] Станция обновлена.");
    }
  }

  private void deleteStation(long routeId) {
    printStationList(routeId);
    System.out.print("\n  ID станции для удаления: ");
    long id = readLong();
    stationService.deleteStation(id);
    System.out.println("\n  [✓] Станция удалена.");
  }

  private void printStationList(long routeId) {
    List<Station> stations = stationService.getStationsForRoute(routeId);
    System.out.println();
    System.out.println("  ID │ № │ Название");
    System.out.println(" ────┼───┼──────────────────");
    for (Station s : stations) {
      System.out.printf("  %2d │ %d │ %s%n", s.getId(), s.getOrderIndex() + 1, s.getName());
    }
  }

  private StationType parseType(String input) {
    return switch (input) {
      case "1" -> StationType.DEPARTURE;
      case "3" -> StationType.ARRIVAL;
      default -> StationType.INTERMEDIATE;
    };
  }

  private long readLong() {
    try {
      return Long.parseLong(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}
