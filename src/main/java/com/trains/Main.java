package com.trains;

import com.trains.repository.RouteRepository;
import com.trains.repository.StationRepository;
import com.trains.repository.impl.memory.InMemoryRouteRepository;
import com.trains.repository.impl.memory.InMemoryStationRepository;
import com.trains.service.RouteService;
import com.trains.service.StationService;
import com.trains.ui.console.ConsoleMenu;
import com.trains.ui.console.RouteConsoleController;
import com.trains.ui.console.StationConsoleController;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    // репозитории
    RouteRepository routeRepo = new InMemoryRouteRepository();
    StationRepository stationRepo = new InMemoryStationRepository();

    // сервисы
    RouteService routeService = new RouteService(routeRepo, stationRepo);
    StationService stationService = new StationService(stationRepo);

    // UI
    Scanner scanner = new Scanner(System.in);
    RouteConsoleController routeController = new RouteConsoleController(routeService, scanner);
    StationConsoleController stationController =
        new StationConsoleController(stationService, routeService, scanner);
    ConsoleMenu menu = new ConsoleMenu(routeController, stationController, scanner);

    menu.start();
  }
}
