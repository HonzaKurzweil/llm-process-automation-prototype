Formátovací pravidla demonstrovaná v příkladech:

- `contactPhone`: česká čísla ve tvaru `+420 XXX XXX XXX`.
- `mobileLines[].label`: první linka `hlavní linka`, druhá `druhá linka`, třetí `třetí linka` atd.

--- EXAMPLE 1 (broker email — complete valid reference) ---
INPUT:
Dobrý den,
zasílám rodinnou objednávku pro nového zákazníka Tomáše Dvořáka.
Telefon: 606 200 111
Tarif: Mobil Family Plus, 3 linky
Hlavní linka: přenos ano, číslo 604 887 220 od Vodafone
Druhá linka: nové číslo
Třetí linka: nové číslo
Závazek: 24 měsíců
Sleva: 100 Kč na další linky

OUTPUT:
{"customerStatus":"new","customerName":"Tomáš Dvořák","contactPhone":"+420 606 200 111","contactEmail":null,"
requestedServices":[{"serviceId":"svc_mobile_family_plus","quantity":3}],"mobileLinesCount":3,"
mobileLines":[{"label":"hlavní linka","planServiceId":"svc_mobile_family_plus","portingRequested":true},{"label":"druhá linka","planServiceId":"svc_mobile_family_plus","portingRequested":false},{"label":"třetí linka","planServiceId":"svc_mobile_family_plus","portingRequested":false}],"
portedNumbers":[{"number":"+420 604 887 220","donorOperator":"Vodafone"}],"contractTermMonths":24,"
requestedDiscounts":[{"discountId":"disc_family_line_100"}]}

--- EXAMPLE 2 (CRM ticket — incomplete reference) ---
INPUT:
Stávající zákaznice: Jana Křížová
Tel.: 608 441 903
Požadavek: Family Plus, 2 linky
Detail linek: hlavní linka bez portace
Závazek: 24 měs.
Sleva: family line 100

OUTPUT:
{"customerStatus":"existing","customerName":"Jana Křížová","contactPhone":"+420 608 441 903","contactEmail":null,"
requestedServices":[{"serviceId":"svc_mobile_family_plus","quantity":2}],"mobileLinesCount":2,"
mobileLines":[{"label":"hlavní linka","planServiceId":"svc_mobile_family_plus","portingRequested":false}],"
portedNumbers":null,"contractTermMonths":24,"requestedDiscounts":[{"discountId":"disc_family_line_100"}]}

--- EXAMPLE 3 (call transcript — complete invalid reference) ---
INPUT:
Operátor: S čím vám pomohu?
Zákazník: Chci Family Plus.
Operátor: Pro kolik linek?
Zákazník: Jen pro jednu. Jmenuju se Roman Hlaváč, číslo 605780123, jsem stávající zákazník.
Operátor: Závazek?
Zákazník: 24 měsíců. A chci tu slevu 100 Kč na další linku.

OUTPUT:
{"customerStatus":"existing","customerName":"Roman Hlaváč","contactPhone":"+420 605 780 123","contactEmail":null,"
requestedServices":[{"serviceId":"svc_mobile_family_plus","quantity":1}],"mobileLinesCount":1,"
mobileLines":[{"label":"hlavní linka","planServiceId":"svc_mobile_family_plus","portingRequested":false}],"
portedNumbers":null,"contractTermMonths":24,"requestedDiscounts":[{"discountId":"disc_family_line_100"}]}
