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

## To run as a docker image & use an external save file (data persists even when image is terminated)

```
docker run -p 8080:8080 -v /home/mbmartinez/test/data.json:/data.json markmfreelancer/docker-based-logger-simulator:2.0
```

Note that ```/home/mbmartinez/test/data.json``` needs to be replaced with your own data.json absolute path.

## To run as a docker image & override spring-boot configuration
Similar to mounting data.json, it's also possible to mount a custom application.yml configuration file as follows:

```
docker run -p 8080:8080 -v /home/mbmartinez/test/application.yml:/app/application.yml markmfreelancer/docker-based-logger-simulator:2.0
```

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

Changing the values of these properties will change the output of the affected endpoints.

## To run with all configuration options
The above commands can be combined to run the docker image with all configuration options activated as follows:
```
docker run -p 8080:8080 -e DEVICE_STATUS=NOK -e DEVICE_UUID=Image15 -v /home/mbmartinez/test/data.json:/data.json -v /home/mbmartinez/test/application.yml:/app/application.yml markmfreelancer/docker-based-logger-simulator:2.0
```
As before, the ```/home/mbmartinez/test``` must be replaced with your own absolute path with the locations of ```data.json``` and ```application.yml```.

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
    "id": "6574444a-5b10-4b0b-9458-682e51736733",
    "status": "OK"
}
```

2. GET /current_state
```
curl localhost:8080/current_state
```
Expected response:
```json
{
    "CurrentState": {
        "DigitalInput": [
            {
                "Name": "DI1",
                "Value": "0"
            },
            {
                "Name": "DI2",
                "Value": "0"
            },
            {
                "Name": "DI3",
                "Value": "0"
            },
            {
                "Name": "DI4",
                "Value": "0"
            },
            {
                "Name": "DI5",
                "Value": "0"
            },
            {
                "Name": "DI6",
                "Value": "0"
            },
            {
                "Name": "DI7",
                "Value": "0"
            },
            {
                "Name": "DI8",
                "Value": "0"
            },
            {
                "Name": "DI9",
                "Value": "0"
            },
            {
                "Name": "DI10",
                "Value": "0"
            },
            {
                "Name": "DI11",
                "Value": "0"
            },
            {
                "Name": "DI12",
                "Value": "0"
            },
            {
                "Name": "DI13",
                "Value": "0"
            },
            {
                "Name": "DI14",
                "Value": "0"
            },
            {
                "Name": "DI15",
                "Value": "0"
            }
        ],
        "AnalogInput": [
            {
                "Name": "AI1",
                "Value": "94"
            },
            {
                "Name": "AI2",
                "Value": "817"
            },
            {
                "Name": "AI3",
                "Value": "0"
            },
            {
                "Name": "AI4",
                "Value": "0"
            },
            {
                "Name": "AI5",
                "Value": "0"
            },
            {
                "Name": "AI6",
                "Value": "0"
            },
            {
                "Name": "AI7",
                "Value": "0"
            }
        ],
        "TemperatureInput": [
            {
                "Name": "TI1",
                "Value": ""
            },
            {
                "Name": "TI2",
                "Value": ""
            },
            {
                "Name": "TI3",
                "Value": ""
            },
            {
                "Name": "TI4",
                "Value": ""
            },
            {
                "Name": "TI5",
                "Value": ""
            },
            {
                "Name": "TI6",
                "Value": ""
            },
            {
                "Name": "TI7",
                "Value": ""
            }
        ],
        "Device": {
            "Name": "SIMULATION_LOGGER",
            "MAC": "75-10-0B-0E-C8-0E"
        }
    }
}
```
3. POST /new_shifts
```
curl --location --request POST 'http://localhost:8080/new_shifts' \
--header 'Content-Type: application/json' \
--data-raw '{
  "shiftOneStart": "01:00",
  "shiftOneEnd": "02:00",
  "shiftTwoStart": "03:00",
  "shiftTwoEnd": "04:00",
  "shiftThreeStart": "23:00",
  "shiftThreeEnd": "02:30"
}'
```
Expected response:
```json
{
  "status":"OK"
}
```
4. POST /new_timing
```
curl --location --request POST 'http://172.16.0.10:8080/new_timing' \
--header 'Content-Type: application/json' \
--data-raw '{
  "newTiming": 10000
}'
```
Expected response:
```json
{
  "status": "OK"
}
```
