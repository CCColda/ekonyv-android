# EKönyv android frontend
Egy [Arduino MKR Zero-n + MKR ETH shielden futó szerver](https://github.com/cccolda/ekonyv-arduino)
frontendje. Biztosítja a felhasználókezelést, könyvek hozzáadását, ISBN-szkennelést,
és lekérdezést a NEKTÁR szervereiről.

## Roadmap
- [ ] IP-cím kezelés
  - [ ] Szerver kiválasztása (felhasználói IP-cím)
  - [ ] Szerver keresése helyi hálózaton
  - [ ] Szerver IP-címének tárolása, ellenőrzése indításkor
  - [ ] Szerver IP-címének ellenőrzése az `/ekonyv` végponttal
- [ ] Felhasználók hitelesítése
  - [ ] Regisztráció (felhasználónév, jelszó, kód)
  - [ ] Bejelentkezés
  - [ ] Bejelentkezési adatok megjegyzésének felajánlása
  - [ ] Session token-ek tárolása, frissítése 401-es kód esetén
- [ ] Felhasználói végpontok 
  - [ ] Felhasználói adatok lekérése, módosítása
  - [ ] Könyvek lekérdezése, szűrése
  - [ ] Könyv hozzáadása manuálisan
  - [ ] Könyv hozzáadása ISBN-ből
  - [ ] Adott könyv módosítása
  - [ ] Adott könyv törlése
- [ ] Offline tárolás
  - [ ] Könyvek tárolása offline
  - [ ] Offline hozzáadott könyvek várólistás tárolása
  - [ ] Frissítés a szerver állományából módosítási idő alapján
- [ ] ISBN-vonalkód leolvasása
- [ ] OSZK lekérdezés ISBN kódokra
- [ ] Fallback lekérdezés nemzetközi adatbázisból

