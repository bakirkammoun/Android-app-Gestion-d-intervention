🚀 Projet : Application de Gestion d’Intervention
📱 Partie 1 : Application Android
L’application mobile permet aux techniciens ou agents d’intervention de :

Se connecter à leur compte

Consulter les interventions assignées

Voir les détails des interventions (lieu, date, client…)

Mettre à jour le statut d’une intervention (en cours, terminée…)

Ajouter des commentaires ou pièces jointes (photos)

Gérer leur planning d’intervention

👨‍🔧 Technologie utilisée :

Langage : Java 

IDE : Android Studio

Architecture : MVVM 

🖥️ Partie 2 : Backend Spring Boot
Le backend est un serveur REST développé avec Spring Boot, qui fournit les API nécessaires à l’application mobile.

🗄️ Il permet de :

Gérer les utilisateurs (authentification, rôles)

Ajouter/modifier/supprimer des interventions

Affecter des interventions aux techniciens

Consulter l’historique des interventions

Stocker les images ou documents liés aux interventions

Gérer les notifications (optionnel)

🔧 Technologies backend :

Langage : Java

Framework : Spring Boot

Sécurité : Spring Security + JWT

Base de données : MySQL / PostgreSQL

Communication : API REST (JSON)

Documentation API : Swagger (optionnel)

Stockage des fichiers : Système de fichiers ou stockage cloud (ex: AWS S3)

🔗 Communication entre l’App et le Backend
Le backend expose des endpoints RESTful (ex: /api/interventions, /api/login)

L'application Android utilise Retrofit (ou Volley) pour interagir avec ces endpoints

Les données sont échangées au format JSON

L’authentification peut être gérée via JWT tokens
