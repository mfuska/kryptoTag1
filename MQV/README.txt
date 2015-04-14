//MQV
// implementiert:
// 1. MQV Server und MQV CLient
// 2. SHA256  
// 3. Signatur mit DSA
mkdir MQV
cd MQV
unzip MQV_20150414.zip
cd ..
javac  MQV/*.java
//Start SERVER Terminal 1
java MQV/Server
//Start CLIENT Terminal 2
java MQV/Client
