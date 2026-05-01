### Příklad 1
Vstup:
CRM: paní Lenka Malá, tel 9999000333, email LENKA.MALA@EXAMPLE.COM. Rodinný mobil pro tři linky, z toho hlavní, partnerská a dětská. Závazek 24 měsíců. Konkrétní tarif není ve vstupu uveden.

Výstup:
```json
{
  "customerStatus": null,
  "customerName": "Lenka Malá",
  "contactPhone": "+420 9999 000 333",
  "contactEmail": "lenka.mala@example.com",
  "familyTariffId": null,
  "mobileLinesCount": 3,
  "mobileLines": [
    {
      "lineIndex": 1,
      "lineRole": "primary",
      "planServiceId": null,
      "portingRequested": null,
      "portedNumber": null,
      "donorOperator": null
    },
    {
      "lineIndex": 2,
      "lineRole": "partner",
      "planServiceId": null,
      "portingRequested": null,
      "portedNumber": null,
      "donorOperator": null
    },
    {
      "lineIndex": 3,
      "lineRole": "child",
      "planServiceId": null,
      "portingRequested": null,
      "portedNumber": null,
      "donorOperator": null
    }
  ],
  "contractTermMonths": 24,
  "requestedDiscountIds": []
}
```
