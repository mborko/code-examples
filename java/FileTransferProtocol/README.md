# Lightweight File-Transfer-Protocol

## AUFGABENSTELLUNG

Ihr Ziel ist es einen einfachen Socket-basierten Textfile Transfer-Client in Java zu implementieren. Dieser Client soll sich zu einem Fileserver verbinden und dem Benutzer erlauben nach ASCII-Files zu suchen und diese herunterzuladen (d.h. Sie müssen sich nicht um das Übertragen von binary files wie Bildern und Programmen kümmern).

Die Funktionalität des Transfer-Clients soll in der Klasse "Client" implementiert werden. Diese Klasse soll als stand-alone Applikation realisiert werden, und braucht dazu eine statische main-Methode.

Ihr Programm soll genau zwei Kommandozeilen-Argumente akzeptieren. Wenn mehr oder weniger Argumente übergeben werden oder ein anderer Fehler auftritt (z.B. Host nicht gefunden), soll Ihr Programm die entsprechende Fehlermeldung ausgeben und mit dem Exit-Code 1 (d.h. System.exit(1)) sofort beenden. In allen anderen Fällen soll ihr Programm den Exit-Code 0 zurückgeben. Die Synopsis des Progammes ist wie folgt:

java Client <server> <port>

wobei

1. <server> der Name des einfachen File-Transfer-Servers ist, welcher den gewünschten Service anbietet (z.B. localhost). Diesen sollten Sie z.B. mit "java Server 12345 12345 hallo" vorher starten!

2. <port> die Port-Nummer des Servers ist (z.B. 12345 wie oben beim Server angeführt)

Wenn nun Ihr Programm mit zwei Argumenten aufgerufen wird, sollte es sich zunächst mit dem File-Transfer-Server verbinden, um anschließend vom Standard-Input (System.in) die Benutzerbefehle einzulesen.

Ihr interaktiver Client muss folgende Befehle verstehen und verarbeiten können (beachten Sie, dass die Befehle Case-Sensitive sind, d.h. LS ist somit am Client ein unbekanntes Kommando! Beachten Sie, dass der Server durch das Protokoll nur ein großgeschriebenes Kommando akzeptiert!):

1. ls <path>
Wenn "path" ein Filename ist (z.B. funny-stuff/chicken.txt), dann soll Ihr Programm die Länge der Datei liefern. Sollte "path" ein Verzeichnis sein, dann gibt Ihr Programm den Inhalt dieses Verzeichnisses wieder.

2. get <path>
Downloaded die Datei, welche durch "path" identifiziert wurde lokal in die Datei "download.info". Sollte "path" keine Datei sein, muss dies dem Benutzer mitgeteilt werden und kein Download stattfinden.

3. quit
Beendet die Verbindung zum Server und schließt das Programm mit dem Exit-Code 0.


Der File-Transfer-Server verwendet ein einfaches Protokoll, um mit den Benutzern zu kommunizieren. Sie finden es im File protocol.txt bzw. in der ausgedruckten Beilage.
Den Server können Sie nach Kompilierung mit "javac Server.java" folgendermaßen starten:

java Server 12345 1234 pass

Nachdem sich Ihr Client mit dem Server verbunden hat, sollte folgendes auf der Standardausgabe angezeigt werden:

Connection established.
>: 

Die Eingabeaufforderung soll dem Benutzer mit ">: " angezeigt werden. Sollte der Benutzer eine Falscheingabe tätigen, muss ihm der Fehlercode als auch die Fehlermeldung angezeigt werden.

Sie können sich die beiden Files "input.txt" und "output.txt" als Referenzen ansehen. Jegliche andere Ausgaben sind zu unterlassen. Sie können mit folgendem Befehl Ihren Client testen:

java Client localhost 12345 < input.txt

Die Ausgabe sollte genau den Vorgaben in "output.txt" entsprechen!


## DOKUMENTATION

Es ist Pflicht den geschriebenen Sourcecode sinnvoll zu kommentieren. Beachten Sie dabei, dass offensichtliche Codezeilen NICHT kommentiert gehören. Es ist jedoch notwendig Algorithmen und deren Zweck, sowie logische Funktionsblöcke ausführlich zu kommentieren. Nehmen Sie als Richtwert an, den Code soweit zu kommentieren, um mit den Kommentaren auch wirklich die Funktionsweise des Programmes erklären zu können.

Des weiteren ist es notwendig JavaDoc zu verwenden. Dabei sind kurze und treffende Kommentare über jeder Klasse und Methode zu schreiben. Die Dokumentation muss in einem "doc" Ordner erstellt werden, wo sich die generierten html Files befinden sollen.

Änderungen bzw. Abweichungen am Code müssen im Protokoll gesondert vermerkt und begründet werden. Die Begründung muss sich auf technische Argumente bezüglich Softwaredesign und Effizienz stützen.


## FUNKTIONALITÄT

Der von Ihnen abgegebene Code MUSS auf jeden Fall kompilier- und ausführbar sein! Eine mangelnde Funktionsweise führt zu einer schlechteren Bewertung der abgegebenen Arbeit.


## PROTOKOLL

Das Protokoll ist in einem lesbaren PDF, DOC oder DOCX Format abzugeben. Es muss mit Namen, Klasse und Datum sowie der Aufgabenstellung in einem Satz versehen werden. Das Protokoll muss das fertig gestellte Design (UML-Klassendiagramm), die implementierten Teile des Quellcodes mit Erklärungen, die auch als Kommentare direkt im Quellcode enthalten sein können, und den Ablauf der durchgeführten Testfälle enthalten. 


## ABGABE

Das Protokoll muss ausgedruckt werden.
Zusätzlich muss die Abgabe in einem .zip Archiv erfolgen! Dieses Archiv muss neben dem Protokoll Sourcecode, JavaDoc sowei die Dateien für die UML-Diagramme in den vorgesehenen Ordner enthalten.
