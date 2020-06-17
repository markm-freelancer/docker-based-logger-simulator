# Docker-based Logger Simulator

## To run as a docker image
```
docker run -p 8080:8080 markmfreelancer/docker-based-logger-simulator:2.0
```
## To run as a docker image with environment variables set
There are 2 environment variables used by this image:
- DEVICE_UUID
- DEVICE_STATUS

These variables affect the return value of the ```GET /logger_status``` endpoint. i.e. The default response is

```json
{
    "id": "6574444a-5b10-4b0b-9458-682e51736733",
    "status": "OK"
}
```

However, by running this image with environment variables set as follows,
```
docker run -p 8080:8080 -e DEVICE_STATUS=NOK -e DEVICE_UUID=Image15 markmfreelancer/docker-based-logger-simulator:2.0
```

The response changes to

```json
{
    "id": "Image15",
    "status": "NOK"
}
```

## To run as a docker image & override spring-boot configuration
The following is the default configuration of the application:
```yaml
configuration:
  data-filepath: data.json
  generator-interval: 1000
  DEVICE_UUID: 6574444a-5b10-4b0b-9458-682e51736733
  DEVICE_STATUS: OK
  DEVICE_MAC_ADDRESS: 75-10-0B-0E-C8-0E
```

| Property | Description |
|----------|-------------|
|data-filepath|The internal path of the file used to save persistent data|
|generator-interval|The default generator interval, used by the generated values generator used by the ```GET /current_state``` endpoint|
|DEVICE_UUID|The fallback Device ID if the environment variable DEVICE_UUID is not set at runtime|
|DEVICE_STATUS|The fallback Device status if the environment variable DEVICE_STATUS is not set at runtime|
|DEVICE_NAME|The name returned by the ```GET /current_state``` endpoint as Device.Name|
|DEVICE_MAC_ADDRESS|The mac address returned by the ```GET /current_state``` endpoint as Device.MAC|

In order to override these properties, an external configuration file must be passed to the docker image at runtime as follows:

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
