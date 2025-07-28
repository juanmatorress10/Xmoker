# 1) Etapa de build con Node
FROM node:18-alpine AS builder
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps

COPY . .
RUN npm run build

# 2) Etapa de runtime con Nginx
FROM nginx:stable-alpine
RUN rm /etc/nginx/conf.d/default.conf

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html

# ✅ Exponemos también el 9090 para el proxy simulado
EXPOSE 80
EXPOSE 9090

CMD ["nginx", "-g", "daemon off;"]
