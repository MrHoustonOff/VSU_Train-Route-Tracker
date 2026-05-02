package com.trains.repository.impl.jdbc;

import com.trains.model.Station;
import com.trains.model.StationType;
import com.trains.repository.StationRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcStationRepository implements StationRepository {

  @Override
  public List<Station> findByRouteId(long routeId) {
    List<Station> stations = new ArrayList<>();
    String sql = "SELECT id, route_id, name, arrival_time, departure_time, order_index, day_offset, type, latitude, longitude " +
                 "FROM stations WHERE route_id = ? ORDER BY order_index";
                 
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
      pstmt.setLong(1, routeId);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          stations.add(mapRowToStation(rs));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return stations;
  }

  @Override
  public Station findById(long id) {
    String sql = "SELECT id, route_id, name, arrival_time, departure_time, order_index, day_offset, type, latitude, longitude " +
                 "FROM stations WHERE id = ?";
                 
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
      pstmt.setLong(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToStation(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Station save(Station station) {
    String sql = "INSERT INTO stations (route_id, name, arrival_time, departure_time, order_index, day_offset, type, latitude, longitude) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                 
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         
      setStationParams(pstmt, station);
      pstmt.executeUpdate();
      
      try (ResultSet keys = pstmt.getGeneratedKeys()) {
        if (keys.next()) {
          station.setId(keys.getLong(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return station;
  }

  @Override
  public Station update(Station station) {
    String sql = "UPDATE stations SET route_id = ?, name = ?, arrival_time = ?, departure_time = ?, " +
                 "order_index = ?, day_offset = ?, type = ?, latitude = ?, longitude = ? WHERE id = ?";
                 
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
      setStationParams(pstmt, station);
      pstmt.setLong(10, station.getId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return station;
  }

  @Override
  public void delete(long id) {
    String sql = "DELETE FROM stations WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
      pstmt.setLong(1, id);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void setStationParams(PreparedStatement pstmt, Station station) throws SQLException {
    pstmt.setLong(1, station.getRouteId());
    pstmt.setString(2, station.getName());
    
    if (station.getArrivalTime() != null) {
      pstmt.setTime(3, Time.valueOf(station.getArrivalTime()));
    } else {
      pstmt.setNull(3, java.sql.Types.TIME);
    }
    
    if (station.getDepartureTime() != null) {
      pstmt.setTime(4, Time.valueOf(station.getDepartureTime()));
    } else {
      pstmt.setNull(4, java.sql.Types.TIME);
    }
    
    pstmt.setInt(5, station.getOrderIndex());
    pstmt.setInt(6, station.getDayOffset());
    pstmt.setString(7, station.getType().name());
    pstmt.setDouble(8, station.getLatitude());
    pstmt.setDouble(9, station.getLongitude());
  }

  private Station mapRowToStation(ResultSet rs) throws SQLException {
    Station station = new Station();
    station.setId(rs.getLong("id"));
    station.setRouteId(rs.getLong("route_id"));
    station.setName(rs.getString("name"));
    
    Time arrTime = rs.getTime("arrival_time");
    if (arrTime != null) {
      station.setArrivalTime(arrTime.toLocalTime());
    }
    
    Time depTime = rs.getTime("departure_time");
    if (depTime != null) {
      station.setDepartureTime(depTime.toLocalTime());
    }
    
    station.setOrderIndex(rs.getInt("order_index"));
    station.setDayOffset(rs.getInt("day_offset"));
    station.setType(StationType.valueOf(rs.getString("type")));
    station.setLatitude(rs.getDouble("latitude"));
    station.setLongitude(rs.getDouble("longitude"));
    
    return station;
  }
}
