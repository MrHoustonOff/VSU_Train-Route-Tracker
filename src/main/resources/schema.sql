CREATE TABLE IF NOT EXISTS routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_number VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS stations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    arrival_time TIME,
    departure_time TIME,
    order_index INT NOT NULL,
    day_offset INT NOT NULL DEFAULT 0,
    type VARCHAR(20) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);
