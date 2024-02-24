# Task management system

**Task management system** - сервис для управления задачами. Сервис предоставляет RESTful API для создания, обновления, удаления и просмотра задач.

## Локальный Запуск Проекта с Использованием Docker Compose

**1. Установите Docker и Docker Compose:**
- [Руководство по Установке Docker](https://docs.docker.com/get-docker/)
- [Руководство по Установке Docker Compose](https://docs.docker.com/compose/install/)


**2. Клонируйте Репозиторий:**
   ```bash
   git clone https://github.com/Ladzislau/task-management-system
   cd task-management-system
   ```

**3. Создайте Docker Image приложения:**

   ```bash
./mvnw spring-boot:build-image
   ```
Эта команда создаст образ приложения на вашем локальном пк.

**4. Запустите Docker Compose:**
    
   ```bash
docker-compose up --build
   ```
   Эта команда создаст и запустит два контейнера: само приложение и mysql в качестве БД.


**5. Документация Swagger UI:**

 Откройте веб-браузер и перейдите по адресу http://localhost:8080/swagger-ui/index.html, чтобы получить доступ к Swagger UI, где вы можете изучить API и использовать интерактивную документацию.
