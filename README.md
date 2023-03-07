# User service

This is User service for [Qaprotours](https://github.com/ilisau/qaprotours).

This service uses Reactive Postgres Client for database interaction and provides Reactive approach.

## Installation

You need to pull the project from the repository and install the dependencies.

This service by default use the following variables from .env file:

1. 8081 - port for the service itself
2. 8761 - port for the Eureka server
2. `DB_HOST` - host for the database
3. `POSTGRES_DB` - database name
4. `POSTGRES_USERNAME` - database user
5. `POSTGRES_PASSWORD` - database password
