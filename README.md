# ElectricityTimer

Calculates the times when it is most affordable to use electricity. 
The main parameter is the minutes of use you need in a day.
The response is a list of times when it is cheapest to use that amount of minutes and also if now is the time to use electricity.
The electricity prices are fetched from ENTSO-E integration (https://github.com/iivorait/ENTSO-E-integration).

The GET parameters in a query are:

- minutes, how many minutes of electricity use per day is needed
- blocks (optional), in how many equally sized segments the usage is spread - if only one block, the electricity usage will probably all be at night
- cheapmode (optional), if using blocks, then the division of minutes with block count might produce additional timeslots that are scraped in cheapmode, but the location of the scraped timeslots may be suboptimal
- minprice (optional), if the price is equal or below a given threshold, use the electricity anyways - the unit is given by ENTSO-E integration, so for example EUR/MWh

Example query:

```
GET http://localhost:8080/calculate?minutes=480&blocks=8&cheapmode=0&minprice=2.5
```

The settings in the application.properties file are:

- the ENTSO-E areacode from the list https://transparency.entsoe.eu/content/static_content/Static%20content/web%20api/Guide.html#_areas, read from the AREACODE environment variable
- the ENTSO-E integration URL, read from the ENTSOE_INTEGRATION_URL environment variable

# Quarkus boilerplate instructions

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application with Dockerfile

Build with Docker:
```shell script
docker build -t electricitytimer .
```

Run with Docker:
```shell script
docker run --name electricitytimer electricitytimer
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.
