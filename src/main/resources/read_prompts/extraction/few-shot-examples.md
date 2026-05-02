Ukázky obecného zacházení s formáty a nejasnostmi:

- Telefonní čísla mohou být ve vstupu uvedena různě deformovaná. Pokud je celé číslo zřejmé, vrať normalizovaný tvar
  používaný v procesu.
    - zápis ve vstupu: 9888000055
    - hodnota pro proces: +420 9888 000 055
    - zápis ve vstupu: +420-9888-100-099
    - hodnota pro proces: +420 9888 100 099

- E-mail může mít odlišnou velikost písmen. Pokud je adresa zřejmá, vrať ji v malých písmenech.
    - zápis ve vstupu: Zakaznik55@Prikladova.CZ
    - hodnota pro proces: zakaznik55@prikladova.cz

- Jméno zákazníka může obsahovat oslovení nebo být uvedeno v obráceném pořadí. Do procesu patří jméno bez oslovení
  a bez titulů.
    - zápis ve vstupu: paní Monika Vlčková
    - hodnota pro proces: Monika Vlčková
    - zápis ve vstupu: Šimáčka František
    - hodnota pro proces: František Šimáčka

- Instalační adresa může být uvedena volným textem. Extrahuj dostupné složky (PSČ, město, ulici, číslo popisné)
  jako strukturovaný objekt. Pokud některá složka ve vstupu chybí, vyplň null.
    - zápis ve vstupu: „Bydlím na Znojemské 9, Jihlava, 586 01"
    - hodnota pro proces: { psc: "586 01", city: "Jihlava", street: "Znojemská", houseNumber: "9" }
    - zápis ve vstupu: „adresa instalace je Šumperk"
    - hodnota pro proces: { psc: null, city: "Šumperk", street: null, houseNumber: null }

- Katalogové služby, produkty, slevy a operátory mapuj pouze podle doménového kontextu. Pokud text obsahuje synonymum
  nebo obchodní název z kontextu, vrať odpovídající ID. Pokud hodnota v kontextu není, nehádej nové ID, vrať null.
    - zápis ve vstupu: „chci tarif Rychlík 200" (název v doménovém kontextu není)
    - hodnota pro proces: null

- Pokud je údaj ve vstupu chybějící, neodvozuj jej z běžných obchodních zvyklostí. Pro jednotlivou chybějící hodnotu
  použij null. Pro chybějící seznam použij prázdný seznam.
    - zákazník nezmíní žádnou slevu → requestedDiscountIds: []
    - zákazník nezmíní e-mail → contactEmail: null

- Pokud text obsahuje šum, poznámku operátora, smalltalk nebo interní zkratky, extrahuj jen procesně relevantní
  údaje odpovídající výstupnímu schématu.
    - ve vstupu je přepis hovoru s pozdravy a zdvořilostními frázemi → extrahuj pouze hodnoty polí z výstupního schématu
