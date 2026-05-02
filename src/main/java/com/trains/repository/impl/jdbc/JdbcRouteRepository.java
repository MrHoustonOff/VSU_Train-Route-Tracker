package com.trains.repository.impl.jdbc;

import com.trains.model.Route;
import com.trains.repository.RouteRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcRouteRepository implements RouteRepository {

  @Override
  public List<Route> findAll() {
    List<Route> routes = new ArrayList<>();
    String sql = "SELECT id, train_number, name FROM routes ORDER BY id";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
         
      while (rs.next()) {
        routes.add(mapRowToRoute(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return routes;
  }

  @Override
  public Route findById(long id) {
    String sql = "SELECT id, train_number, name FROM routes WHERE id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
      pstmt.setLong(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToRoute(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Route save(Route route) {
    if (route.getId() == 0) {
      String sql = "INSERT INTO routes (train_number, name) VALUES (?, ?)";
      try (Connection conn = DatabaseConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           
        pstmt.setString(1, route.getTrainNumber());
        pstmt.setString(2, route.getName());
        pstmt.executeUpdate();
        
        try (ResultSet keys = pstmt.getGeneratedKeys()) {
          if (keys.next()) {
            route.setId(keys.getLong(1));
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      String sql = "UPDATE routes SET train_number = ?, name = ? WHERE id = ?";
      try (Connection conn = DatabaseConnection.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
        pstmt.setString(1, route.getTrainNumber());
        pstmt.setString(2, route.getName());
        pstmt.setLong(3, route.getId());
        pstmt.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return route;
  }

  @Override
  public void delete(long id) {
    String sql = "DELETE FROM routes WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
      pstmt.setLong(1, id);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private Route mapRowToRoute(ResultSet rs) throws SQLException {
    Route route = new Route();
    route.setId(rs.getLong("id"));
    route.setTrainNumber(rs.getString("train_number"));
    route.setName(rs.getString("name"));
    return route;
  }
}
