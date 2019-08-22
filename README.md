#Podstawy Baz Danych 2018/2019

##Wykonali:
* Adam Stec 
* Bartłomiej Bujniak
* Przemysław Brzeziński
* Piotr Nowakowski

###Polecane narzędzie
Do pracy przy aplikacji oraz bazie danych polecam [IntellijIdea Ultimate](https://www.jetbrains.com/idea/)


Ściągasz ją ze [tej strony](https://www.jetbrains.com/idea/download/#section=windows) - #####WEŹ WERSJĘ ULTIMATE


Następnie albo klikasz `Evaluate for free` i cieszysz się miesięcznym trialem albo rjeestrujesz się na [tej stronie](https://www.jetbrains.com/shop/eform/students) używając maila uczelnianego z końcówką pw.edu.pl i cieszysz się roczną licencją 


Tutaj tutorial [jak połączyć się z bazą danych](https://www.jetbrains.com/help/idea/connecting-to-a-database.html)

##Co muszę mieć zainstalowane ?
###Git
Jeżeli używasz Windowsa -> zainstaluj #####[GitBash](https://gitforwindows.org/)

Po instalacji git'a wykonaj

`$ git config --global user.name "John Doe"`


 `$ git config --global user.email johndoe@example.com`
###Java
Musisz mieć wersję co najmniej #####[1.8 !!](https://www.ibm.com/support/knowledgecenter/en/SS88XH_1.6.0/iva/install_mils_windows_java.html)


Jeśli jej nie masz,  możesz ją pobrać -> z [tej strony](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 




#####PAMIĘTAJ BY DODAĆ JĄ DO ZMIENNEJ [PATH](https://www.java.com/pl/download/help/path.xml) !!!!!! 
###Docker
By móc odpalić bazę danych potrzebujesz [Docker'a](https://pl.wikipedia.org/wiki/Docker_(oprogramowanie))


Jak zainstalować pod Windowsem -> [tutaj link](https://docs.docker.com/docker-for-windows/install/)


Jeżeli używasz Linux'a musisz znaleźć instrukcje dla swojej dystrybucji -> [tutaj link](https://docs.docker.com/install/)


##Jak ściągnąć ?

Najlepiej&nbsp; użyj Gita

```sh
$ git clone https://solveretur@bitbucket.org/solveretur/pbd.git
$ cd ./pbd
$ chmod +x bin/deploy-local-stack.sh
$ chmod +x bin/stop-local-stack.sh

```

Albo po prostu [ściągnij stąd](https://bitbucket.org/solveretur/pbd/downloads/) 


##### PAMIĘTAJ BY WYKONAĆ TE INSTRUKCJE  `chmod +x .....` ##### BO INACZEJ NIE PÓJDZIE 

##Jak postawić bazę danych ?
Wykonaj skrypt w folderze w którym masz projekt 
```sh
$ cd ./pbd
$ bin/deploy-local-stack.sh

```
Jeżeli wszystko dobrze poszło powinieneś móc pod adresem [http://localhost:8000](http://localhost:8000) znaleźć się na stronie logowanie gdzie możesz się zalogować do [Adminera](https://www.adminer.org/pl/)


Server ma być: `db` 

 User: `dbuser` 

 Password: `dbuser` 
 

 Database: `domtel`

##Jak uruchomić ?
Teraz musisz uruchomić aplikację. W tym celu musisz wejść w folder projektu i wykonać

```sh
$ cd ./pbd
$ ./mvnw clean install
$ java -jar ./target/pbd-0.0.1-SNAPSHOT.jar

```

Jeżeli wszystko dobrze pójdzie to powinieneś znaleźć coś pod adresem [http://localhost:8080](http://localhost:8080)

###FAQ
- Czy mogę zainstalować sobie MYSQL i podłączyć nie używając dockera ?
Tak, byle działał na `localhost` port `3306` baza `domtel` user `dbuser` password `dbuser`
- Jak stworzyć dobrą CSV'k do importu ?
Wartości null muszą być zapisywane jako `\N` 
- Jak rozwiązać Foreign Keys Constraint validaiton ?
`SET FOREIGN_KEY_CHECKS = 0;`
- Baza nie działa, jak wyświetlić logi
Usuń `-d` w skrypcie `./bin/deploy-local-stack.sh`