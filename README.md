![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-Skeleton-blue?style=flat)

# Hokotrok-Purgatorium

Rövid leírás: ez jelenleg egy szkeleton/keret projekt a hókotró szimulátorhoz.
A fő osztályközi kapcsolatok (öröklés, interface-ek, teszt-vázak) be vannak kötve, az üzleti logika nagy része még nincs implementálva.

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

## Fontos megjegyzés

- A projekt jelenleg fejlesztés alatt van, ezért sok metódus csak váz.
- A tesztek osztályai létre vannak hozva, de a konkrét viselkedés implementálása a következő lépés.