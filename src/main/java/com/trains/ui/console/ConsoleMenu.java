package com.trains.ui.console;

import java.util.Scanner;

public class ConsoleMenu {

  private final RouteConsoleController routeController;
  private final StationConsoleController stationController;
  private final Scanner scanner;

  public ConsoleMenu(RouteConsoleController routeController,
      StationConsoleController stationController, Scanner scanner) {
    this.routeController = routeController;
    this.stationController = stationController;
    this.scanner = scanner;
  }

  public void start() {
    while (true) {
      printMainMenu();
      String input = scanner.nextLine().trim();
      switch (input) {
        case "1" -> routeController.listAll();
        case "2" -> routeController.viewOne();
        case "3" -> routeController.create();
        case "4" -> routeController.delete();
        case "5" -> stationController.manage();
        case "0" -> {
          System.out.println("\n  До свидания!\n");
          return;
        }
        default -> System.out.println("\n  [!] Неизвестная команда.\n");
      }
    }
  }

  private void printMainMenu() {
    System.out.println();
    System.out.println("╔══════════════════════════════════════╗");
    System.out.println("║       УЧЁТ МАРШРУТОВ ПОЕЗДОВ        ║");
    System.out.println("╚══════════════════════════════════════╝");
    System.out.println();
    System.out.println("  1. Все маршруты");
    System.out.println("  2. Просмотр маршрута");
    System.out.println("  3. Создать маршрут");
    System.out.println("  4. Удалить маршрут");
    System.out.println("  5. Управление станциями");
    System.out.println("  0. Выход");
    System.out.println();
    System.out.print("Введите команду: ");
  }
}
