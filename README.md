Пользователи:
USERNAME: nina@mail.ru PASSWORD: qwerty123
USERNAME: kate@mail.ru PASSWORD: qwerty222

Описании реализации:
Приложение разработано с использованием Spring Boot
Использован сборщик пакетов maven
Использована база данных mysql
Использована система управления миграциями liquibase
Для запуска используется docker, docker-compose
Код покрыт unit тестами с использованием mockito
Добавлены интеграционные тесты с использованием testcontainers
Информация о пользователях сервиса и их файлах хранится в базе данных


FRONT доступен на порту 8080, BACKEND - на порту 5500

Авторизация для получения токена:
POST localhost:5500/login
Content-Type: application/json

{
"login": "nina@mail.ru",
"password": "qwerty123"
}
