# Hokotrok-Purgatorium

![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-GUI%20integrated-blue?style=flat)

A projekt a Hókotrók Purgatórium Java prototípusa. A jelenlegi ágban a konzolos modellre egy Swing alapú grafikus alkalmazás épül rá: a modell és a CLI-kompatibilis betöltés megmarad, a grafikus felület pedig külön `gui` csomagokban indul el.

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
5. A játéknézet egy univerzális `GameSnapshot` alapján frissíti a státuszt, a térképet, a vezérlőket és az üzenetpanelt.
6. A térképen bal klikkel elem választható ki, jobb klikkel részletes információ kérhető le a csomópontokról, sávokról és járművekről.
7. A bolti vásárlás, eke felszerelés, utántöltés, buszmozgatás és hókotrómozgatás a közös modellállapotot módosítja.

---

## Pályafájlok

A pályafájlok közvetlenül a `maps/` mappában találhatók:

- grafikus pályák: `base-map-*`, `tutorial-map-*`, `city-map-*`, `blizzard-map-*`
- konzolos alap pálya: `console-map-*`

## Játékprofilok és fix konstansok

Ez a rész a jelenlegi modellben rögzített, kódból vagy pályafájlból olvasott értékeket foglalja össze. A pályák minden sávja az alapértelmezett sávhosszt használja, mert a `maps/` fájlok nem írnak felül külön `length` értéket.

### Időzítés és körváltás

| Szabály | Fix érték | Jelentés |
| --- | ---: | --- |
| Egy sáv alap hossza | `5` progress egység | Ennyi tick alatt ér végig egy mozgó jármű egy teljes sávon, ha nem akad el. |
| Teljes kör utáni szimuláció | `5` tick | Nem minden egyes játékos után fut le, hanem akkor, amikor a körsorrend végéről visszaér az első játékosra. |
| Kézi konzolos tick | tetszőleges pozitív szám | A `tick,N` parancs N alkalommal hívja meg a szimulációs órát. |
| Só aktiválódása | `2` tick | Sózás után ennyi tick kell a hó vagy jég tisztára olvadásához. |
| Só utáni hó-immunitás | `4` tick | Tiszta sáv sózás után ennyi tickig nem kezd újra havasodni. |
| Tiszta sávból vékony hó | `5` tick | Ha nincs sóvédettség és az út nem alagút, ennyi tick után `ThinSnowCondition` lesz. |
| Vékony hóból vastag hó | további `5` tick | Vékony hó állapotból ennyi tick után `ThickSnowCondition` lesz. |
| Vékony hó letaposási küszöb | `20` járműbelépés | Ennyi bénítható jármű áthaladása után a vékony hó jéggé válhat. |
| Jégen megcsúszás esélye | `20%` | Normál módban a bénítható járművek ennyi eséllyel csúsznak meg jeges sávon. |
| Ütközés/bénulás ideje | `2` tick | Megcsúszás vagy ütközés után ennyi tickig bénult a normál jármű. |
| Autó várakozása munkahelyen | `10` tick | Célba érés után ennyit vár, mielőtt hazafelé indul. |
| Autó várakozása otthon | `20` tick | Hazatérés után ennyit vár, mielőtt újra munkába indul. |

A körsorrendben a `Cleaner` játékosok jönnek előbb, utána a `BusDriver` játékosok, azonos szerepen belül regisztrációs azonosító szerint rendezve. Ha egy busz vastag hó miatt elakadt vagy bénult, a buszsofőr köre automatikusan átugorható, amíg a busz újra mozgásképes nem lesz. Ha az átugrás közben teljes kör zárul, akkor ugyanúgy lefut az `5` tickes szimuláció.

Egy tick alatt a játék a regisztrált `ITickable` elemek pillanatképén megy végig. Az adott tick közben újonnan felvett elem csak a következő tickben fut. A pályák fix tickable elemei a sávok és az automata autók; játékosregisztrációkor minden Cleaner egy hókotrót, minden BusDriver egy buszt ad még a tickable listához. Új hókotró vásárlásakor az új jármű is tickable lesz.

### Játékosprofilok

| Szerep | Kezdő állapot | Fő cél | Pont vagy pénz |
| --- | --- | --- | --- |
| Hókotrós (`Cleaner`) | `0` pénz, egy hókotró, seprűs eke | Havas vagy jeges sávok takarítása, felszerelések vásárlása | Sikeresen tisztára takarított sávért `+1` érme jár. Ugyanaz a sáv csak akkor ad újra pénzt, ha közben újra koszos lett. |
| Busz sofőr (`BusDriver`) | `0` pont, egy busz | A két buszmegálló között minél többször eljutni célba | Célmegálló elérésekor `+1` pont jár, majd a start és cél megálló felcserélődik. |

Regisztrációs megkötések:

- Buszsofőr létrehozásához pontosan `2` `BusStop` kell a pályán.
- Hókotrós létrehozásához legalább `1` `Depot` kell a pályán.
- A dinamikusan létrehozott busz az első olyan buszmegállóból indul, amelynek van kimenő sávja.
- A dinamikusan létrehozott hókotró az első depó első kimenő útjának első sávjára kerül.

### Bolt és árak

| Tétel | Ár | Megvásárolható | Mire használható |
| --- | ---: | --- | --- |
| `Salt` | `2` | igen | `SaltPlow` feltöltése, sózás hóra vagy jégre. |
| `Gravel` | `2` | igen | `GravelPlow` feltöltése, jeges sáv kavicsozása. |
| `Biokerosene` | `4` | igen | `DragonPlow` feltöltése, hó és jég azonnali tisztítása. |
| `SweeperPlow` | `0` konstansban | nem | Alap eke, új hókotró ezzel indul. Külön boltban nem vehető. |
| `GravelPlow` | `8` | igen | Kavicsot szór jeges sávra. |
| `DumpPlow` | `10` | igen | Vékony vagy vastag havat eltávolít a sávból. |
| `IcebreakerPlow` | `10` | igen | Jeget vékony hóvá tör. |
| `SaltPlow` | `12` | igen | Sót szór hóra vagy jégre. |
| `DragonPlow` | `15` | igen | Biokerozinnal bármely nem tiszta sávot tisztára olvaszt. |
| `Snowplow` | `25` | igen | Új hókotró, seprűs ekével, a meglévő flotta aktuális sávjára kerül. |

Vásárlási és felszerelési megkötések:

- A bolt csak akkor enged vásárolni, ha a Cleaner pénztárcájában van elég pénz.
- A megvett `Salt`, `Gravel` és `Biokerosene` mindig `1` egységnyi mennyiséggel kerül a raktárba.
- Utántöltéskor a fogyóeszköz kikerül a raktárból, és a felszerelt eke tartálya lesz belőle.
- Csak típushelyes fogyóeszköz tölthető be: `SaltPlow` csak sót, `GravelPlow` csak kavicsot, `DragonPlow` csak biokerozint fogad.
- Egy adott ekefajtából a Cleaner legfeljebb annyit birtokolhat, ahány hókotrója van. Ez akadályozza meg, hogy minden hókotróra jutó darabszám fölé vásároljon ugyanabból az ekeosztályból.
- A hókotró csak a saját Cleaner játékos flottájában lévő járművet irányíthatja vagy szerelheti.

### Ekék és fogyóanyagok

| Eke | Fogyóanyag | Hatás | Fontos korlát |
| --- | --- | --- | --- |
| `SweeperPlow` | nincs | A vékony vagy vastag havat a jobb oldali szomszédos sávra tolja, majd a saját sávot tisztára állítja. | Ha nincs jobb sáv, a hó nem kerül át máshová, de a saját sáv tiszta lesz. |
| `DumpPlow` | nincs | Vékony vagy vastag havat közvetlenül eltávolít. | Jégen és tiszta sávon nincs takarítási hatása. |
| `IcebreakerPlow` | nincs | Jeget vékony hóvá alakít. | Nem teszi azonnal tisztává a sávot. |
| `SaltPlow` | `Salt` | Hó vagy jég sózása után `2` tick múlva tiszta állapot jön létre. | Üres tartállyal hibát ad. Egy használat `1` egységet fogyaszt. |
| `GravelPlow` | `Gravel` | Jégen `GraveledIceCondition` állapotot hoz létre. | Vastag vagy vékony havon nincs közvetlen állapotváltozás. Egy használat `1` egységet fogyaszt. |
| `DragonPlow` | `Biokerosene` | Bármely nem tiszta sávot azonnal `CleanCondition` állapotba állít. | Tiszta sávon nincs hatása. Egy használat `1` egységet fogyaszt. |

### Sávállapotok

| Állapot | Létrejön | Járműhatás | Takarítás / kezelés |
| --- | --- | --- | --- |
| `CleanCondition` | Alapállapot, takarítás után, só olvadása után | Nincs negatív hatás | Sózás után `4` tick hóvédettséget ad. |
| `ThinSnowCondition` | Tiszta sávból `5` tick után vagy kavicsozott jégre havazva | Bénítható járművek letapossák; `20` áthaladás után jég lehet | Seprűs/dömperes eke tisztítja, só `2` tick után tisztítja. |
| `ThickSnowCondition` | Vékony hóból további `5` tick után, jégre havazva | Busz és autó elakad; hókotró haladhat | Seprűs/dömperes/sárkány eke tisztíthatja, só `2` tick után tisztítja. |
| `IceCondition` | Vékony hó letaposásából vagy pálya kezdőállapotból | Busz és autó `20%` eséllyel megcsúszik és `2` tickre bénul | Só `2` tick után tisztít, kavics kavicsozott jéggé alakít, jégtörő vékony hóvá töri, sárkány eke tisztítja. |
| `GraveledIceCondition` | Kavics jeges sávra szórásából | Nem bénít, a megcsúszási logika nem fut le | Havazás vékony hóvá alakíthatja, további kavicsnak nincs halmozott hatása. |

Alagutaknál a sávállapotok tickje nem növeli a havazást és nem futtatja az automatikus hó-jég átalakulást. A pályafájlban megadott kezdőállapot ettől még létezhet, de az időjárási tick nem rontja tovább.

### Járművek és mozgás

| Jármű | Ki irányítja | Automatikus útvonal | Bénítható | Mozgási szabály |
| --- | --- | --- | --- | --- |
| Busz | Busz sofőr | nincs, kézi célút és célsáv kell | igen | Tickenként `+1` progress, vastag hóban nem halad, célmegállónál pontot ad. |
| Hókotró | Hókotrós | nincs, kézi célút és célsáv kell | nem | Tickenként előbb takarít, majd `+1` progress. Jégen és vastag hóban nem bénul. |
| Autó | modell | BFS-sel választ utat otthon és munkahely között | igen | Tickenként halad, akadály előtt szomszéd sávot keres, célba érve várakozik. |

A kézi mozgásnál a megadott sávnak ténylegesen a megadott úthoz kell tartoznia. A célok a jelenlegi sáv célcsomópontjából kimenő utak sávjai közül választhatók ki.

### Pályaprofilok

| Pálya | Nehézség | Init / layout | Node-ok | Utak | Sávok | Kezdő autók | Kezdő sávállapotok | Fix tickable elemek |
| --- | --- | --- | ---: | ---: | ---: | ---: | --- | ---: |
| `tutorial` - Oktatópálya - Rövid útvonal | Könnyű | `maps/tutorial-map-init.txt` / `maps/tutorial-map-layout.txt` | `6` | `10` | `18` | `3` | `10` tiszta, `5` vékony hó, `0` vastag hó, `3` jég | `21` |
| `base` - Alappálya - Városi kör | Közepes | `maps/base-map-init.txt` / `maps/base-map-layout.txt` | `9` | `16` | `28` | `6` | `13` tiszta, `8` vékony hó, `1` vastag hó, `6` jég | `34` |
| `city` - Forgalmi pálya - Több megálló és kereszteződés | Közepes | `maps/city-map-init.txt` / `maps/city-map-layout.txt` | `9` | `18` | `26` | `8` | `14` tiszta, `6` vékony hó, `1` vastag hó, `5` jég | `34` |
| `blizzard` - Viharzóna - Nehéz mentési helyzet | Nehéz | `maps/blizzard-map-init.txt` / `maps/blizzard-map-layout.txt` | `9` | `18` | `23` | `7` | `12` tiszta, `4` vékony hó, `4` vastag hó, `3` jég | `30` |

A node bontás a négy grafikus pályán egységesen `1` depó, `2` buszmegálló, `3` épület és `3` kereszteződés, kivéve az oktatópályát, ahol `1` depó, `2` buszmegálló, `2` épület és `1` kereszteződés van. A konzolos pálya `1` depót, `2` buszmegállót, `1` épületet és `2` kereszteződést tartalmaz.

Többsávos útszakaszok:

- `tutorial`: `road_home_cross`, `road_cross_home`, `road_cross_work`, `road_work_cross` - mind `3` sávos.
- `base`: `road_home_to_cross1`, `road_cross1_to_home`, `road_cross1_to_cross2`, `road_cross2_to_cross1`, `tunnel_cross2_to_cross3`, `tunnel_cross3_to_cross2` - mind `3` sávos.
- `city`: `road_crossW_crossC`, `road_crossC_crossW`, `road_crossC_crossE`, `road_crossE_crossC` - mind `3` sávos.
- `blizzard`: `road_blizz_n_center` `4` sávos, `road_blizz_center_n` `3` sávos.

### Hidak és alagutak

A `Bridge` és `Tunnel` osztály támogat explicit `paired` kapcsolatot, de a jelenlegi pályafájlokban nincs `paired` link megadva. A gyakorlatban az összetartozó irányok névvel és célcsomóponttal vannak modellezve.

| Pálya | Hidak | Alagutak | Megjegyzés |
| --- | --- | --- | --- |
| `tutorial` | nincs | nincs | Csak normál utak. |
| `base` | nincs | `tunnel_cross2_to_cross3 -> cross3`, `tunnel_cross3_to_cross2 -> cross2` | Mindkét alagút `3` sávos. |
| `city` | `bridge_depot_crossC -> city_cross_c`, `bridge_crossC_depot -> city_depot` | nincs | Depó és központ közötti kétirányú hídpár, irányonként `1` sávval. |
| `blizzard` | `bridge_blizz_center_factory -> blizz_factory`, `bridge_blizz_factory_center -> blizz_cross_c` | `tunnel_blizz_north_factory -> blizz_factory`, `tunnel_blizz_factory_north -> blizz_cross_n` | Hídpár a központ és gyár között, alagútpár az északi csomópont és gyár között. |


### Grafikus fix megjelenítési profil

| Elem | Fix megjelenítés |
| --- | --- |
| Tiszta út | fekete |
| Vékony hó | világosszürke |
| Vastag hó | fehér |
| Jég | világoskék |
| Sózott jég | sötétkék |
| Kavicsozott jég | barna |
| Busz | sárga |
| Autó | piros |
| Hókotró | zöld test, az eke színe az eke típusától függ |
| Buszmegálló node | sárga |
| Depó node | zöld |
| Épület node | piros |
| Kereszteződés node | fekete |

Render konstansok:

- sáv belső vonalvastagság: `3` pixel;
- sáv körvonalvastagság: `5` pixel;
- csomópont alap átmérő: `28` pixel;
- kijelölés színe: sárga kiemelés;
- a jármű pozíciója a `progress / laneLength` arány alapján interpolálódik a sáv rajzolt eleje és vége között.

---

## Konzolos mód

A konzolvezérlés automatikusan csatlakozik a futó játékhoz, amikor a játék ténylegesen elindul.

Amíg nincs kiválasztott pálya és elindított játék, a konzol nem ad játékmenet-visszajelzést.

Elérhető vezérlőparancsok:

- `h` vagy `help` - súgó megjelenítése.
- `c` vagy `command` - az aktuális szerepkör parancsainak megjelenítése.

Játék közben konzolon kiadott parancsok azonnal látszanak GUI-n, és GUI műveletek is ugyanazt a konzolos játékállapotot módosítják.

Játék végén a konzol kiírja, hogy vége a játéknak, és listázza az eredményeket is.
