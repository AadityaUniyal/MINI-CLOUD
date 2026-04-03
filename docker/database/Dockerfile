# Custom PostgreSQL Database Instance Dockerfile
FROM postgres:15-alpine

LABEL maintainer="MiniCloud"
LABEL version="1.0"
LABEL description="Custom PostgreSQL Instance for MiniCloud Infrastructure"

# Set custom PostgreSQL configurations
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_USER=postgres
ENV POSTGRES_DB=minicloud

# Expose the standard PostgreSQL port
EXPOSE 5432

# Copy initialization scripts
COPY init/*.sql /docker-entrypoint-initdb.d/

CMD ["postgres"]
