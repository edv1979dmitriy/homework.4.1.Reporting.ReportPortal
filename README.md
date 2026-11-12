# Интеграция с ReportPortal (в проекте с JUnit5)

### Настроить build.gradle

1. Добавить зависимости для агента ReportPortal и логгера:

   *testImplementation 'com.epam.reportportal:agent-java-junit5:5.5.8'*\
   *testImplementation 'com.epam.reportportal:logger-java-logback:5.4.0'*\
   *testImplementation 'ch.qos.logback:logback-classic:1.5.18'*

   Необходимо отключить (при наличии) зависимость *'org.slf4j:slf4j-simple:2.0.3'* (возможны конфликты с логгером)

2. Включить автоопределение расширений в задаче test:

   *systemProperty 'junit.jupiter.extensions.autodetection.enabled', 'true'*

### Подключить ReportPortalExtension

1. Создать директорию *src/test/resources/META-INF.services* с файлом *org.junit.jupiter.api.extension.Extension*
2. Добавить в него строку *com.epam.reportportal.junit5.ReportPortalExtension*

### Настроить отправку логов

1. В директории *src/test/resources* создать файл *logback-test.xml*
2. Добавить в него следующий код

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    <appender name="ReportPortalAppender" class="com.epam.reportportal.logback.appender.ReportPortalAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%t] %-5level - %msg%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="ReportPortalAppender"/>
    </root>
</configuration>
```

### Создать файл со своими реквизитами для подключения к серверу ReportPortal

1. В директории *src/test/resources* создать файл *reportportal.properties*
2. Добавить в него следующий код

   *rp.endpoint=http://localhost:8080*  // адрес сервера\
   *rp.api.key= ХХХХ_ХХХХ_ХХХХХХ*       // сгенерированный код из вашего профиля (см. далее)\
   *rp.launch=My_Tests*                 // название запуска (любое, не должно содержать точек и пробелов)\
   *rp.project=default_personal*        // название проекта (дефолтное)\
   *rg.enable=true*                     // отправка отчётов на сервер разрешена

### Развернуть собственный локальный сервер ReportPortal

1. Проверить соответствие характеристик локальной машины предъявляемым требованиям:
    - CPU: минимум 2 ядра.
    - RAM: минимум 6 ГБ оперативной памяти.
    - Диск: от 20 ГБ свободного места.
2. Скачать конфигурационный файл с *https://raw.githubusercontent.com/reportportal/reportportal/master/docker-compose.yml*
3. Создать файл *docker-compose.yml* в корневой папке проекта и добавить в него содержимое скачанного файла.
4. Настроить пароль администратора (при необходимости):
    - найти в файле пароль (возможно, строка 503) и заменить его:
    - *services:*\
      *uat:*\
      *environment:*\
      - *RP_INITIAL_ADMIN_PASSWORD=Пароль_123*
5. Командой *docker compose -p reportportal up -d* запустить создание и запуск всех необходимых контейнеров

### Получить код

1. Перейти по адресу http://localhost:8080
2. Войти под дефолтными логин: *default* , пароль: *1q2w3e*
3. Открыть профиль (левый нижний угол страницы)
4. Перейти на вкладку *API Keys*
5. Сгенерируйте ключ и вставте в *reportportal.properties*

### Результат

После запуска тестов командой *./gradlew clean test* на сервере автоматически появляются результаты тестов

## Скриншоты моих тестов

<details>
<summary>Скриншоты</summary>

<img width="1303" height="367" alt="Screenshot_27" src="https://github.com/user-attachments/assets/50fc1159-c812-46bd-8c8f-8340a76899fc" />

<img width="1326" height="701" alt="Screenshot_25" src="https://github.com/user-attachments/assets/bebdcacf-037f-489e-bce9-70874a39274e" />

<img width="1339" height="840" alt="Screenshot_26" src="https://github.com/user-attachments/assets/593c4222-79da-4da6-9ea6-abc744f57d30" />


</details>
