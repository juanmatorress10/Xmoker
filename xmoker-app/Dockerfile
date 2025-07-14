# xmoker-app/Dockerfile

# 1) Etapa de build con Node
FROM node:18-alpine AS builder
WORKDIR /app

# Copiamos sólo package.json / lock para cachear npm ci
COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps

# Copiamos el resto y lanzamos build en modo production
COPY . .
RUN npm run build

# 2) Etapa de runtime con Nginx
FROM nginx:stable-alpine
# Quitamos la config por defecto de Nginx
RUN rm /etc/nginx/conf.d/default.conf

# Copiamos nuestro nginx.conf custom (prepara el proxy, CORS, etc)
COPY nginx.conf /etc/nginx/conf.d/

# Servimos los ficheros estáticos generados
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx","-g","daemon off;"]
