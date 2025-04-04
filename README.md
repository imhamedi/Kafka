# Kafka POC with Spring Boot

Ce projet est un Proof of Concept (POC) démontrant une manière simple mais complète d’envoyer et consommer des messages Kafka avec Spring Boot, Spring Kafka, WebSocket, et une interface web. Il permet un démarrage rapide pour toute intégration Kafka.

---

## 📌 Table of Contents

1. [📝 Overview](#-overview)
2. [⚙️ Features](#️-features)
3. [✅ Prerequisites](#-prerequisites)
4. [🚀 Setup](#-setup)
5. [▶️ Running the Project](#️-running-the-project)
6. [📦 Creating Kafka Topics](#-creating-kafka-topics)
7. [🧪 Testing the Application](#-testing-the-application)
8. [📁 Project Structure](#-project-structure)
9. [🚧 Potential Improvements](#-potential-improvements)

---

## 📝 Overview

Ce POC démontre :

- Un **producteur dynamique** qui envoie vers plusieurs topics Kafka selon le choix de l'utilisateur.
- Un **consumer à commit manuel** qui écoute plusieurs topics simultanément.
- Une **intégration WebSocket** pour recevoir les messages consommés en temps réel dans le navigateur.
- Un **KafkaHistoryService** qui simule `--from-beginning` pour ramener tous les anciens messages d’un topic donné dès le chargement de la page.

---

## ⚙️ Features

- **🎯 Producteur Dynamique**  
  Envoie de messages vers `order_notifications` ou `order_updates` via une liste déroulante.

- **📥 Multi-topic Consumer**  
  Un seul consumer écoute plusieurs topics et push les messages reçus en WebSocket.

- **📡 WebSocket + UI**  
  Rafraîchissement automatique de l’IHM pour afficher les messages sans recharger la page.

- **🕓 Kafka History Service**  
  Lors du premier chargement, tous les anciens messages sont récupérés avec un consumer temporaire.

---

## ✅ Prerequisites

- Java 11+
- Maven
- Kafka (en local avec Zookeeper ou KRaft)
- Git

---

## 🚀 Setup

1. **Cloner le projet**

   ```bash
   git clone https://github.com/imhamedi/Kafka.git
   cd Kafka
   ```

2. **Configurer Kafka**

   Assurez-vous que Kafka est bien lancé sur `localhost:9092`.  
   Sinon, adaptez la propriété dans `src/main/resources/application.properties` :

   ```properties
   spring.kafka.bootstrap-servers=localhost:9092
   ```

3. **Installer les dépendances**

   ```bash
   mvn clean install
   ```

---

## ▶️ Running the Project

Lancer l’application Spring Boot :

```bash
mvn spring-boot:run
```

Accéder à l’interface via : [http://localhost:8080/notifications](http://localhost:8080/notifications)

---

## 📦 Creating Kafka Topics

Créer les deux topics requis pour les tests :

### Sous Linux/macOS :

```bash
bin/kafka-topics.sh --create --topic order_notifications --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

bin/kafka-topics.sh --create --topic order_updates --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

### Sous Windows :

```bash
.\bin\windows\kafka-topics.bat --create --topic order_notifications --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

.\bin\windows\kafka-topics.bat --create --topic order_updates --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

---

## 🧪 Testing the Application

1. **Aller sur l’interface web**  
   Accédez à [http://localhost:8080/notifications](http://localhost:8080/notifications)

2. **Envoyer un message**

   - Choisissez un topic dans la liste déroulante (`order_notifications` ou `order_updates`)
   - Tapez un message et cliquez sur **"Envoyer"**

3. **Voir les messages**

   - Tous les anciens messages seront affichés dès le chargement
   - Les nouveaux messages seront affichés **en temps réel** grâce à WebSocket

4. **(Optionnel) Utiliser kafka-console-consumer**

   ```bash
   bin/kafka-console-consumer.sh --topic order_notifications --from-beginning --bootstrap-server localhost:9092
   ```

---

## 📁 Project Structure

```
Kafka/
├── src/main/java/com/kafka/template
│   ├── config
│   │   ├── KafkaConsumerConfig.java           # Configuration du listener Kafka (commit manuel)
│   │   └── WebSocketConfig.java               # Configuration WebSocket (STOMP)
│   ├── controller
│   │   └── NotificationController.java        # Controller principal (affichage + envoi)
│   ├── services
│   │   ├── KafkaHistoryService.java           # Service qui simule --from-beginning avec un consumer temporaire
│   │   ├── OrderNotificationProducer.java     # Producteur Kafka (message vers le topic choisi)
│   │   └── OrderNotificationConsumerManual.java # Consumer Kafka à commit manuel
│
├── src/main/resources
│   ├── application.properties                 # Configuration de l'app
│   └── templates
│       └── notifications.html                 # Interface utilisateur (Thymeleaf)
│
└── README.md
```

---
---

> ✨ Ce POC peut servir de **starter Kafka/Spring Boot** pour tous vos projets temps réel. 
