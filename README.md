![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-Skeleton-blue?style=flat)

# Hokotrok-Purgatorium

Rövid leírás: Ez a `proto` branch amely a "Hókotrók" játék prototípusát tartalmazza. <br>
A projekt működése itt már teljesen megvan valósítva, de jelenleg ez az állapot még csak egy konzolos alkalmazás. Nincs grafikus megjelenítés, de a logika és a tesztek már müködnek.

---

## Fordítás és futtatás (javac)

### 0. Előfeltétel ellenőrzése

```bash
javac -version
java -version
```

Ha `javac: command not found` hibát kapsz, akkor nincs telepítve a JDK, vagy nincs benne a PATH-ban.
Ajánlott: JDK 21, majd terminál újranyitása.

### 1. Fordítás

```bash
mkdir -p out
find src -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d out
java -cp out Main
```

### 2. Futtatható JAR fájl készítése

A lefordított fájlokból egyetlen futtatható `proto.jar` állományt készítünk. Megadjuk neki, hogy a program belépési pontja (ahol a `public static void main` van) a `Main` osztály:

```bash
jar cfe proto.jar Main -C out .
```

*(**c** = create, **f** = file name, **e** = entry point. A `-C out` . azt jelenti, hogy lépjen be az out mappába, és csomagoljon be mindent `.` amit ott talál.)*

### 3. Futtatás

Ha a JAR fájl elkészült, a program indítása (Játék mód vagy manuális tesztelés):

```bash
java -jar proto.jar
```

---

### Automata tesztelés futtatása fájlból (Standard I/O átirányítás):

Ha van egy előre megírt teszt bemeneted (pl. `test-xyz.txt`), így lehet automatikusan lefuttatni és a kimenetet fájlba menteni:

```bash
java -jar proto.jar < test-xyz.txt > test-xyz-result.txt
```