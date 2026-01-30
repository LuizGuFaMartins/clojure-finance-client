# Estágio 1: Build
FROM clojure:temurin-21-alpine AS build

RUN apk add --no-cache nodejs npm

WORKDIR /app

COPY . .

RUN npm ci

ARG API_URL
ENV API_URL=$API_URL

ARG AUTH_API_URL
ENV AUTH_API_URL=$AUTH_API_URL

ARG NODE_ENV
ENV NODE_ENV=$NODE_ENV

RUN npm run clean
RUN npm run build

# Estágio 2: Nginx
FROM nginx:1.27-alpine

RUN rm -rf /usr/share/nginx/html/*

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/public /usr/share/nginx/html

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
