# User service

This is User service for [Qaprotours](https://github.com/ilisau/qaprotours).

This service uses Reactive Postgres Client for database interaction and provides Reactive approach.

This application provides:

1) Resilience4j Circuit Breaker

We use Circuit Breaker to wrap function calls and return default data instead
of throwing exceptions if supplier service is unavailable. This approach helps to avoid falling of request.
After some requests are fallen, the status of breaker is changed and custom handling is used until supplier can't
respond to requests.

You can change Circuit Breaker configuration in ```resilience4j.circuitbreaker``` property.

2) Kafka

We use Kafka here for sending messages to mail client instead of blocking rest approach.


3) Redis cache

We use Redis here for caching users and improving speed of accessing user data.

## Installation

You need to pull the project from the repository and install the dependencies.

This service by default use the following variables from .env file:

1. 8081 - port for the service itself
3. `DB_HOST` - host for the database
4. `MONGO_DB` - database name
5. `MONGO_USERNAME` - database user
6. `MONGO_PASSWORD` - database password
7. `KAFKA_HOST` - host with port for Kafka bootstrap server

To run application you need to run
```console
 sh run.sh
 ```

