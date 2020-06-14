# Docker-based Logger Simulator

## To run as a docker image
```
docker run -p 8080:8080 markmfreelancer/docker-based-logger-simulator:1.0
```
## To build
- Required software
   - Java
   - Maven
   - Docker (to build image)
- Build process:
1. Clone the repository
```
git clone https://github.com/markm-freelancer/docker-based-logger-simulator.git
```
2. Build the java code with maven
```
cd docker-based-logger-simulator
mvn clean install
```
3. Build the docker image. This will also push the new image to Dockerhub
```
cd dbls-app
mvn clean install -PbuildImage
```
## To run locally

1. Execute steps 1 & 2 in "To build" section above
2. Start a local spring-boot application:
```
cd dbls-app
mvn spring-boot:run
```

## Endpoints

1. GET /logger_status
```
curl localhost:8080/logger_status
```
Expected response:
```json
{
  "macAddresses": {
    "wifi1": "4C-1D-96-5F-53-3A",
    "wifi2": "4E-1D-96-5F-53-39",
    "wifi0":"4C-1D-96-5F-53-39",
    "eth1":"00-FF-51-0E-32-61",
    "eth0":"F8-75-A4-1D-74-3B"
  }
}
```

2. GET /current_state
```
curl localhost:8080/current_state
```
Expected response:
```json
{
  "status": "Normal",
  "pressure": 12.0,
  "volume": 80.0,
  "weight": 16.0,
  "temperature": 35.5
}
```
3. POST /new_shifts
```
curl -X POST localhost:8080/new_shifts -H 'Content-Type: application/json' -d '{}'
```
Expected response:
```json
{
  "status":"OK"
}
```
4. POST /new_timing
```
curl -X POST localhost:8080/new_timing -H 'Content-Type: application/json' -d '{}'
```
Expected response:
```json
{
  "status":"OK"
}
```
