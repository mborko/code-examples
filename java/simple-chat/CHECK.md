# "Java GUI & Socket Programmierung - Simple Chat" - Checklist

## Ausführung
Folgender Befehl wird bei der Abnahme ausgeführt und bewertet:

    gradle clean test jacocoTestReport javadoc

Diese Befehle sind nützlich, um die Bewertung genauer unter die Lupe zu nehmen:

    gradle client --args="--name Franz --host localhost --port 5050"
    gradle client --args="--name Sissi --host localhost --port 5050"

    gradle client --args="--name Sissi --host 10.0.106.7 --port 5050"


## Bewertung 

### GK Anforderungen überwiegend erfüllt
- [ ] README Dokumentation enthält Informationen über angepassten Sourcecode
- [ ] JavaFX GUI ist funktionstüchtig
- [ ] kontinuierliche Entwicklungshistorie (``git log``)
- [ ] kein ``System.out.println()`` verwendet sondern Logger
- [ ] Nebenläufigkeit sauber implementiert
- [ ] Verbindung zum Server Port 5050 wird beim Starten hergestellt
- [ ] im Fehlerfall wird eine MessageBox mit dem Fehler angezeigt
- [ ] "Senden" Nachricht an den Server geschickt und das Eingabe-Textfeld geleert
- [ ] eingehende Nachrichten werden mittels Thread in Chatbereich eingefügt
- [ ] Das Close-Event wird abgefangen, dem Server wird das Kommando *!EXIT* geschickt
- [ ] Port 5050 in einem Thread auf eingehende Verbindungen horchen
- [ ] *accept()* in einem eigenen Thread aufrufen
- [ ] bei Fehlerfall wird entsprechende MessageBox angezeigt und das Programm beendet
- [ ] Neue Nachrichten an den Server werden an alle Clients verteilt und im eigenen Chat Feld angezeigt
- [ ] Bei Close, wird die Verbindung zu allen Clients sauber beendet (automatic close, !EXIT an alle Clients)


### GK Anforderungen zur Gänze erfüllt
- [ ] Javadoc-Dokumentation erweitert
- [ ] README Recherche und Zusammenfassung der verwendeten Technologien
- [ ] Aufbau des ganzen Systems mit entsprechender Dokumentation (z.B. Sequenzdiagramme)
- [ ] Sauberes Schließen von Resourcen (kein ``System.exit(1)``)
- [ ] Programmende keine offene Resourcen hinterlassen
- [ ] automatisch vergebene Client-Namen (z.B. Client, Client#1, Client#2).
- [ ] eigener Messagespeicher, der nicht persistiert werden muss


### EK Anforderungen überwiegend erfüllt
- [ ] User kann den Chatnamen bei Programmaufruf mitgeben
- [ ] Client kann dem Server seinen Chatnamen mittels des Kommandos *!CHATNAME Franz* übermitteln
- [ ] Clients werden der Client-Liste hinzugefügt, sobald sie sich verbinden
- [ ] ServerAdmin kann Client entfernen indem er ihn in der UserListBox anlickt und dann den Disconnect Button drückt.
- [ ] Der Client muss konfiguriert werden können (Host, Port, Name)
- [ ] Chat-Textbox automatisch ganz nach unten scrollen
- [ ] Die grafische Oberfläche muss bei einem Resize skalieren
- [ ] *Software-Testing mit Testreport*

### EK Anforderungen zur Gänze erfüllt
- [ ] Resize von GUI sauber implementiert (Vollbild <-> kleinstes mögliches Fenster)
- [ ] Ausführliches Software-Testing
- [ ] Coverage Report
