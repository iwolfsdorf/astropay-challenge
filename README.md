# Astro Pay Challenge

## Requirements
- Linux console (In Windows SO you can use Git Bash)
- Postman
- Maven (You can install maven with [SdkMan](https://sdkman.io))
- Java 11 (You can install java with [SdkMan](https://sdkman.io))
- Postgres DB Installed

## Usage

1. Download code from [astropay-challenge](https://github.com/iwolfsdorf/astropay-challenge)
2. Go to `$ ~/astropay-challenge/`
3. Run `$ mvn clean install -U`
4. Create a database and user to access in postgres db
5. Edit `$ ~/astropay-challenge/src/main/resources/appilcation.properties` with your configuration 
6. Run `$ mvn spring-boot:run`
7. In Postman import collection from astropay-challenge.postman_collection.json 
8. Execute request **GetResults** or **Sum two numbers with percentage**