![Java](https://img.shields.io/badge/Java-21+-007396?style=flat&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-javac-orange?style=flat)
![Status](https://img.shields.io/badge/Status-Skeleton-blue?style=flat)

# Hokotrok-Purgatorium

A "Hokotrok-Purgatorium" egy Java nyelven írt projekt, amely a "Hókotrók" játékot valósítja meg.

---

## Branch struktúra

- A projekt jelenleg 3 külön branch-en fut: `skeleton`, `proto`, `graphics`.
- A `skeleton` branch egy ideiglenes váz és a fő osztály létrehozására szolgál.
- A `proto` branch a prototípusos implementáció helye, ahol a fő logika és a tesztek kerülnek megvalósításra.
- A `graphics` branch a grafikus megjelenítés fejlesztésére szolgál, ahol a vizuális elemek és a játék megjelenítése lesz a fókuszban.

A fejlesztés során a `proto` branch a `skeleton` branch-hez képest tartalmazza a fejlettebb implementációkat. Miután a `proto` branch-ben a fő logika és a tesztek elkészülnek, a `graphics` branch-re kerül át a fejlesztés, ahol a vizuális megjelenítés lesz a fő cél. Ez a struktúra lehetővé teszi, hogy a fejlesztés különböző aspektusai párhuzamosan haladjanak, miközben megőrizzük a kód tisztaságát és szervezettségét. A végső cél az, hogy a `graphics` branch-ben egy teljesen működőképes és vizuálisan is vonzó hókotró szimulátor legyen elérhető. Amely majd a `main` branch-re kerül, miután minden fejlesztés befejeződött és tesztelve lett.