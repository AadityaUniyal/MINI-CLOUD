# Custom Database Instance Dockerfile
# Based on MySQL 8.0
FROM mysql:8.0

LABEL maintainer="MiniCloud"
LABEL version="1.0"
LABEL description="Custom Database Instance for MiniCloud Infrastructure"

# Set custom MySQL configurations
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=minicloud_db

# Expose the standard MySQL port
EXPOSE 3306

CMD ["mysqld"]
