--- EXAMPLE 1 (CRM ticket — ported number not yet provided) ---
INPUT:
Nový zákazník – Petr Veselý
Tel.: 603 112 445
Tarif: Mobil Neomezeně 5G – 1x
Závazek: 24 měs.
Portace: ANO – číslo zatím neposkytnuto
Sleva: Přenos čísla 200 Kč / 6 měs.

OUTPUT:
{"customer_status":"new","customer_name":"Petr Veselý","contact_phone":"+420 603 112 445","requested_services":[{"service_id":"svc_mobile_unlimited_5g","quantity":1}],"contract_term_months":24,"porting_requested":true,"ported_numbers":null,"contact_email":null,"requested_discounts":[{"discount_id":"disc_porting_mobile_200"}],"notes":null}

--- EXAMPLE 2 (broker email — complete valid order with porting) ---
INPUT:
Dobrý den,
zasílám objednávku nové mobilní služby pro zákaznici Kláru Horákovou.
Zákaznice: Klára Horáková (nová zákaznice)
Telefon: +420 777 210 884
E-mail: klara.horakova@email.cz
Tarif: Mobil Start 5G, 1 linka
Délka závazku: 24 měsíců
Portace čísla: ano – přenášené číslo +420 731 555 888, dosavadní operátor O2
Sleva: Přenos čísla 200 Kč na 6 měsíců

OUTPUT:
{"customer_status":"new","customer_name":"Klára Horáková","contact_phone":"+420 777 210 884","requested_services":[{"service_id":"svc_mobile_start_5g","quantity":1}],"contract_term_months":24,"porting_requested":true,"ported_numbers":[{"number":"+420 731 555 888","donor_operator":"O2"}],"contact_email":"klara.horakova@email.cz","requested_discounts":[{"discount_id":"disc_porting_mobile_200"}],"notes":null}
