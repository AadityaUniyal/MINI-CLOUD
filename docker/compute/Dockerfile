# Custom Compute Instance Dockerfile
# Based on Tomcat 10.1 (Lightweight)
FROM tomcat:10.1-jdk21

LABEL maintainer="MiniCloud"
LABEL version="1.0"
LABEL description="Custom Compute Instance for MiniCloud Infrastructure"

# Set a custom welcome message in the console
RUN echo "echo 'Welcome to Mini-Compute Instance! Access your apps on port 8080.'" >> ~/.bashrc

# Expose the standard Tomcat port
EXPOSE 8080

CMD ["catalina.sh", "run"]
