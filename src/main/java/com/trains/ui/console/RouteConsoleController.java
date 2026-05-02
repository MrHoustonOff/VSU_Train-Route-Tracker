package com.trains.ui.console;

import com.trains.model.Route;
import com.trains.service.RouteService;
import java.util.List;
import java.util.Scanner;

public class RouteConsoleController {

  private final RouteService routeService;
  private final Scanner scanner;

  public RouteConsoleController(RouteService routeService, Scanner scanner) {
    this.routeService = routeService;
    this.scanner = scanner;
  }

  public void listAll() {
    List<Route> routes = routeService.getAllRoutes();
    System.out.println();
    System.out.println("╔══════════════════════════════════════════════════════╗");
    System.out.println("║                   ВСЕ МАРШРУТЫ                       ║");
    System.out.println("╚══════════════════════════════════════════════════════╝");
    System.out.println();
    if (routes.isEmpty()) {
      System.out.println("  Маршрутов пока нет.");
      return;
    }
    System.out.println("  ID │ Номер поезда │ Название маршрута");
    System.out.println(" ────┼──────────────┼───────────────────────────────");
    for (Route r : routes) {
      System.out.printf("  %2d │ %-12s │ %s%n", r.getId(), r.getTrainNumber(), r.getName());
    }
    System.out.printf("%nНайдено маршрутов: %d%n", routes.size());
  }

  public void viewOne() {
    System.out.print("\n  ID маршрута: ");
    long id = readLong();
    Route route = routeService.getRouteWithStations(id);
    if (route == null) {
      System.out.printf("%n  [!] Маршрут с ID %d не найден.%n", id);
      return;
    }
    System.out.println();
    System.out.printf("╔══════════════════════════════════════════════════════╗%n");
    System.out.printf("║         МАРШРУТ: %-34s║%n", route.getName());
    System.out.printf("╚══════════════════════════════════════════════════════╝%n");
    System.out.printf("%n  Поезд №%s%n%n", route.getTrainNumber());
    if (route.getStations().isEmpty()) {
      System.out.println("  Станций нет.");
      return;
    }
    System.out.println("  № │ Станция          │ Прибытие │ Отправление │ Тип");
    System.out.println(" ───┼──────────────────┼──────────┼─────────────┼──────────────");
    int num = 1;
    for (var s : route.getStations()) {
      String arr = s.getArrivalTime() != null ? s.getArrivalTime() : "   —  ";
      String dep = s.getDepartureTime() != null ? s.getDepartureTime() : "    —   ";
      String type = formatType(s.getType());
      System.out.printf(" %2d  │ %-16s │  %6s  │   %5s     │ %s%n",
          num++, s.getName(), arr, dep, type);
    }
  }

  public void create() {
    System.out.println("\n  Создание маршрута");
    System.out.print("  Номер поезда: ");
    String number = scanner.nextLine().trim();
    System.out.print("  Название маршрута: ");
    String name = scanner.nextLine().trim();
    Route created = routeService.createRoute(number, name);
    System.out.printf("%n  [✓] Маршрут создан. ID: %d%n", created.getId());
  }

  public void delete() {
    System.out.print("\n  ID маршрута для удаления: ");
    long id = readLong();
    if (routeService.getRouteWithStations(id) == null) {
      System.out.printf("%n  [!] Маршрут с ID %d не найден.%n", id);
      return;
    }
    routeService.deleteRoute(id);
    System.out.println("\n  [✓] Маршрут удалён.");
  }

  private String formatType(com.trains.model.StationType type) {
    return switch (type) {
      case DEPARTURE -> "Отправление";
      case INTERMEDIATE -> "Промежуточная";
      case ARRIVAL -> "Прибытие";
    };
  }

  // читаем long, при ошибке возвращаем -1
  private long readLong() {
    try {
      return Long.parseLong(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}
