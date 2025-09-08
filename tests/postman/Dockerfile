FROM node:18-alpine

WORKDIR /app

# Копируем package.json и package-lock.json из папки tests
COPY package*.json ./
RUN npm install

# Копируем все файлы из tests (включая postman/, run-tests.sh и т.д.)
COPY . .

# Делаем скрипт запуска исполняемым
RUN chmod +x run-tests.sh

# Запуск тестов
ENTRYPOINT ["./run-tests.sh"]
