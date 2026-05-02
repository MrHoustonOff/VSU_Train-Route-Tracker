package com.trains.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
  private long id;
  private String trainNumber;
  private String name;
  private List<Station> stations = new ArrayList<>();
}
