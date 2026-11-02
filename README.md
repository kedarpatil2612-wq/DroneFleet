# 🚁 DroneFleet — Intelligent Drone Management & Fleet Simulation

A Java-based desktop application for managing drone fleets, scheduling missions, monitoring telemetry, and planning routes using the A* pathfinding algorithm.

![Java](https://img.shields.io/badge/Java-17-orange)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36)
![Status](https://img.shields.io/badge/Project-Mini%20Project-success)

## 📌 Project Overview

DroneFleet is an educational drone fleet management and simulation system built using Java, Swing, JDBC, MySQL, and Maven.

It provides a desktop interface to register drones, manage missions, simulate telemetry, and explore route planning.

**Operating Mode:** Simulation only. This project does not control real drones.

## ✨ Features

- Drone registration, editing, listing, and deletion.
- Battery level and drone status monitoring.
- Mission creation and drone assignment.
- Mission lifecycle management.
- Telemetry simulation.
- A* grid-based route planning.
- Route proximity and illustrative safety checks.
- MySQL database integration.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core application |
| Java Swing | Desktop GUI |
| JDBC | Database connectivity |
| MySQL 8 | Data storage |
| Maven | Build and dependency management |
| A* Algorithm | Route planning |

## 📸 Application Screenshots

### Dashboard
![Dashboard](screenshots/dashboard.png)

### Drone Management
![Drone Management](screenshots/drone-management.png)

### Mission Management
![Missions](screenshots/missions.png)

### Telemetry Simulator
![Telemetry](screenshots/telemetry.png)

### A* Route Planner
![Route Planner](screenshots/route-planner.png)

## 🗄️ Database Structure

Database: `dronefleet_db`

Tables:
- `drones`
- `missions`
- `waypoints`
- `telemetry`

Database schema is available in `database/schema.sql`.

## ⚙️ Requirements

- JDK 17 or compatible
- Apache Maven
- MySQL Server 8.x
- Visual Studio Code or another Java IDE

## 🚀 Installation & Setup

1. Clone this repository:

   ```bash
   git clone https://github.com/YOUR_USERNAME/DroneFleet.git
   ```

2. Open the project directory:

   ```bash
   cd DroneFleet
   ```

3. Create the database by executing:

   `database/schema.sql`

4. Configure the database environment variables in PowerShell:

   ```powershell
   $env:DRONE_DB_USER="root"
   $env:DRONE_DB_PASSWORD="YOUR_MYSQL_PASSWORD"
   ```

5. Compile the application:

   ```bash
   mvn clean compile
   ```

6. Run the application:

   ```bash
   mvn exec:java
   ```

## 🧪 Testing

The project has been compiled successfully using Maven with Java 17.

Further functional testing should be performed for individual modules.

## 🔮 Future Scope

- Interactive map-based drone tracking.
- Advanced mission optimization.
- Enhanced collision-risk simulation.
- Real-time telemetry visualization.
- Role-based access and reporting.

## ⚠️ Disclaimer

DroneFleet is an educational simulation project. It is not certified for real-world drone navigation, flight control, or aviation safety decisions.

## 👨‍💻 Developer

**Kedar Patil**

Artificial Intelligence & Data Science

---

⭐ If you find this project interesting, consider giving the repository a star.