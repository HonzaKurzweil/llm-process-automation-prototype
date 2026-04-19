--- EXAMPLE 1 (broker email — family order with partial porting) ---
INPUT:
Dobrý den,
zasílám rodinnou objednávku pro klienta Martina Konečného, nový zákazník.
Kontakt: +420 774 555 444
E-mail: martin.konecny@email.cz
Tarif: Mobil Family Plus
Počet linek: 3
Linka 1: přenos ano, číslo 731 555 111, operátor O2
Linka 2: přenos ano, číslo 732 666 222, operátor T-Mobile
Linka 3: nové číslo
Závazek: 24 měsíců
Slevy: rodinná sleva 100 Kč na další linku, sleva za přenos čísla

OUTPUT:
{"customer_status":"new","customer_name":"Martin Konečný","contact_phone":"+420 774 555 444","requested_services":[{"service_id":"svc_mobile_family_plus","quantity":1}],"mobile_lines_count":3,"mobile_lines":[{"porting_requested":true},{"porting_requested":true},{"porting_requested":false}],"contract_term_months":24,"ported_numbers":[{"number":"+420 731 555 111","donor_operator":"O2"},{"number":"+420 732 666 222","donor_operator":"T-Mobile"}],"contact_email":"martin.konecny@email.cz","requested_discounts":[{"discount_id":"disc_family_line_100"},{"discount_id":"disc_porting_mobile_200"}],"notes":null}

--- EXAMPLE 2 (CRM ticket — existing customer, no porting) ---
INPUT:
Stávající zákazník: Lucie Procházková
Telefon: 605 111 222
Požadavek: rodinný mobilní tarif Family Plus, 4 linky
Portace: ne, všechna čísla nová v síti
Závazek 24 měsíců
Sleva: family line 100
Pozn.: aktivace všech linek najednou

OUTPUT:
{"customer_status":"existing","customer_name":"Lucie Procházková","contact_phone":"+420 605 111 222","requested_services":[{"service_id":"svc_mobile_family_plus","quantity":1}],"mobile_lines_count":4,"mobile_lines":[{"porting_requested":false},{"porting_requested":false},{"porting_requested":false},{"porting_requested":false}],"contract_term_months":24,"ported_numbers":null,"contact_email":null,"requested_discounts":[{"discount_id":"disc_family_line_100"}],"notes":"aktivace všech linek najednou"}

--- EXAMPLE 3 (call transcript — two lines, one ported) ---
INPUT:
Operátor: Co pro vás mohu udělat?
Zákazník: Chci rodinný tarif pro dvě čísla.
Operátor: Jméno?
Zákazník: Eva Jelínková, jsem nová zákaznice.
Operátor: Kontaktní telefon?
Zákazník: 777123456.
Operátor: Bude to Family Plus?
Zákazník: Ano. Jedno číslo chci přenést od Vodafone, je to 734 222 999. Druhé bude nové.
Operátor: Závazek?
Zákazník: 12 měsíců.
Operátor: Nějaká sleva?
Zákazník: Jen tu za přenos.

OUTPUT:
{"customer_status":"new","customer_name":"Eva Jelínková","contact_phone":"+420 777 123 456","requested_services":[{"service_id":"svc_mobile_family_plus","quantity":1}],"mobile_lines_count":2,"mobile_lines":[{"porting_requested":true},{"porting_requested":false}],"contract_term_months":12,"ported_numbers":[{"number":"+420 734 222 999","donor_operator":"Vodafone"}],"contact_email":null,"requested_discounts":[{"discount_id":"disc_porting_mobile_200"}],"notes":null}
