FROM node:16

WORKDIR /frontend

COPY package.json ./

RUN npm install

COPY public/ ./public
COPY src/ ./src

EXPOSE 3000

ENTRYPOINT npm start
