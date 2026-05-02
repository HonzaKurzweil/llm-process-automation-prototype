Ukázky obecného zacházení s formáty a nejasnostmi:

- Telefonní čísla mohou být ve vstupu uvedena různě deformovaná. Pokud je celé číslo zřejmé, vrať normalizovaný tvar
  používaný v procesu.
    - zápis ve vstupu: 9999000001
    - hodnota pro proces: +420 9999 000 001
    - zápis ve vstupu: +420-9999-100-003
    - hodnota pro proces: +420 9999 100 003

- E-mail může mít odlišnou velikost písmen. Pokud je adresa zřejmá, vrať ji v malých písmenech.
    - zápis ve vstupu: Customer001@Example.COM
    - hodnota pro proces: customer001@example.com

- Jméno zákazníka může obsahovat oslovení nebo být uvedeno v obráceném pořadí. Do procesu patří jméno bez oslovení a bez
  titulů.
    - zápis ve vstupu: paní Veronika Jarošová
    - hodnota pro proces: Veronika Jarošová
    - zápis ve vstupu: Čermák Martin
    - hodnota pro proces: Martin Čermák

- Adresa může být uvedena volným textem nebo zkráceně. Pokud odpovídá jedné adrese z doménového kontextu, vrať její
  katalogové ID. Pokud odpovídá více adresám nebo není v kontextu rozpoznatelná, nevyplňuj ID.

- Katalogové služby, produkty, slevy a operátory mapuj pouze podle doménového kontextu. Pokud text obsahuje synonymum
  nebo obchodní název z kontextu, vrať odpovídající ID. Pokud hodnota v kontextu není, nehádej nové ID.

- Pokud je údaj ve vstupu chybějící, neodvozuj jej z běžných obchodních zvyklostí. Pro jednotlivou chybějící hodnotu
  použij null. Pro chybějící seznam použij prázdný seznam.

- Pokud text obsahuje šum, poznámku operátora, smalltalk nebo interní zkratky, extrahuj jen procesně relevantní údaje
  odpovídající výstupnímu schématu.
