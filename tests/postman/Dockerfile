FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN chmod +x run-tests.sh

ENTRYPOINT ["./run-tests.sh"]
