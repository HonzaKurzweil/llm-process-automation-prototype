--- EXAMPLE 1 (broker email — family order with partial porting) ---
INPUT:
Dobrý den,
zasílám rodinnou objednávku pro klienta Martina Konečného, nový zákazník.
Kontakt: +420 774 555 444
E-mail: martin.konecny@email.cz
Tarif: Mobil Rodina Plus
Počet linek: 3
Linka 1: přenos ano, číslo 731 555 111, operátor O2
Linka 2: přenos ano, číslo 732 666 222, operátor T-Mobile
Linka 3: nové číslo
Závazek: 24 měsíců
Slevy: rodinná sleva 100 Kč na další linku, sleva za přenos čísla

OUTPUT:
{"customerStatus":"new","customerName":"Martin Konečný","contactPhone":"+420 774 555 444","contactEmail":"martin.konecny@email.cz","requestedServices":[{"serviceId":"svc_mobile_family_plus","quantity":3}],"mobileLinesCount":3,"mobileLines":[{"label":"hlavni_linka","planServiceId":"svc_mobile_family_plus","portingRequested":true},{"label":"druha_linka","planServiceId":"svc_mobile_family_plus","portingRequested":true},{"label":"treti_linka","planServiceId":"svc_mobile_family_plus","portingRequested":false}],"portedNumbers":[{"number":"+420 731 555 111","donorOperator":"O2"},{"number":"+420 732 666 222","donorOperator":"T-Mobile"}],"contractTermMonths":24,"requestedDiscounts":[{"discountId":"disc_family_line_100"},{"discountId":"disc_porting_mobile_200"}]}

--- EXAMPLE 2 (CRM ticket — existing customer, no porting) ---
INPUT:
Stávající zákazník: Lucie Procházková
Telefon: 605 111 222
Požadavek: rodinný mobilní tarif Rodina Plus, 4 linky
Portace: ne, všechna čísla nová v síti
Závazek 24 měsíců
Sleva: rodinná linka 100

OUTPUT:
{"customerStatus":"existing","customerName":"Lucie Procházková","contactPhone":"+420 605 111 222","contactEmail":null,"requestedServices":[{"serviceId":"svc_mobile_family_plus","quantity":4}],"mobileLinesCount":4,"mobileLines":[{"label":"hlavni_linka","planServiceId":"svc_mobile_family_plus","portingRequested":false},{"label":"druha_linka","planServiceId":"svc_mobile_family_plus","portingRequested":false},{"label":"treti_linka","planServiceId":"svc_mobile_family_plus","portingRequested":false},{"label":"linka_4","planServiceId":"svc_mobile_family_plus","portingRequested":false}],"portedNumbers":null,"contractTermMonths":24,"requestedDiscounts":[{"discountId":"disc_family_line_100"}]}

--- EXAMPLE 3 (call transcript — two lines, one ported) ---
INPUT:
Operátor: Co pro vás mohu udělat?
Zákazník: Chci rodinný tarif pro dvě čísla.
Operátor: Jméno?
Zákazník: Eva Jelínková, jsem nová zákaznice.
Operátor: Kontaktní telefon?
Zákazník: 777123456.
Operátor: Bude to Mobil Rodina Plus?
Zákazník: Ano. Jedno číslo chci přenést od Vodafone, je to 734 222 999. Druhé bude nové.
Operátor: Závazek?
Zákazník: 24 měsíců.
Operátor: Nějaká sleva?
Zákazník: Jen tu za přenos.

OUTPUT:
{"customerStatus":"new","customerName":"Eva Jelínková","contactPhone":"+420 777 123 456","contactEmail":null,"requestedServices":[{"serviceId":"svc_mobile_family_plus","quantity":2}],"mobileLinesCount":2,"mobileLines":[{"label":"hlavni_linka","planServiceId":"svc_mobile_family_plus","portingRequested":true},{"label":"druha_linka","planServiceId":"svc_mobile_family_plus","portingRequested":false}],"portedNumbers":[{"number":"+420 734 222 999","donorOperator":"Vodafone"}],"contractTermMonths":24,"requestedDiscounts":[{"discountId":"disc_porting_mobile_200"}]}
