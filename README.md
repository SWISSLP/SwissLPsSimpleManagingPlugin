# SwissLPsSimpleManagingPlugin
A 1.21. Minecraft Server bukkit plugin which has many functions. Such as timeout function, crate log etc. Find out more about it in the README File :)
### **README before use**

---

## **SwissLP's Minecraft Plugin**

### **Description**
SwissLP's Minecraft Plugin is a feature-rich addition to your server. It includes a variety of functionalities such as timeout management, chest and inventory backups, detailed logs for block interactions, and inventory inspections. Designed for admins, it ensures complete control and detailed monitoring of server activities.

---

### **Features**
1. **Timeout System**:
   - Temporarily restrict players from joining the server for a specified period.
   
2. **Block Logging**:
   - Tracks details about broken blocks, including the type, position, and the player who broke them.
   
3. **Chest Interaction Logging**:
   - Logs when a player interacts with a chest, detailing the items taken/added, the player involved, and the time of the interaction.
   
4. **Inventory Inspection**:
   - Allows admins to view players' inventories through a GUI interface.
   
5. **Backup System**:
   - **Automatic Backups**:
     - Backups of all chests and player inventories are created automatically at midnight.
   - **Manual Backups**:
     - Admins can manually trigger backups for chests or inventories.

6. **Restore System**:
   - Restore specific chest contents or player inventories from backups using timestamps.

---

### **Commands**

| **Command**                         | **Description**                                                    | **Example**                                   |
|-------------------------------------|--------------------------------------------------------------------|-----------------------------------------------|
| `/timeout <Name> <Hours>`           | Temporarily prevents a player from joining the server.             | `/timeout SwissLP 2`                         |
| `/listblock`                        | Displays logs of broken blocks.                                    | `/listblock`                                 |
| `/listkiste`                        | Displays interaction logs for the chest the player is looking at.  | `/listkiste`                                 |
| `/listinventar`                     | Opens a GUI to inspect players' inventories.                      | `/listinventar`                              |
| `/backupchest`                      | Creates a manual backup of all chest logs.                        | `/backupchest`                               |
| `/backupinventar`                   | Creates a manual backup of all player inventories.                | `/backupinventar`                            |
| `/restorechest <Timestamp>`         | Restores the contents of a chest from a backup.                   | `/restorechest 2024_12_01_00_00_00`          |
| `/restoreinventory <Name> <Timestamp>` | Restores a player's inventory from a backup.                      | `/restoreinventory SwissLP 2024_12_01_00_00_00` |

---

### **How to Use**
1. **Installation**:
   - Download the plugin `.jar` file and place it in your server's `plugins` folder.
   - Restart the server to load the plugin.

2. **Backup and Restore**:
   - Use the backup commands (`/backupchest` and `/backupinventar`) to create manual backups.
   - To restore, ensure you use the exact timestamp format provided during the backup creation.

---

### **License and Copyright**
This plugin is the intellectual property of **SwissLP**. Redistribution, modification, or usage of the plugin without explicit permission is prohibited. All rights reserved.

---

---

### **README auf Deutsch**

---

## **SwissLPs Minecraft-Plugin**

### **Beschreibung**
SwissLPs Minecraft-Plugin ist eine funktionsreiche Erweiterung für deinen Server. Es bietet zahlreiche Funktionen wie Timeout-Management, Backup-Systeme für Kisten und Inventare, detaillierte Protokolle über Block-Interaktionen und eine Inventarinspektion. Das Plugin wurde speziell für Admins entwickelt, um vollständige Kontrolle und Überwachung der Serveraktivitäten zu ermöglichen.

---

### **Funktionen**
1. **Timeout-System**:
   - Verhindert temporär, dass Spieler dem Server beitreten können.

2. **Block-Protokollierung**:
   - Zeichnet Details über abgebrochene Blöcke auf, einschließlich Typ, Position und Spielername.

3. **Kisten-Interaktionsprotokollierung**:
   - Protokolliert, wenn ein Spieler mit einer Kiste interagiert, und speichert, welche Gegenstände entnommen/hinzugefügt wurden.

4. **Inventarinspektion**:
   - Erlaubt Admins, Inventare der Spieler über eine GUI zu überprüfen.

5. **Backup-System**:
   - **Automatische Backups**:
     - Backups von Kisten-Logs und Spieler-Inventaren werden täglich um Mitternacht erstellt.
   - **Manuelle Backups**:
     - Admins können Backups manuell auslösen.

6. **Wiederherstellungssystem**:
   - Stellt Kisteninhalte oder Spielerinventare aus Backups mit Zeitstempeln wieder her.

---

### **Befehle**

| **Befehl**                          | **Beschreibung**                                                   | **Beispiel**                                  |
|-------------------------------------|--------------------------------------------------------------------|-----------------------------------------------|
| `/timeout <Name> <Stunden>`         | Verhindert temporär, dass ein Spieler dem Server beitritt.         | `/timeout SwissLP 2`                         |
| `/listblock`                        | Zeigt Protokolle über abgebrochene Blöcke an.                      | `/listblock`                                 |
| `/listkiste`                        | Zeigt Interaktionsprotokolle für die Kiste, auf die geschaut wird. | `/listkiste`                                 |
| `/listinventar`                     | Öffnet eine GUI zur Überprüfung von Spieler-Inventaren.            | `/listinventar`                              |
| `/backupchest`                      | Erstellt ein manuelles Backup aller Kisten-Logs.                   | `/backupchest`                               |
| `/backupinventar`                   | Erstellt ein manuelles Backup aller Spieler-Inventare.             | `/backupinventar`                            |
| `/restorechest <Zeit>`              | Stellt Kisteninhalte aus einem Backup wieder her.                  | `/restorechest 2024_12_01_00_00_00`          |
| `/restoreinventory <Name> <Zeit>`  | Stellt ein Spielerinventar aus einem Backup wieder her.            | `/restoreinventory SwissLP 2024_12_01_00_00_00` |

---

### **Anleitung**
1. **Installation**:
   - Lade die `.jar`-Datei des Plugins herunter und lege sie in den `plugins`-Ordner deines Servers.
   - Starte den Server neu, um das Plugin zu laden.

2. **Backups und Wiederherstellungen**:
   - Nutze die Backup-Befehle (`/backupchest` und `/backupinventar`), um manuelle Backups zu erstellen.
   - Um Daten wiederherzustellen, verwende den genauen Zeitstempel, der beim Backup generiert wurde.

---

### **Lizenz und Copyright**
Dieses Plugin ist geistiges Eigentum von **SwissLP**. Die Weitergabe, Modifikation oder Nutzung des Plugins ohne ausdrückliche Erlaubnis ist untersagt. Alle Rechte vorbehalten.

---
