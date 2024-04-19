## Splitter Backend

Backend service for splitter written in Kotlin Spring Boot

## Prerequisites

- JDK 17 or higher
- Docker
- Postgres

## Running with Docker

1. Set up database

   ```BASH
   docker compose up -d db
   ```

2. Build

   ```BASH
   docker compose build
   ```

3. Run the app

   ```BASH
   docker compose up splitterbackend
   ```

## Running Locally

1. Run postgres db

2. Open IntelliJ IDEA

3. Install DB Navigator plugin

4. Connect to the database using DB Navigator

5. Edit SplitterApplicationKt configuration to add environment variables

   1. DB_URL
   2. DB_USER
   3. DB_PASSWORD

6. Run the app using the configuration
