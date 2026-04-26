Formátovací pravidla demonstrovaná v příkladech:

- `contactPhone`: česká čísla ve tvaru `+420 XXX XXX XXX`.

--- EXAMPLE 1 (broker email — complete valid reference) ---
INPUT:
Dobrý den,
posílám objednávku pro novou zákaznici Simonu Havlíčkovou.
Telefon: 777 808 909
E-mail: simona.havlickova@email.cz
Tarif: Mobil Start 5G
Závazek: 24 měsíců
Portace: ano, číslo 731 222 999 od O2
Sleva: přenos čísla 200 Kč na 6 měsíců

OUTPUT:
{"customerStatus":"new","customerName":"Simona Havlíčková","contactPhone":"+420 777 808 909","contactEmail":"
simona.havlickova@email.cz","requestedServices":[{"serviceId":"svc_mobile_start_5g","quantity":1}],"contractTermMonths":
24,"portingRequested":true,"portedNumbers":[{"number":"+420 731 222 999","donorOperator":"O2"}],"
requestedDiscounts":[{"discountId":"disc_porting_mobile_200"}]}

--- EXAMPLE 2 (CRM ticket — incomplete reference) ---
INPUT:
Nový zákazník: Petr Vacek
Tel.: 603 112 445
Požadavek: Mobil Neomezeně 5G
Závazek: 24 měs.
Portace: ano, číslo dodá později
Sleva: přenos čísla 200 Kč / 6 měs.

OUTPUT:
{"customerStatus":"new","customerName":"Petr Vacek","contactPhone":"+420 603 112 445","contactEmail":null,"
requestedServices":[{"serviceId":"svc_mobile_unlimited_5g","quantity":1}],"contractTermMonths":24,"portingRequested":
true,"portedNumbers":null,"requestedDiscounts":[{"discountId":"disc_porting_mobile_200"}]}

--- EXAMPLE 3 (call transcript — complete invalid reference) ---
INPUT:
Operátor: Co pro vás mohu udělat?
Zákazník: Chci tarif Mobil Neomezeně 5G.
Operátor: Jméno a kontakt?
Zákazník: Marek Beneš, číslo 724990331. Jsem nový zákazník.
Operátor: Chcete přenést číslo?
Zákazník: Ne.
Operátor: Závazek?
Zákazník: Bez závazku. Ale chci tam tu slevu za přenos čísla.

OUTPUT:
{"customerStatus":"new","customerName":"Marek Beneš","contactPhone":"+420 724 990 331","contactEmail":null,"
requestedServices":[{"serviceId":"svc_mobile_unlimited_5g","quantity":1}],"contractTermMonths":0,"portingRequested":
false,"portedNumbers":null,"requestedDiscounts":[{"discountId":"disc_porting_mobile_200"}]}
