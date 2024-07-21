# skillchallenge3-metaphorce
Skill Challenge 3 de Metaphorce para el curso Back end Microservicios Java Middle 2024

La aplicación consiste en un sitio web en el cual existe una sala para jugar un videojuego llamado Roblox. Esta sala tiene un cupo limitado de 8 jugadores.
En la base de datos existen 24 jugadores. 
La cola de espera no se implementó para esta versión nueva del proyecto anterior del Skill Challenge 2, por lo que la única funcionalidad de esta aplicación web es ver los jugadores en la sala y registrar uno nuevo.

Se usa TomCat como servidor web y se dockeriza el proyecto con una imagen para la BD (mysql) y otra para la App.
Existen 2 servlets, uno para mostrar la lista de los 8 lugares que contiene la sala del videojuego Roblox y otro para añadir un nuevo jugador a la sala.

IMPORTANTE:
Ejecutar el comando docker-compose up --build para generar las imágenes de docker y crear los contenedores.
La app utiliza el puerto 8080 y la base de datos el puerto 3308.
