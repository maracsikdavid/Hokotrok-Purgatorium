![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-Skeleton-blue?style=flat)

# Hokotrok-Purgatorium

Rövid leírás: Prototípus a hókotró szimulátorhoz.
A projekt működése itt már teljesen megvan valósítva, de jelenleg ez az állapot még csak egy konzolos alkalmazás. Nincs grafikus megjelenítés, de a logika és a tesztek már müködnek.

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