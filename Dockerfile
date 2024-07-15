# On récupère une image pour compiler le projet Spring Boot
FROM eclipse-temurin:22-jdk-alpine as build

# On indique à Docker où l'on veut travailler dans les dossiers
WORKDIR /workspace/app

# On copie tous les fichiers de notre dossier dans le dossier actuel du conteneur (/workspace/app)
COPY . .

# Installer les dépendances nécessaires, y compris ffmpeg
RUN apk add --no-cache ffmpeg

# Lancement d'une commande dans le conteneur avec RUN. Ici, nous souhaitons lancer la compilation 
# de notre projet avec Maven en omettant les tests unitaires
RUN ./mvnw clean package -DskipTests

# Image finale. Celle qui sera réellement utilisée pour créer notre conteneur avec notre projet à l'intérieur
FROM eclipse-temurin:22-jdk-alpine

# Installer ffmpeg dans l'image finale
RUN apk add --no-cache ffmpeg

# Ceci est pour indiquer que l'on peut mapper un dossier du conteneur sur notre Hôte
VOLUME /tmp

# ARG est utilisé pour la déclaration d'une variable au moment de la construction de l'image
ARG VERSION=0.0.1-SNAPSHOT
ARG DB_USERNAME
ARG DB_PASSWORD

# Définir les variables d'environnement pour Spring Boot
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

# On copie notre fichier compilé (JAR) de notre conteneur avec l'alias 'build', dans notre image actuelle
# en renommant le fichier en app.jar
COPY --from=build /workspace/app/target/back-$VERSION.jar app.jar

# Commande qui indique que l'on souhaite exécuter notre app.jar lorsque notre conteneur sera créé
ENTRYPOINT ["java","-jar","/app.jar"]
