# Use nginx to serve static files
FROM nginx:alpine

# Copy the frontend files to nginx's default serving directory
COPY Frontend/. /usr/share/nginx/html/

# Copy custom nginx configuration
COPY Frontend/nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"] 