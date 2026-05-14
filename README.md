![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-GUI%20skeleton-blue?style=flat)

# Hokotrok-Purgatorium

A projekt a Hókotrók Purgatórium Java prototípusa. A jelenlegi ágban a konzolos modellre egy Swing alapú grafikus alkalmazásváz épül rá: a modell és a CLI-kompatibilis betöltés megmarad, a grafikus felület pedig külön `gui` csomagokban indul el.

---

## Fordítás

JDK 21 vagy újabb ajánlott. A verziók ellenőrzése:

```bash
javac -version
java -version
```

A projekt fordítása:

```bash
mkdir -p out
find src -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d out
```

---

## Indítás

Fejlesztés közben közvetlenül az `out` mappából indítható:

```bash
java -cp out Main
```

Indítás után a menüben válaszd a grafikus módot:

```text
2
```

Ez elindítja a Swing alkalmazásvázat, ahol a főmenüből a grafikus pályaválasztó, majd a játékos-regisztrációs nézet érhető el.

---

## Futtatható JAR készítése

Fordítás után készíthető egy futtatható JAR fájl:

```bash
jar cfe proto.jar Main -C out .
java -jar proto.jar
```

A JAR indítása után ugyanúgy a `2` opcióval választható a grafikus mód.

---

## A grafikus működés röviden

1. A program főmenüvel indul.
2. A `Grafikus mód` a pályaválasztó képernyőre vezet.
3. A pályaválasztás után a játékos-regisztrációs panelen vehetők fel Hókotrós és Buszsofőr szerepkörű játékosok.
4. A játék indításakor létrejön egy `GameSession`, amely a meglévő `Parser`, `ObjectRegistry` és `Game` modellobjektumokra épül.
5. A játéknézet egy univerzális `GameSnapshot` alapján frissíti majd a státuszt, a térképet, a vezérlőket és az üzenetpanelt.

Fontos: a grafikus réteg jelenleg váz és bekötési alap. A tényleges rajzolási és interakciós logika későbbi implementációra van előkészítve.

---

## Konzolos mód

A régi tesztelési és manuális útvonal továbbra is elérhető marad:

- `0` - Test Mode
- `1` - Game Mode
- `2` - Graphic Mode

Ez azért maradt meg, hogy a meglévő tesztek és a modell debugolása a grafikus fejlesztés közben se sérüljön.
