--- EXAMPLE 1 (broker email — existing customer, no porting) ---
INPUT:
Dobrý den,
posílám objednávku nové mobilní služby pro stávající zákaznici Janu Malou.
Jméno zákaznice: Jana Malá
Telefon: 777 888 999
E-mail: jana.mala@email.cz
Požadovaný tarif: Mobil Start 5G, 1x
Závazek: 12 měsíců
Portace: ne
Poznámka: aktivaci prosíme od 1. 5.

OUTPUT:
{"customer_status":"existing","customer_name":"Jana Malá","contact_phone":"+420 777 888 999","requested_services":[{"service_id":"svc_mobile_start_5g","quantity":1}],"contract_term_months":12,"porting_requested":false,"ported_numbers":null,"contact_email":"jana.mala@email.cz","requested_discounts":null,"notes":"aktivaci prosíme od 1. 5."}

--- EXAMPLE 2 (CRM ticket — new customer, porting requested, number not yet provided) ---
INPUT:
Nový zákazník – Petr Veselý
Tel.: 603 112 445
Tarif: Mobil Neomezeně 5G – 1x
Závazek: 24 měs.
Portace: ANO – číslo zatím neposkytnuto
Sleva: Přenos čísla 200 Kč / 6 měs.

OUTPUT:
{"customer_status":"new","customer_name":"Petr Veselý","contact_phone":"+420 603 112 445","requested_services":[{"service_id":"svc_mobile_unlimited_5g","quantity":1}],"contract_term_months":24,"porting_requested":true,"ported_numbers":null,"contact_email":null,"requested_discounts":[{"discount_id":"disc_porting_mobile_200"}],"notes":null}

--- EXAMPLE 3 (call transcript — new customer, complete porting) ---
INPUT:
Operátor: Dobrý den, co byste si přál?
Zákazník: Chtěl bych nový tarif Mobil Start 5G.
Operátor: Bude to pro nového zákazníka?
Zákazník: Ano, na moje jméno Tomáš Král. Volejte mi na 605334221.
Operátor: Portace čísla?
Zákazník: Ano, chci převést číslo 731 222 111 od Vodafone.
Operátor: Na jak dlouho závazek?
Zákazník: Na 24 měsíců. A když bude, tak i sleva za přenos.
Operátor: Dobře.

OUTPUT:
{"customer_status":"new","customer_name":"Tomáš Král","contact_phone":"+420 605 334 221","requested_services":[{"service_id":"svc_mobile_start_5g","quantity":1}],"contract_term_months":24,"porting_requested":true,"ported_numbers":[{"number":"+420 731 222 111","donor_operator":"Vodafone"}],"contact_email":null,"requested_discounts":[{"discount_id":"disc_porting_mobile_200"}],"notes":null}
