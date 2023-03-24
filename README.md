# User service

This is User service for [Qaprotours](https://github.com/ilisau/qaprotours).

This service uses Reactive Postgres Client for database interaction and provides Reactive approach.

This application provides:

1) Eureka Service Registry with [Eureka server](https://github.com/ilisau/eureka-server)

We use Service Registry to avoid direct URLs of services via passing requests
to [Eureka server](https://github.com/ilisau/eureka-server) instead.
Services register themselves on server, so server knows addresses of all services.
When we need to call any service we need only know service name, and server
passes request to services itself, so we don't need to know their addresses.

You can change server address in ```eureka.client.server-url.defaultZone``` property.

2) Resilience4j Circuit Breaker

We use Circuit Breaker to wrap function calls and return default data instead
of throwing exceptions if supplier service is unavailable. This approach helps to avoid falling of request.
After some requests are fallen, the status of breaker is changed and custom handling is used until supplier can't
respond to requests.

You can change Circuit Breaker configuration in ```resilience4j.circuitbreaker``` property.

3) Kafka

We use Kafka here for sending messages to mail client instead of blocking rest approach.


4) Redis cache

We use Redis here for caching users and improving speed of accessing user data.

## Installation

You need to pull the project from the repository and install the dependencies.

This service by default use the following variables from .env file:

1. 8081 - port for the service itself
2. 8761 - port for the Eureka server
3. `DB_HOST` - host for the database
4. `MONGO_DB` - database name
5. `MONGO_USERNAME` - database user
6. `MONGO_PASSWORD` - database password
7. `KAFKA_HOST` - host with port for Kafka bootstrap server
