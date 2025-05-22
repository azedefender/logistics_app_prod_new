# README для проекта Logistics

## Описание проекта

Этот проект представляет собой веб-приложение для управления заказами в системе логистики. Он позволяет пользователям создавать, редактировать, удалять заказы, а также генерировать отчеты в формате CSV и PDF.

## Скрипт создания базы данных

Для создания базы данных в MySQL используйте следующий скрипт:

```sql
-- Создание базы данных
CREATE DATABASE logistics_dbaseone;

-- Использование созданной базы данных
USE logistics_dbaseone;

-- Создание таблицы для заказов
CREATE TABLE `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `cargo_description` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Установка

### Предварительные требования

- Java 11 или выше
- Maven
- СУБД (например, MySQL или H2)

### Шаги по установке

1. **Клонируйте репозиторий**:

   ```bash
   git clone https://github.com/yourusername/logistics.git
   cd logistics
   ```

2. **Настройте базу данных**:

   Создайте базу данных в вашей СУБД, используя предоставленный скрипт, и настройте подключение в файле `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/logistics_dbaseone
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

   Если вы используете H2, добавьте следующую строку:

   ```properties
   spring.h2.console.enabled=true
   ```

3. **Соберите проект**:

   Используйте Maven для сборки проекта:

   ```bash
   mvn clean install
   ```

4. **Запустите приложение**:

   Вы можете запустить приложение с помощью следующей команды:

   ```bash
   mvn spring-boot:run
   ```

   После успешного запуска приложение будет доступно по адресу: `http://localhost:8080`.

## Использование

### Основные функции

- **Управление заказами**: Создавайте, редактируйте и удаляйте заказы.
- **Отслеживание грузов**: Отслеживайте статус ваших заказов.
- **Генерация отчетов**: Генерируйте отчеты о заказах в формате CSV и PDF.

### Эндпоинты

- `GET /orders` - Получить список всех заказов.
- `GET /orders/new` - Открыть форму для создания нового заказа.
- `POST /orders/create` - Создать новый заказ.
- `GET /orders/{id}/edit` - Открыть форму для редактирования заказа по ID.
- `POST /orders/{id}/update` - Обновить заказ по ID.
- `GET /orders/{id}/delete` - Удалить заказ по ID.
- `GET /orders/tracking` - Открыть страницу отслеживания грузов.
- `POST /orders/tracking` - Отслеживание заказа по ID.
- `GET /orders/reports` - Открыть страницу генерации отчетов.
- `POST /orders/reports` - Генерировать отчет по диапазону дат.
- `POST /orders/reports/csv` - Экспортировать отчет в формате CSV.
- `POST /orders/reports/pdf` - Экспортировать отчет в формате PDF.

## Зависимости

Убедитесь, что в вашем проекте присутствуют следующие зависимости в файле `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>2.0.24</version>
</dependency>
```

## Примечания

- Для генерации PDF отчета убедитесь, что шрифт `Arial Unicode MS` доступен в папке ресурсов вашего проекта (например, в `src/main/resources/static`).
- Проверьте, что у вас установлены все необходимые зависимости и настройки базы данных.

## Лицензия

Этот проект лицензирован под MIT License. Пожалуйста, ознакомьтесь с файлом LICENSE для получения дополнительной информации.

---
