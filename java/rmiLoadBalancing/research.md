## Fragestellung

**Was ist RMI und welches Prinzip der verteilten Programmierung kommt dabei zur Anwendung?**

RMI steht für *Remote Method Invocation* (dt. Aufruf entfernet Methoden) und macht auch genau das. Mit RMI ist es möglich Methoden eines anderen Objektes aufzurufen, auch wenn es sich in einem anderen Adressraum befindet. Dabei ist es egal ob das andere Objekt am selben oder an einem entfernten Rechner liegt.

*Funktionsweise:* ein Public Remote Server Objekt ermöglicht die Kommunikation zwischen Client und Server  

Die Kommunikation findet zwischen dem Stub-Objekt des Clients und dem Stub-Objekt des Servers statt. 

Diese Funktion ermöglicht Distributed Computing (Client-Server Modell) über ein standarisiertes Protokoll.



**Was sind Stubs? Welche Aufgabe hat dabei das Proxy-Objekt?**

Der Stub stellt ein (Proxy-)Objekt des entfernten Dienst dar (Client-Stub beim Server/ Server-Stub beim Client). 

*Stub am Client*

Der Stub im Client dient als eine Art Gateway. Es werden Informationen gesammelt und an den Server übergeben

Es müssen auf jeden Fall folgende Daten enthalten sein:

* Identifizierung des Remote Object

* Methodenaufruf + notwendige Paramter für das Remote Object

  

*Stub am Server (Skeleton/Tie)* 

Der Stub im Server nimmt die Nachricht des Client-Stubs entgegen und leitet die Anfrage an das Remote-Objekt weiter. Das entspricht also dem Methodenaufruf + Parameter, welche vom Client übergeben werden. 

Der Rückgabewert der Methode wird über die Stubs an den Client gesendet.

![](img\bild6.png)

 

**Was wird in der Registry gespeichert?**

In der Registry werden neu erzeugte Server-Objekte mit einem Bind-Namen (eindeutig) registriert. 

Server-Objekte werden mit folgender Methode registriert

```java
//Die Registry wird erzeugt und an den Port 1099 gebunden
Registry registry = LocateRegistry.createRegistry(1099);
//ein Server "Server1" und der Stub werden registriert
registry.rebind("Server1", stub);
```

Der Client kann nur auf Server zugreifen, die in der Registry gespeichert sind. Er holt sich die Server-Referenz über den Bindname aus der Registry.

```java
//Registry wird übergeben. args entspricht Konsoleneingabe
Registry registry = LocateRegistry.getRegistry(args[0]);
//Server wird über den Namen "Server1" ausgelesen
Compute comp = (Compute) registry.lookup("Server1");
```

<img src="img\bild7.png" style="zoom: 67%;" />





**Wie kommt das `Remote`-Interface zum Einsatz? Was ist bei der Definition von Methoden zu beachten? Was ist bei der Weitergabe von Objekten unabdingbar?**

Das Remote-Interface gibt die Methoden vor, die der Server implementieren muss (z.B ``executeTask()``) Das Remote-Objekt implementiert das Remote-Interface, außerdem muss es mit dem *UnicastRemoteObject* verknüpft werden. Deswegen muss es im Remote-Objekt auch einen Konstruktor ohne Parameter geben, welcher ``super()`` aufruft. Dadurch wird der Konstruktor von *UnicastRemoteObject* aufgerufen. 

Im Remote-Interface muss weiters auch jede Methode eine *RemoteException* werden. Diese kommt ebenfalls vom *UnicastRemoteObject*. 



**Welche Methoden des `UnicastRemoteObject` kommen bei der Server-Implementierung zum Einsatz?**

*Konstruktor* 

```java
super();
```

Erzeugt ein neues UnicastRemoteObject-Objekt mit einem anonymen Port



*public static Remote exportObject(Remote obj, int port)*

```java
Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
```

Das Remote Objekt wird über ein ServerSocket exportiert. Dadurch wird es für andere Remote Objekte über den festgelegten Port verfügbar. Es wird eine *RemoteException* geworfen, falls der Export fehlschlägt

Der Rückgabewert ist das exportierte Objekt. 



*public static boolean unexportObject(Remote obj, boolean force)* 

```java
UnicastRemoteObject.unexportObject(engine, false);
```

Das zuvor exportierte Objekt wird wieder aus der RMI-Laufzeit entfernt. Es ist jetzt nicht mehr verfügbar. Der Parameter gibt an, ob ein Export erzwungen werden soll (true) oder ob zuerst alle anstehende Anfragen noch bearbeitet werden (false). Es wird eine *NoSuchObjectException* geworfen, wenn das übergebene Remote Objekt zuvor nicht exportiert wurde.

Der Rückgabewert entspricht true, wenn der Unexport erfolgreich war und false,  wenn nicht.  



**Wie kann der Server ein sauberes Schließen ermöglichen? Was muss mit dem exportierten Objekt geschehen?**

Idealerweise erfolgt das Beenden durch einen Userinput, z.B "exit" als Konsoleneingabe. Dann wird in einem Finally-Block zuerst der Server durch ein *Unregister* aus dem Loadbalancer entfernt (dieser Schritt ist in unserem Beispiel notwendig, da wir Loadbalancing verwenden). 

Als nächstes muss noch  das Remote-Objekt durch ein *Unexport* abgemeldet werden. 

```java
try {
  // do something first
  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	while (!reader.readLine().equals("exit")) ;
	UnicastRemoteObject.unexportObject(engine, false);
} catch (Exception e) {
  // catch something
} finally {
	try {
    // Server wird aus dem Loadbalancer entfernt
  	loadbalancer.unregister(stub);
  } catch (RemoteException e) {
  	// catch something else
  }
  try {
    // Remote Objekt wird aus LZ entfernt
  	UnicastRemoteObject.unexportObject(this, false);
  } catch (NoSuchObjectException e) {
  	e.printStackTrace();
  }
}
```



## weitere Loadbalancing-Prinzipien

Loadbalancing Methoden kann man in drei Kategorien unterteilen:

* **Hardware basiert**

  mehrere physische Server auf denen die Last verteilt wird

  \+ volle Kontrollle

  \- teuer

  \- wird nicht mehr so oft verwendet

* **Cloud basiert**

  Das Loadbalancing inkl. alle Funktionen werden vollkommen in die Cloud ausgelagert

  \+  Pay for Use

  \+ keine Hardware notwendig

* **Software basiert**

  Die Software wird selbst installiert, verwaltet und konfiguriert. 

  \+ Open Source oder kostenpflichtig

  \- hoher Aufwand/Komplexität 

  

Außerdem gibt es verschiedene Algorithmen, welche die Lastverteilung unterschiedlich regeln. 



### Weighted Round Robin

Das Prinzip von Weighted Round Robin ist eine Erweiterung von Round Robin. Der große Unterschied besteht darin, dass die Anfragen nicht gleichmäßig auf die Server verteilt werden, sondern gewichtet sind. Leistungsstarke Server bekommen also mehr Anfragen zugewiesen als schwächere Server. 

Die Gewichtung kann individuelle durch numerische Werte definiert werden.    

Besonders geeignet für: heterogene Server-Cluster (verschiedene Ressourcen)

<img src="img\wrr.png" style="zoom: 50%;" />

### Least Connections

(Weighted) Round Robin berücksichtigt die Anzahl der Verbindungen nicht. Das kann dazu führen, dass durch zu viele gleichzeitige Anfragen der Server überlastet werden kann. 

Das Prinzip der *Least Connection* prüft die Anzahl der Verbindungen der einzelnen Server und weist die Anfrage an den Server mit den wenigsten Anfragen zu. 

Besonders geeignet für: homogene Server-Cluster (ähnliche Ressourcen).

<img src="img\lc.png" style="zoom: 50%;" />

### Weighted Least Connections

Dieses Prinzip ist eine Kombination von *Weighted Round Robin* und *Least Connections*. Eine eingehende Anfrage wird demnach an den höchstgewichteten Server mit den geringsten bestehenden Verbindungen weitergeleitet. 

### weitere Algorithmen

**Least Response Time**

Die Gewichtung erfolgt anhand der Antwortzeit. Die Anfrage wird an den Server mit der kürzesten Antwortzeit geschickt. 

**Source IP Hash**

Dieses Prinzip ermöglicht es Verbindungen (zu einem bestimmten Server) nach einer Unterbrechung wiederherzustellen. Es wird ein Hash-Schlüsseln aus der IP-Adresse von Server und Client generiert.  Dadurch wird ein Client einem Server eindeutig zugeordnet  



## Quellen

[1] https://www.tutorialspoint.com/java_rmi/java_rmi_quick_guide.htm - RMI Einführung

[2] https://www.oracle.com/java/technologies/javase/remote-method-invocation-distributed-computing.html - Oracle RMI Documentation

[3] https://www.geeksforgeeks.org/remote-method-invocation-in-java/ - RMI Tutorial + Erklärung

[4] https://www.ionos.com/digitalguide/server/know-how/what-is-distributed-computing/ - Distributed Computing

[5] https://docs.oracle.com/javase/8/docs/api/java/rmi/server/UnicastRemoteObject.html - Java API UnicastRemoteObject

[6] http://www.sbgl.de/rmi/ - Java RMI Ziele und Funktionsweisen

[7] https://geekflare.com/open-source-load-balancer/ - Load Balancing Kategorien

[8] https://www.ionos.com/digitalguide/server/know-how/load-balancers-distributing-server-workloads/ - Load Balancing Prinzipien

[9] https://kemptechnologies.com/load-balancer/load-balancing-algorithms-techniques/ - Load Balancing Algorithmen

sowie die im Moodle-Kurs angeführten Quellen
