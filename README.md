# food-inventory
This is the final project for ATTSW exam.

Build Status
[![Build Status](https://travis-ci.com/em-gei/food-inventory.svg?branch=develop)](https://travis-ci.com/em-gei/food-inventory)

Code Coverage Status
[![Coverage Status](https://coveralls.io/repos/github/em-gei/food-inventory/badge.svg?branch=develop)](https://coveralls.io/github/em-gei/food-inventory?branch=develop)

Quality Gate Status
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=em-gei_food-inventory&metric=alert_status)](https://sonarcloud.io/dashboard?id=em-gei_food-inventory)


This application run on a local mysql database, these are the possible run configurations:
* Download and run a local mysql db (NOT RECOMMENDED)
    * Run a mysql db in a docker container and launch application normally: 
    * docker run --name=mysql-local -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_food -e MYSQL_USER=springUser -e MYSQL_PASSWORD=springPwd --restart on-failure -d mysql/mysql-server:8.0
* Run both db and application in separate docker containers:
    * [Create docker network]: docker network create food-mysql
    * [Run mysql docker container]: docker run --name=mysql1 --network food-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_food -e MYSQL_USER=springUser -e MYSQL_PASSWORD=springPwd --restart on-failure -d mysql/mysql-server:8.0
    * [Build application docker image]: docker build --build-arg jarToCopy=foodinventory-0.0.1-SNAPSHOT.jar -t foodinventory-image .
    * [Run application docker container]: docker run --network food-mysql --name foodinventory-container -p 8080:8080 -d foodinventory-image
* Run application and db together with docker compose: 
    * docker-compose up