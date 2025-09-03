FROM node:18-alpine

WORKDIR /app

# Копируем package.json и package-lock.json
COPY tests/postman/package*.json ./
RUN npm install

# Копируем весь код из tests/postman
COPY tests/postman .

# Делаем скрипт исполняемым
RUN chmod +x run-tests.sh

# Запускаем его
ENTRYPOINT ["./run-tests.sh"]
