# food-inventory
This is the final project for ATTSW exam.

Build Status
[![Build Status](https://travis-ci.com/em-gei/food-inventory.svg?branch=develop)](https://travis-ci.com/em-gei/food-inventory)

Code Coverage Status
[![Coverage Status](https://coveralls.io/repos/github/em-gei/food-inventory/badge.svg?branch=feature/improve-travis-configuration)](https://coveralls.io/github/em-gei/food-inventory?branch=feature/improve-travis-configuration)

This application run on a local mysql database, these are the possible run configurations:
1) Download and run a local mysql db (NOT RECOMMENDED)
2) Run a mysql db in a docker container and launch application normally: 
  - docker run --name=mysql-local -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_food -e MYSQL_USER=springUser -e MYSQL_PASSWORD=springPwd --restart on-failure -d mysql/mysql-server:8.0
3) Run both db and application in separate docker containers:
  - [Create docker network]: docker network create food-mysql
  - [Run mysql docker container]: docker run --name=mysql1 --network food-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_food -e MYSQL_USER=springUser -e MYSQL_PASSWORD=springPwd --restart on-failure -d mysql/mysql-server:8.0
  - [Build application docker image]: docker build --build-arg jarToCopy=foodinventory-0.0.1-SNAPSHOT.jar -t foodinventory-image .
  - [Run application docker container]: docker run --network food-mysql --name foodinventory-container -p 8080:8080 -d foodinventory-image
4) Run application and db together with docker compose: 
  - docker-compose up
