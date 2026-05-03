Ukázky obecného zacházení s formáty a nejasnostmi:

- Telefonní čísla mohou být ve vstupu uvedena různě deformovaná. Pokud je celé číslo zřejmé, vrať normalizovaný tvar
  používaný v procesu. Pravidlo platí pro všechna pole s telefonním číslem, včetně portedNumber uvnitř mobileLines.
    - zápis ve vstupu: 9888000055
    - hodnota pro proces: +420 9888 000 055
    - zápis ve vstupu: +420-9888-100-099
    - hodnota pro proces: +420 9888 100 099
    - zápis ve vstupu (portedNumber): 9999-100-006
    - hodnota pro proces (portedNumber): +420 9999 100 006

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

- PSČ normalizuj vždy na formát NNN NN (tři číslice, mezera, dvě číslice). Pokud je ve vstupu bez mezery nebo
  s odlišným formátováním, doplň mezeru na správné místo.
    - zápis ve vstupu: „19000"
    - hodnota pro proces: "190 00"
    - zápis ve vstupu: „60200"
    - hodnota pro proces: "602 00"
    - zápis ve vstupu: „PSČ: 37001"
    - hodnota pro proces: "370 01"

- Název ulice zapisuj přesně tak, jak je uveden ve vstupním textu. Nerozpisuj zkratky (tř., nám., nábř. apod.)
  a nezkracuj plné názvy.
    - zápis ve vstupu: „tř. Kosmonautů 48"
    - hodnota pro proces: street: "tř. Kosmonautů", houseNumber: "48"
    - zápis ve vstupu: „Palackého třída 97"
    - hodnota pro proces: street: "Palackého třída", houseNumber: "97"

- Katalogové služby, produkty, slevy a operátory mapuj pouze podle doménového kontextu. Pokud text obsahuje synonymum
  nebo obchodní název z kontextu, vrať odpovídající ID. Pokud hodnota v kontextu není, nehádej nové ID, vrať null.
    - zápis ve vstupu: „chci tarif Rychlík 200" (název v doménovém kontextu není)
    - hodnota pro proces: null

- Každé pole musí být podloženo vlastním signálem ve vstupním textu. Neodvozuj hodnotu jednoho pole z jiného.
    - vstup obsahuje currentServiceIds: ["svc_mobile_unlimited_5g"], ale targetServiceId není zmíněn
    - hodnota pro proces: targetServiceId: null
    - vstup obsahuje mobileTariffId, ale contactEmail není zmíněn
    - hodnota pro proces: contactEmail: null

- Rozlišuj mezi explicitní absencí (signál v textu) a chybějícím údajem (pole vůbec není zmíněno):
    - „bez závazku" → contractTermMonths: 0  (nula je platná hodnota podložená textem)
    - contractTermMonths není ve vstupu zmíněn vůbec → contractTermMonths: null
    - „bez mesh jednotek" → meshNodeQuantity: 0
    - meshNodeQuantity není zmíněn → meshNodeQuantity: null
    - „bez sportovního balíčku" → sportsPackRequested: false
    - sportsPackRequested není zmíněn → sportsPackRequested: null

- Rozlišuj mezi explicitně prázdným seznamem a chybějícím seznamem:
    - „zákazník nepožaduje žádnou další slevu" → requestedDiscountIds: []  (prázdný seznam podložený textem)
    - slevy nejsou ve vstupu zmíněny vůbec → requestedDiscountIds: null

- Pokud je údaj ve vstupu chybějící, neodvozuj jej z běžných obchodních zvyklostí.
    - zákazník nezmíní e-mail → contactEmail: null
    - zákazník nezmíní donor operátor → donorOperator: "unknown"  (nikdy null — chybějící operátor se vždy mapuje na
      unknown)

- Pokud text obsahuje šum, poznámku operátora, smalltalk nebo interní zkratky, extrahuj jen procesně relevantní
  údaje odpovídající výstupnímu schématu.
    - ve vstupu je přepis hovoru s pozdravy a zdvořilostními frázemi → extrahuj pouze hodnoty polí z výstupního schématu
