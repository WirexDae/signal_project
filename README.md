# Cardio Data Simulator

The Cardio Data Simulator is a Java-based application designed to simulate real-time cardiovascular data for multiple patients. This tool is particularly useful for educational purposes, enabling students to interact with real-time data streams of ECG, blood pressure, blood saturation, and other cardiovascular signals.

## Features

- Simulate real-time ECG, blood pressure, blood saturation, and blood levels data.
- Supports multiple output strategies:
  - Console output for direct observation.
  - File output for data persistence.
  - WebSocket and TCP output for networked data streaming.
- Configurable patient count and data generation rate.
- Randomized patient ID assignment for simulated data diversity.

## Getting Started

### Prerequisites

- Java JDK 11 or newer.
- Maven for managing dependencies and compiling the application.

### Installation

1. Clone the repository:

   ```sh
   git clone https://github.com/tpepels/signal_project.git
   ```

2. Navigate to the project directory:

   ```sh
   cd signal_project
   ```

3. Compile and package the application using Maven:
   ```sh
   mvn clean package
   ```
   This step compiles the source code and packages the application into an executable JAR file located in the `target/` directory.

### Running the Simulator

After building the project, you can run the simulator using the packaged JAR:

```sh
mvn clean package
  ```
The executable JAR will be available in:
```sh
bin/6402932_cardio_data_simulator.jar
 ```
Run it using:
```sh
java -jar bin/6402932_cardio_data_simulator.jar
 ```
You can also pass runtime options:
```sh
java -jar bin/6402932_cardio_data_simulator.jar --patient-count
100 --output file:./output
```
### Supported Output Options

- `console`: Directly prints the simulated data to the console.
- `file:<directory>`: Saves the simulated data to files within the specified directory.
- `websocket:<port>`: Streams the simulated data to WebSocket clients connected to the specified port.
- `tcp:<port>`: Streams the simulated data to TCP clients connected to the specified port.

## UML Models

This repository includes UML class diagrams and their relative explanations for the Cardiovascular Health Monitoring System (CHMS).

Location:
- `/uml_models`

Subsystems modeled:
1. Alert Generation System
2. Data Storage System
3. Patient Identification System
4. Data Access Layer

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Project Members

- Student ID: 6402932
