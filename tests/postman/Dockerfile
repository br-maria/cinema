FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN chmod +x tests/postman/run-tests.sh
ENTRYPOINT ["tests/postman/run-tests.sh"]

