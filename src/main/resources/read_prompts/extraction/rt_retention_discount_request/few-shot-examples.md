### Příklad 1
Vstup:
CRM: stávající zákazník Karel Bílý, tel. 9999000666. Volá kvůli retenci a chce lepší cenu ke službě, ale konkrétní služba ani sleva nejsou v poznámce uvedeny. Zmiňuje konkurenci, text nabídky nevyplňovat do DTO.

Výstup:
```json
{
  "customerStatus": "existing",
  "customerName": "Karel Bílý",
  "contactPhone": "+420 9999 000 666",
  "retentionCase": true,
  "currentServiceIds": [],
  "targetServiceId": null,
  "requestedDiscountIds": []
}
```
