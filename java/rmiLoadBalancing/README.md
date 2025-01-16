# Distributed Computing "*RMI Task Loadbalancer*" 

## Aufgabenstellung
Die detaillierte [Aufgabenstellung](TASK.md) beschreibt die notwendigen Schritte zur Realisierung. Die ausgearbeiteten befinden sich im [Research](research.md)



## Implementierung

 Maria Ottendorfer, 4DHIT

1.) Zuerst das Repository von Git klonen.

2.) Dann im Terminal mit Gradle den Server starten 

```bash
gradle engine
```

Der Task wird ausgeführt

<img src="img\bild1.png" />



3.) Dann eine neues Terminal öffnen und den Client starten 

```bash
gradlew compute --args="localhost 1234"
```

Beim ersten Ausführen bekommt man eine **Java Access Controll Exception**. Um dieses Problem zu lösen muss in der *Java Policy* der Zugriff erlaubt werden.

Dazu im Home-Verzeichnis (bei mir: ``C:\Users\maria``) ein neues File **.java.policy** erstellen und alle Permissions erlauben:

```bash
grant {
	permission java.security.AllPermission;
};
```

Jetzt ein ``gradlew clean`` ausführen und den Server + Client neu starten 

Jetzt erhält man folgende Ausgabe: 

<img src="img\bild2.png" />)



**möglicher Fehler** 

Zuerst habe ich einen *java.security.policy - Error* bekommen. Das liegt an einem Syntax-Fehler in *.java.policy*. Nachdem ich diesen Fehler behoben habe, hatte ich keine weiteren Probleme mehr 😀

<img src="img\bild3.png" />



### Gitignore

1.) IntelliJ-Plugin **.ignore** installieren

2.) ein neues *.gitignore* File erstellen 

3.) Die Punkte *Template*, *Java* und *Gradle* auswählen - Jetzt kann das File commited werden 



### ComputeEngine schließen

#### ComputeEngine.java

1.) Zuerst ein neues Computer-Engine Objekt **engine** außerhalb des Try-Catch Block erzeugen. 

2.) Am ende des Try-Blocks nach dem System.out einen neuen BufferReder erzeugen. Dieser läuft solange bis der User "exit" in der Konsole eingibt und somit das Programm beendet. 

```java
BufferedReader reader = new BufferedReader((new InputStreamReader(System.in)));
while(!reader.readLine().equals("exit"));
```

3.) Jetzt muss noch das *UnicastRemoteObject* geschlossen werden. Dazu im Finally-Block (damit das Objekt *immer* geschlossen wird) folgenden Code hinuzfügen.

```java
try {
	UnicastRemoteObject.unexportObject(engine, true);
} catch (NoSuchObjectException e) {
	e.printStackTrace();
}
```

 

#### build.gradle

Im *build.gradle* muss noch **System.In**  für die Standardeingabe hinzugefügt werden. Das ist wichtig damit man keine NullPointerExeption bekommt. 

```
task engine(type: JavaExec) {
    classpath = sourceSets.main.runtimeClasspath
    main = 'engine.ComputeEngine'
    standardInput = System.in
}
```

Jetzt kann die laufenden Engine mit der Usereingabe "exit" gestoppt werden
<img src="img\bild4.png" />



### Fibonacci 

Für die Fibonacci-Folge habe ich zuerst eine neue Klasse **Fibonacci.java** im Package *client* hinzugefügt. Den Code habe ich aus folgendem Git-Repo verwendet: https://github.com/mborko/code-examples.git 

In der Klasse **ComputePi** habe ich statt dem Pi ein neues Fibonacci-Objekt erstellt und die Methode ``executeTask()`` aus der Klasse **ComputeEngine** aufgerufen 

```java
Fibonacci task = new Fibonacci(Integer.parseInt(args[1]));
BigInteger fib = comp.executeTask(task);
```

Die Methode liefert die entsprechende Fibonacci-Folge zurück und gibt sie in der Konsole aus. 

<img src="img\bild5.png" />



### Loadbalancer

Für den Loadbalancer habe ich ein neues Interface **Loadbalancing.java** und eine neue Klasse **LoadbalancerEngine.java** erstellt.

#### Interface Loadbalancing

1.) Das Interface erbt von *Remote*. Das ist wichtig, damit aus dem Loadbalancer ein neues Remote-Objekt erzeugt werden kann. 

2.) Dann werden zwei Methoden ``register(Compute stub)``  und ``unregister(Compute Stub)`` definiert. Mit diesen Methoden können sich später Server am Loadbalancer anmelden bzw. auch wieder abmelden. 

```java
public interface Loadbalancing extends Remote {
    void register(Compute stub) throws RemoteException;
    void unregister(Compute stub) throws RemoteException;
}
```



#### Klasse LoadbalancingEngine

Der Sinn von Loadbalancing ist es, den Client nicht zu verändern, sondern die Last serverseitig zu verteilen. Wenn mehrere Client-Tasks ausgeführt werden sollen werden diese mittels Round Robin gleichmäßig auf die Server verteilt. Der Client soll vom Loadbalancing nicht mitbekommen.  

1.) Die Klasse erbt von den beiden Interfaces *Loadbalancing* und *Compute*. Demnach müssen die Methoden ``register()``, ``unregister()`` und ``executeTask()`` implementiert werden. 

2.) Die Methode ``register(Compute stub)`` registriert den übergebenen Server-Stub in dem eine neue, eindeutige ID generiert wird. Mit dieser ID wird der Server dann in die Liste gespeichert. 

3.) Die Methode ``unregister(Compute stub)`` prüft zuerst, ob der Server-Stub registriert wurde und entfernt diesen dann aus der Liste, außerdem wird die ID gelöscht.

4.) Die Methode ``executeTask()`` führt den Task des Clients über einen Server aus. Dabei werden die auszuführenden Tasks gleichmäßig auf die registrierten Server verteilt. 

5.) Es gibt ein Set-Attribut **counter**. In diesem werden die Server-IDs seperat von den Servern gespeichert. Das ist wichtig, da die ID *Comparable* sein muss. Comparable kann nicht auf ein Proxy-Objekt (z.B Server) angewendet werden. 

 

> Mögliche Erweiterungen: 
>
> Bei register/unregister dem Server eine Rückmeldung geben, evtl. bei Anfragen eines unregistrierten Servers diesen gleich registrieren. 



#### Änderungen in Compute Engine

Es wird eine neue LoadbalancingEngine erzeugt und der Registry übergeben

```java
lbEngine = new LoadbalancerEngine();
Loadbalancing loadbalancer = (Loadbalancing) UnicastRemoteObject.exportObject(lbEngine, 0);
registry.rebind(name1, loadbalancer);
```

Die Methode ``executeTask()`` leitet die Anfrage an den Loadbalancer weiter

```java
public <T> T executeTask(Task<T> t) throws RemoteException {
    // Liste an Server mittels RoundRobin aufrufen und Rückgabe an Client weiterleiten!
    return ((Compute) lbEngine).executeTask(t);
}
```

 

#### Änderungen in Compute Server

Der Loadbalancer wird aus der Registry ausgelesen . In diesem wird dann der Server registriert.

```java
Registry registry = LocateRegistry.getRegistry("localhost");
loadbalancer = (Loadbalancing) registry.lookup("Loadbalancer");
loadbalancer.register(stub);
```

Im Finally-Block erfolgt die Abmeldung vom Loadbalancer

```java
loadbalancer.unregister(stub);
```

Die Methode ``executeTask()`` führt den übergeben Task aus

```java
public <T> T executeTask(Task<T> t) {
    log.info(this.toString() + " says: I'm ready to rumble ... " + t.toString());
    return t.execute();
}
```



#### Änderungen in ComputeTask

ComputeTask ist im Bezug auf den Loadbalancer nicht betroffen. Allerdings ist es jetzt möglich, den Task (Pi/Fibonacci) im Konsolenaufruf zu definieren. 

Zuerst die Engine starten

```bash
gradle engine
```

Dann den Server starten

```bash
gradle server
```

Jetzt kann der entsprechende Task aufgerufen werden

```bash
# Task Pi starten
gradle compute --args="localhost Pi 1234"

# Task Fibonacci starten
gradle compute --args="localhost Fibonacci 1234"
```



### Client-Loadbalancer-Server-Verbindungen über mehrere Rechner

zusammen mit Mira Haselberger und Michaela Heinzel 

Da mein Programm als Server verwendet wurde, habe ich zuerst einige Vorbereitungen treffen müssen

1.) Mira, Michi und ich haben uns alle mit dem selben Hotspot verbunden. Außerdem habe ich in Windows unter ``Netzwerkstatus/Adaperoptionen`` alle Adapter außer dem WLAN deaktiviert. 

2.) Dann habe ich die Engine und den Server gestartet

3.) Mira und Michi haben danach einen Compute-Befehl ausgeführt. Diesmal allerdings nicht mit dem Parameter *localhost* sondern mit der IP-Adresse *192.168.43.110* welche meinem Gerät entspricht. 

```bash
gradle compute --args="192.168.43.110 Pi 1234"
gradle compute --args="192.168.43.110 Fibonacci 1234"
```

4.) beide haben für beide Tasks erfolgreich eine Rückmeldung bekommen

<img src="img\ek2.jpeg" />

<img src="img\ek3.jpeg" />

Außerdem habe auch ich in meinem Server eine Ausgabe bekommen, welche die Verbindung bestätigt

<img src="img\ek1.png" />



## Quellen

[1] Syt Theorie Unterricht

[2] Weitere Quellen: siehe [Research](research.md)