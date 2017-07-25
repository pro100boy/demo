[![Build Status](https://travis-ci.org/pro100boy/demo.svg?branch=master)](https://travis-ci.org/pro100boy/demo)
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
  * с браузере открыть URL `http://localhost:8888` (в этом случае произойдет редирект на `http://localhost:8888/hello/?nameFilter=` с значением nameFilter по-умолчанию `^[В-Яв-яC-Zc-z].*$`) либо `http://localhost:8888/hello/?nameFilter=regexp` (вместо `regexp` ввести нужное регулярное выражение)

После первого запуска в базе будет автоматически создана и заполнена тестовыми данными таблица `contacts`. Количество тестовых строк 500. 

Приложение использует порт `8888` (можно изменить в файле `application.yml`).
