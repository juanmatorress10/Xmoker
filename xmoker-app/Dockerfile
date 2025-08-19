# Stage 1: Build the Angular application
FROM node:18-alpine AS builder
WORKDIR /app

# Copia los archivos de dependencias y las instala
# Usar 'npm ci' es una buena práctica si tienes un package-lock.json
COPY package*.json ./
RUN npm ci --legacy-peer-deps

# Copia el resto del código fuente de la aplicación
COPY . .

# Crea la versión de producción optimizada de la aplicación
RUN npm run build -- --configuration production

# Stage 2: Serve the application with Nginx
FROM nginx:stable-alpine

# Copia la configuración personalizada de Nginx
# Esto sobrescribe el 'default.conf', por lo que no es necesario borrarlo antes
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia los archivos de la aplicación construidos desde la etapa 'builder'
COPY --from=builder /app/dist/xmoker-app/ /usr/share/nginx/html/

# Expone el puerto 80, que es el puerto estándar de Nginx
EXPOSE 80

# Inicia el servidor Nginx cuando el contenedor arranque
CMD ["nginx", "-g", "daemon off;"]