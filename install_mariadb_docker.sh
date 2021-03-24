#!/bin/bash
docker pull mariadb
docker run -d -p 33306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name mariadb mariadb
docker ls -a
