# Hokotrok-Purgatorium

![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-GUI%20skeleton-blue?style=flat)

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

Az alkalmazás alapértelmezetten a Swing grafikus főmenüvel indul. A játék indítása után a konzolvezérlés automatikusan aktiválódik ugyanarra a futó játékállapotra.

Amint kiválasztottad a pályát, felvetted a játékosokat és elindítottad a játékot, a konzol és a grafikus felület párhuzamosan ugyanazt a játékot vezérli.

---

## Futtatható JAR készítése

Fordítás után készíthető egy futtatható JAR fájl:

```bash
jar cfe proto.jar Main -C out .
java -jar proto.jar
```

## A grafikus működés röviden

1. A program főmenüvel indul.
2. A `Grafikus mód` a pályaválasztó képernyőre vezet.
3. A pályaválasztás után a játékos-regisztrációs panelen vehetők fel Hókotrós és Buszsofőr szerepkörű játékosok.
4. A játék indításakor létrejön egy `GameSession`, amely a meglévő `Parser`, `ObjectRegistry` és `Game` modellobjektumokra épül.
5. A játéknézet egy univerzális `GameSnapshot` alapján frissíti majd a státuszt, a térképet, a vezérlőket és az üzenetpanelt.

Fontos: a grafikus réteg jelenleg váz és bekötési alap. A tényleges rajzolási és interakciós logika későbbi implementációra van előkészítve.

---

## Pályafájlok

A pályafájlok közvetlenül a `maps/` mappában találhatók:

- grafikus pályák: `base-map-*`, `tutorial-map-*`, `city-map-*`, `blizzard-map-*`
- konzolos alap pálya: `console-map-*`

---

## Konzolos mód

A konzolvezérlés automatikusan csatlakozik a futó játékhoz, amikor a játék ténylegesen elindul.

Amíg nincs kiválasztott pálya és elindított játék, a konzol nem ad játékmenet-visszajelzést.

Elérhető vezérlőparancsok:

- `h` vagy `help` - súgó megjelenítése.
- `c` vagy `command` - az aktuális szerepkör parancsainak megjelenítése.

Játék közben konzolon kiadott parancsok azonnal látszanak GUI-n, és GUI műveletek is ugyanazt a konzolos játékállapotot módosítják.

Játék végén a konzol kiírja, hogy vége a játéknak, és listázza az eredményeket is.
