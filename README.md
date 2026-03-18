/**
* Application Gym Manager - Système de gestion de salle de sport.
*
* <p>Cette application permet de gérer :</p>
* <ul>
*   <li>Les membres et leurs abonnements</li>
*   <li>Les activités sportives et les coachs</li>
*   <li>Les inscriptions aux activités</li>
*   <li>Les salles de sport</li>
* </ul>
*
* <p><b>Architecture :</b> Pattern MVC avec séparation en couches</p>
* <ul>
*   <li>model : Entités métier</li>
*   <li>dao : Accès aux données (JDBC)</li>
*   <li>controller : Logique métier</li>
*   <li>view : Interface utilisateur (JavaFX)</li>
* </ul>
*

#  Gym Manager

Système de gestion de salle de sport développé en Java avec JavaFX.

## 📋 Fonctionnalités

- ✅ Gestion des membres et abonnements
- ✅ Gestion des activités sportives
- ✅ Gestion des coachs et salles
- ✅ Inscriptions aux activités
- ✅ Interface graphique JavaFX
- ✅ Base de données MySQL

## 🛠️ Technologies utilisées

| Technologie | Version | Usage |
|-------------|---------|-------|
| Java | 25      | Langage principal |
| JavaFX | 25.0.2  | Interface graphique |
| Maven | 3.x     | Gestion des dépendances |
| MySQL | 8.x     | Base de données |
| JDBC | -       | Accès aux données |
| JUnit 5 | 5.10.2  | Tests unitaires |

## 📦 Prérequis

- **JDK 24** ou supérieur
- **Maven 3.6+**
- **MySQL 8.0+** (WAMP)

## Installation

### 1. Cloner le projet
```bash
git clone https://github.com/[ton-username]/gym-manager.git
cd gym-manager
```

### 2. Créer la base de données

1. Démarre WAMP
2. Ouvre phpMyAdmin (`http://localhost/phpmyadmin`)
3. Importe le fichier `database/gym_manager.sql`

Ou exécute directement :
```sql
CREATE DATABASE gym_manager;
USE gym_manager;
-- Puis exécute le contenu de gym_manager.sql
```

### 3. Configurer la connexion

Modifie `src/main/java/org/example/gymmanager/util/DatabaseConnection.java` :
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/gym_manager";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = ""; // Ton mot de passe MySQL
```

### 4. Compiler le projet
```bash
mvn clean install
```

## ▶️ Lancement

### Méthode 1 : Via Maven
```bash
mvn javafx:run
```

### Méthode 2 : Via le JAR
```bash
# Créer le JAR
mvn clean package

# Lancer l'application
java -jar target/gym-manager.jar
``

### Méthode 3 : Via le script

**Windows :**
```bash
deploy.bat
```
## Tests

Lancer les tests unitaires :
```bash
mvn test
```

Générer la Javadoc :
```bash
mvn javadoc:javadoc
```

## Architecture
```
gym-manager/
├── src/main/java/
│   ├── MainApp.java              # Point d'entrée JavaFX
│   ├── model/                    # Entités métier
│   │   ├── Membre.java
│   │   ├── Activite.java
│   │   ├── Coach.java
│   │   ├── Salle.java
│   │   ├── Abonnement.java
│   │   └── Inscription.java
│   ├── dao/                      # Accès aux données (JDBC)
│   │   ├── MembreDao.java
│   │   ├── MembreDaoImpl.java
│   │   └── ...
│   ├── controller/               # Logique métier
│   │   ├── MembreController.java
│   │   ├── ActiviteController.java
│   │   └── InscriptionController.java
│   ├── view/                     # Controllers JavaFX
│   │   └── MainViewController.java
│   ├── util/                     # Utilitaires
│   │   └── DatabaseConnection.java
│   └── exception/                # Exceptions personnalisées
│       └── BusinessException.java
├── src/main/resources/
│   ├── fxml/                     # Fichiers FXML
│   │   └── main-view.fxml
│   └── css/                      # Styles CSS
│       └── style.css
├── src/test/java/                # Tests unitaires
└── pom.xml                       # Configuration Maven
```

### Gestion des membres
![Membres](vois javadocs)

### Gestion des activités
![Activités](vois javadocs)

### Gestion des inscriptions
![Inscriptions] (vois javadocs)

## modèle de données

### Diagramme MCD
```
[COACH] 1 -------- n [ACTIVITE]
[SALLE] 1 -------- n [ACTIVITE]
[MEMBRE] 1 -------- n [INSCRIPTION]
[ACTIVITE] 1 -------- n [INSCRIPTION]
[MEMBRE] 0..1 -------- n [ABONNEMENT]
```

### Tables principales

- **membre** : Adhérents de la salle
- **coach** : Coachs sportifs
- **salle** : Salles de sport
- **activite** : Activités sportives
- **abonnement** : Formules d'abonnement
- **inscription** : Inscriptions aux activités

## Auteur
**merveilline fouelefack**

## Remerciements

- Professeur : Mathieu Calvo
- Établissement : eseo
- Année : 2025-2026

---

**Date de remise :** 25 mars 2025
```

---

# PARTIE 4 : EXPORTER LE SCRIPT SQL

Crée un dossier `database/

### database/gym_manager.sql`

Exporte ta base de données depuis phpMyAdmin :

1. **Sélectionne** la base `gym_manager`
2. **Onglet "Exporter"**
3. **Format** : SQL
4. **Exécuter**
5. **Sauvegarde** le fichier dans `database/gym_manager.sql`


#Fichiers créés dans cette étape
├── deploy.bat                    # Script Windows
├── README.md                     # Documentation complète
├── database/
│   └── gym_manager.sql          # Base de données

  