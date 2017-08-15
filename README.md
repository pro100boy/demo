[![Build Status](https://travis-ci.org/pro100boy/demo.svg?branch=WebSocket)](https://travis-ci.org/pro100boy/demo)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/482f33da3efb42aab948aee96a7e7aa2)](https://www.codacy.com/app/gpg/demo?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pro100boy/demo&amp;utm_campaign=Badge_Grade)
## REST Service (application deployed in application context `hello`) ##
Для работы приложения необходимо:
  * Java 8
  * PostgreSQL 
  * Maven

Перед запуском приложения убедиться в следующем:
  * в БД установлены следующие параметры авторизации (можно изменить в файле `application.yml`):
     - login: `postgres`
     - password: `vortex`

  * в БД существует база `contacts` (создать при необходимости) 

Для запуска приложения:
  * собрать проект в Maven
  * в терминале выполнить команду: `java -Dfile.encoding="UTF-8" -jar demo-0.0.1-SNAPSHOT.jar`
  * в браузере открыть URL `http://localhost:8888` (в этом случае произойдет редирект на `http://localhost:8888/hello/?nameFilter=` с значением nameFilter по-умолчанию `^[В-Яв-яC-Zc-z].*$`) либо `http://localhost:8888/hello/?nameFilter=regexp` (вместо `regexp` ввести нужное регулярное выражение)

После первого запуска в базе будет автоматически создана и заполнена тестовыми данными таблица `contacts`. Количество тестовых строк 500, cyrillic charset. 

Приложение использует порт `8888` (можно изменить в файле `application.yml`).
