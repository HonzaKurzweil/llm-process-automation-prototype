--- EXAMPLE 1 (broker email — fiber internet with router and express installation) ---
INPUT:
Dobrý den,
objednávám pro nového zákazníka Filipa Dvořáka pevný internet.
Kontakt: 602 444 555
E-mail: filip.dvorak@email.cz
Adresa instalace: Jabloňová 18, Brno, 62800
Služba: optický internet 300 Mb/s
Hardware: standardní router
Doplňek: expresní instalace
Závazek: 24 měsíců
Poznámka: montáž po 17. hodině

OUTPUT:
{"customer_status":"new","customer_name":"Filip Dvořák","contact_phone":"+420 602 444 555","installation_address":{"street":"Jabloňová 18","city":"Brno","zip_code":"62800"},"requested_services":[{"service_id":"svc_internet_fiber_300","quantity":1}],"requested_products":[{"product_id":"prod_router_standard","quantity":1},{"product_id":"prod_installation_express","quantity":1}],"contract_term_months":24,"contact_email":"filip.dvorak@email.cz","requested_discounts":null,"notes":"montáž po 17. hodině"}

--- EXAMPLE 2 (CRM ticket — existing customer, wireless internet with pro router and mesh) ---
INPUT:
Stávající zákazník: Hana Černá
Tel.: +420 777 444 111
Adresa: Polní 7, Znojmo, 66902
Požadavek: internet wireless 50
HW: router pro + mesh node
Závazek 12 měs.
Email neuveden.

OUTPUT:
{"customer_status":"existing","customer_name":"Hana Černá","contact_phone":"+420 777 444 111","installation_address":{"street":"Polní 7","city":"Znojmo","zip_code":"66902"},"requested_services":[{"service_id":"svc_internet_wireless_50","quantity":1}],"requested_products":[{"product_id":"prod_router_pro","quantity":1},{"product_id":"prod_mesh_node","quantity":1}],"contract_term_months":12,"contact_email":null,"requested_discounts":null,"notes":null}

--- EXAMPLE 3 (call transcript — DSL with standard router) ---
INPUT:
Operátor: Jakou službu chcete objednat?
Zákazník: Potřebuji internet domů, DSL stovku.
Operátor: Jméno?
Zákazník: Pavel Marek, jsem nový zákazník.
Operátor: Kontaktní telefon?
Zákazník: 604222333.
Operátor: Adresa instalace?
Zákazník: Na Výsluní 9, Olomouc, 77900.
Operátor: Router?
Zákazník: Ano, ten standardní.
Operátor: Na jak dlouho závazek?
Zákazník: 24 měsíců.

OUTPUT:
{"customer_status":"new","customer_name":"Pavel Marek","contact_phone":"+420 604 222 333","installation_address":{"street":"Na Výsluní 9","city":"Olomouc","zip_code":"77900"},"requested_services":[{"service_id":"svc_internet_dsl_100","quantity":1}],"requested_products":[{"product_id":"prod_router_standard","quantity":1}],"contract_term_months":24,"contact_email":null,"requested_discounts":null,"notes":null}
