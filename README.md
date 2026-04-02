![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-Skeleton-blue?style=flat)

# Hokotrok-Purgatorium

Rövid leírás: ez jelenleg egy szkeleton/keret projekt a hókotró szimulátorhoz.
A fő osztályközi kapcsolatok (öröklés, interface-ek, teszt-vázak) be vannak kötve, az üzleti logika nagy része még nincs implementálva.

---

## Branch struktúra

- A projekt jelenleg 3 külön branch-en fut: `skeleton`, `proto`, `graphics`.
- A `skeleton` branch egy ideiglenes váz és a fő osztály létrehozására szolgál.
- A `proto` branch a prototípusos implementáció helye, ahol a fő logika és a tesztek kerülnek megvalósításra.
- A `graphics` branch a grafikus megjelenítés fejlesztésére szolgál, ahol a vizuális elemek és a játék megjelenítése lesz a fókuszban.

A fejlesztés során a `proto` branch a `skeleton` branch-hez képest tartalmazza a fejlettebb implementációkat. Miután a `proto` branch-ben a fő logika és a tesztek elkészülnek, a `graphics` branch-re kerül át a fejlesztés, ahol a vizuális megjelenítés lesz a fő cél. Ez a struktúra lehetővé teszi, hogy a fejlesztés különböző aspektusai párhuzamosan haladjanak, miközben megőrizzük a kód tisztaságát és szervezettségét. A végső cél az, hogy a `graphics` branch-ben egy teljesen működőképes és vizuálisan is vonzó hókotró szimulátor legyen elérhető. Amely majd a `main` branch-re kerül, miután minden fejlesztés befejeződött és tesztelve lett.

---

## Fordítás és futtatás (javac)

### Előfeltétel ellenőrzése

```bash
javac -version
java -version
```

Ha `javac: command not found` hibát kapsz, akkor nincs telepítve a JDK, vagy nincs benne a PATH-ban.
Ajánlott: JDK 21, majd terminál újranyitása.

### Fordítás és futtatás

```bash
mkdir -p out
find src -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d out
java -cp out Main
```