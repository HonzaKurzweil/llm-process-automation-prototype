### Příklad 1
Vstup:
CRM: nová zákaznice Jana Novotná, tel. 9999000111, e-mail JANA.NOVOTNA@EXAMPLE.COM. Chce mobilní tarif, název tarifu není ve vstupu uveden. Bez přenosu čísla. Závazek 24 měsíců.

Výstup:
```json
{
  "customerStatus": "new",
  "customerName": "Jana Novotná",
  "contactPhone": "+420 9999 000 111",
  "contactEmail": "jana.novotna@example.com",
  "mobileTariffId": null,
  "contractTermMonths": 24,
  "portingRequested": false,
  "portedNumber": null,
  "donorOperator": null,
  "requestedDiscountIds": []
}
```

### Příklad 2
Vstup:
Zákazník: Jsem už klient, jmenuji se Svoboda Pavel, kontakt +420-9999-000-222. Chci přenést číslo 9999100222. Tarif mi řekli v jiné části hovoru, tady ho nevidím.

Výstup:
```json
{
  "customerStatus": "existing",
  "customerName": "Pavel Svoboda",
  "contactPhone": "+420 9999 000 222",
  "contactEmail": null,
  "mobileTariffId": null,
  "contractTermMonths": null,
  "portingRequested": true,
  "portedNumber": "+420 9999 100 222",
  "donorOperator": null,
  "requestedDiscountIds": []
}
```
