#!/bin/bash

# Définition de l'URL de l'API
API_URL="https://groovegather-api.olprog-a.fr/api/v1/projects"  # Modifier l'URL selon votre configuration

# Lecture du fichier JSON et découpage des objets
while IFS= read -r project; do
    # Utilisation de curl pour effectuer un POST avec les données JSON du projet
    echo "Envoi du projet : $project"
    response=$(curl -s -X POST -H "Content-Type: application/json" -d "$project" "$API_URL")
    echo "Réponse de l'API : $response"
    echo  # Ajoute une ligne vide pour la clarté entre chaque requête
done < <(jq -c '.[]' projects.json)
