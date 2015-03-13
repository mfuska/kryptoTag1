RSA
//folgenden Dingen wurden ihr implementiert
//1. Server Client Architektur (Server Thread Arch ... damit mehrer Clients paraell abarbeiten
//2. RSA Schlüssel Generierung
//3. Austausch privateKey (Server sendet private Key to Client)
//4. Hashen (SHA-2) eines Textes
//5. Client RSA verschlüsseln message(Test, h(text))
//6. Server RSA entschlüsseln und check Hashwert
javac RSA/*.java
java RSA/Server //startet den Server
java RSA/Client //startet den  Client

ECC
//Curve Werte P224
//hier Werte für die Kurve Werte für JUnit Tests http://point-at-infinity.org/ecc/nisttv
// alog funkt scalar aus Skriptum Kryptographie SS14
// implementiert:
// 1. ECC add
// 2. ECC verdopplung
// 3. ECC skalare Multi
// 4. ECC verify Methode die checked ob Punkt auf Kurve
// 5. JUnit Tests (EccMainTest.java)
javac ECC/ECC.java ECC/EccMain.java
java ECC/EccMain

DSA
// implementiert:
// 1. Berechnen alle benötigten Werte
// 2. SHA-2
// 3. Signaturgenerierung
// 4. Signaturverifizierung
javac DSA/DSA.java 
java DSA/DSA

Cert
// Cert Generierinun: openssl req -newkey rsa:2048 -nodes -keyout domain.key -x509 -days 365 -out domain.crt -sha256
// self signed Cert
// implementiert:
// 1. einlesen eines Certs
// 2. verifizieren des Certs mit Publickey
javac Cert/CertX509v3.java
java Cert/CertX509v3
