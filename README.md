# Gutendex Based Api

## - To run it locally (Based on MacOS and Linux machines):

    1 - Install Java 17, Maven 3 and docker (with compose) in your machine;

    2 - Clone this repo;

    3 - On the root folder of the project, type:
    "mvn clean package" and then "docker compose up --build" to build and run the app;

    4 - You should have the API running on "localhost:8080/api/books/"

    5 - You should also have access to the Swagger UI documentation of the API on "localhost:8080/api/swagger/"

    6 - The  the Swagger UI documentation of the API on "localhost:8080/api/api-docs/"

## - Endpoints (Also available on the swagger page):

    - (GET) http://localhost:8080/api/books/(?title="title") : Our primary endpoint. Returns the combined results of gutendex api (5 pages, for simplicity) and our book review repository;

    - (GET) http://localhost:8080/api/books/{bookId}/ : Returns a sigle book based on its Id, and our data on reviews;

    - (POST) http://localhost:8080/api/books/reviews/ : Post a rating and review for the matched bookId;

    - (GET) http://localhost:8080/api/books/top-reviews/ : Returns the top 3 books based on their average review;

    All of the above are cached endpoints.