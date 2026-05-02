package com.trains.repository.impl.jdbc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseConnection {

  // Храним базу в файле (в корне проекта, папка db)
  private static final String URL = "jdbc:h2:./db/train_routes;DB_CLOSE_DELAY=-1";
  private static final String USER = "sa";
  private static final String PASSWORD = "";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  public static void initDatabase() {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         InputStream is = DatabaseConnection.class.getResourceAsStream("/schema.sql")) {
      
      if (is == null) {
        System.err.println("schema.sql не найден!");
        return;
      }
      
      String sql;
      try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
        sql = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
      }
      
      if (!sql.isEmpty()) {
        stmt.execute(sql);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
