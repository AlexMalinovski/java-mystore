# MyStore
Приложение интернет-магазин

## Технологии и инструменты:
Java 21, Spring Boot v3.3.10 (Web, Validation), JPA, H2, Maven, Lombok, MapStruct, JUnit, Mockito, Tomcat.

## Инструкция по развёртыванию и системные требования
Min CPU/Core: 1/1, RAM:1Gb.

1. Открыть терминал и перейти в папку проекта 
2. Собрать проект ```.\mvnw.cmd clean package``` (windows) или ```./mvnw clean package``` (Unix);
3. Выполнить ```docker compose up```;
4. Открыть браузер и перейти: http://localhost:8090/main;

По-умолчанию БД приложения будет содержать демонстрационные данные.

Для добавления в справочник нового товара перейдите на http://localhost:8090/items/add
