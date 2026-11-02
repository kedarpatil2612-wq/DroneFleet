CREATE DATABASE IF NOT EXISTS dronefleet_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE dronefleet_db;

CREATE TABLE IF NOT EXISTS drones (
    drone_id INT AUTO_INCREMENT PRIMARY KEY,
    drone_code VARCHAR(30) NOT NULL UNIQUE,
    model VARCHAR(80) NOT NULL,
    battery_level INT NOT NULL DEFAULT 100 CHECK (battery_level BETWEEN 0 AND 100),
    status ENUM('AVAILABLE','IN_MISSION','CHARGING','MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
    max_payload_kg DECIMAL(8,2) NOT NULL DEFAULT 1.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS missions (
    mission_id INT AUTO_INCREMENT PRIMARY KEY,
    mission_name VARCHAR(120) NOT NULL,
    mission_type VARCHAR(60) NOT NULL,
    destination VARCHAR(160) NOT NULL,
    assigned_drone_id INT NULL,
    priority ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL DEFAULT 'MEDIUM',
    status ENUM('PLANNED','ASSIGNED','IN_PROGRESS','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PLANNED',
    scheduled_at DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mission_drone FOREIGN KEY (assigned_drone_id)
      REFERENCES drones(drone_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS waypoints (
    waypoint_id INT AUTO_INCREMENT PRIMARY KEY,
    mission_id INT NOT NULL,
    sequence_no INT NOT NULL,
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    altitude_m DECIMAL(8,2) NOT NULL DEFAULT 30.00,
    CONSTRAINT fk_waypoint_mission FOREIGN KEY (mission_id)
      REFERENCES missions(mission_id) ON DELETE CASCADE,
    UNIQUE KEY uq_mission_sequence (mission_id, sequence_no)
);

CREATE TABLE IF NOT EXISTS telemetry (
    telemetry_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drone_id INT NOT NULL,
    battery_level INT NOT NULL,
    altitude_m DECIMAL(8,2) NOT NULL,
    speed_kmh DECIMAL(8,2) NOT NULL,
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_telemetry_drone FOREIGN KEY (drone_id)
      REFERENCES drones(drone_id) ON DELETE CASCADE,
    INDEX idx_telemetry_drone_time (drone_id, recorded_at)
);

-- Optional demo records
INSERT IGNORE INTO drones (drone_code, model, battery_level, status, max_payload_kg)
VALUES
('DF-001','AeroScout X1',92,'AVAILABLE',2.50),
('DF-002','SkyMapper Pro',68,'AVAILABLE',1.80),
('DF-003','CargoLift Mini',35,'CHARGING',5.00);
