# Estágio 1: Build
FROM clojure:temurin-21-alpine AS build

# Instala Node.js e NPM (Clojure e Java já vêm na imagem base)
RUN apk add --no-cache nodejs npm

WORKDIR /app

# Copia arquivos de dependência
COPY package.json package-lock.json deps.edn shadow-cljs.edn ./

# Instala dependências do NPM
RUN npm ci

# Copia o código fonte
COPY src ./src
COPY resources ./resources
COPY public ./public

# Argumentos de build (mantendo compatibilidade com o original)
ARG API_URL
ARG AUTH_API_URL
ENV API_URL=$API_URL \
    AUTH_API_URL=$AUTH_API_URL

# Executa o build (Tailwind + Shadow-cljs)
RUN npm run build

# Estágio 2: Nginx
FROM nginx:1.27-alpine

# Remove arquivos padrão do Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copia a configuração personalizada do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia os artefatos estáticos gerados
COPY --from=build /app/resources/static /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
