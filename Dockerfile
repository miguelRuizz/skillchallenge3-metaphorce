# Utiliza la imagen de Tomcat como base
FROM tomcat:9.0-jdk11-openjdk

# Copia el archivo servlet-api.jar al directorio lib de Tomcat
COPY ./lib/servlet-api.jar /usr/local/tomcat/lib/
COPY ./lib/mysql-connector-j-9.0.0.jar /usr/local/tomcat/lib/

# Copia los archivos fuente de tu proyecto al contenedor
COPY ./src /usr/local/tomcat/src
COPY ./web /usr/local/tomcat/web

# Compila el servlet y prepara el archivo WAR
RUN mkdir -p /usr/local/tomcat/web/WEB-INF/classes/com/example && \
    javac -cp /usr/local/tomcat/lib/servlet-api.jar -d /usr/local/tomcat/web/WEB-INF/classes /usr/local/tomcat/src/com/example/PlayerServlet.java && \
    javac -cp /usr/local/tomcat/lib/servlet-api.jar -d /usr/local/tomcat/web/WEB-INF/classes /usr/local/tomcat/src/com/example/JoinToLobbyServlet.java && \
    jar cvf /usr/local/tomcat/webapps/PlayerLobbys.war -C /usr/local/tomcat/web/ .

# Exponer el puerto 8080
EXPOSE 8080

# Comando para iniciar Tomcat cuando se inicie el contenedor
CMD ["catalina.sh", "run"]