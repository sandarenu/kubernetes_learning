#!/bin/bash
set -e

if [ ! -z "$MYSQL_ROOT_PASSWORD" ]; then
    mysql -uroot -p$MYSQL_ROOT_PASSWORD <<EOSQL

    DROP DATABASE IF EXISTS keycloak;
    CREATE DATABASE keycloak;

    DROP DATABASE IF EXISTS app_db;
    CREATE DATABASE app_db;

    DROP DATABASE IF EXISTS app_db_test;
    CREATE DATABASE app_db_test;

    CREATE USER 'user'@'%' IDENTIFIED BY 'password';
    GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' WITH GRANT OPTION;

EOSQL
else
  echo "MYSQL_ROOT_PASSWORD is not set"
  exit 1
fi
