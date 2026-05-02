package com.trains.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station {
  private long id;
  private long routeId;
  private String name;
  private String arrivalTime;    // null для первой станции
  private String departureTime;  // null для последней станции
  private int orderIndex;        // порядок в маршруте, начиная с 0
  private StationType type;
  private double latitude;       // для карты на этапе 3
  private double longitude;      // для карты на этапе 3
}
