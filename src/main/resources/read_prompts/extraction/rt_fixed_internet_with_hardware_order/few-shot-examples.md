--- EXAMPLE 1 (broker email — fiber internet with router and express installation) ---
INPUT:
Dobrý den,
objednávám pro nového zákazníka Filipa Dvořáka pevný internet.
Kontakt: 602 444 555
E-mail: filip.dvorak@email.cz
Adresa instalace: Jabloňová 18, Brno, 62800
Služba: Optika Domů 300
Hardware: Wi-Fi Router Standard
Doplňek: expresní instalace
Závazek: 24 měsíců

OUTPUT:
{"customerStatus":"new","customerName":"Filip Dvořák","contactPhone":"+420 602 444 555","contactEmail":"filip.dvorak@email.cz","installationAddress":"Brno, Jabloňová 18, 62800","requestedServices":[{"serviceId":"svc_internet_fiber_300","quantity":1}],"requestedProducts":[{"productId":"prod_router_standard","quantity":1},{"productId":"prod_installation_express","quantity":1}],"contractTermMonths":24}

--- EXAMPLE 2 (CRM ticket — existing customer, wireless internet with pro router and mesh) ---
INPUT:
Stávající zákazník: Hana Černá
Tel.: +420 777 444 111
Adresa: Polní 7, Znojmo, 66902
Požadavek: bezdrátový internet 50
HW: router pro + mesh node 2x
Závazek 0 měs.
Email neuveden.

OUTPUT:
{"customerStatus":"existing","customerName":"Hana Černá","contactPhone":"+420 777 444 111","contactEmail":null,"installationAddress":"Znojmo, Polní 7, 66902","requestedServices":[{"serviceId":"svc_internet_wireless_50","quantity":1}],"requestedProducts":[{"productId":"prod_router_pro","quantity":1},{"productId":"prod_mesh_node","quantity":2}],"contractTermMonths":0}

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
{"customerStatus":"new","customerName":"Pavel Marek","contactPhone":"+420 604 222 333","contactEmail":null,"installationAddress":"Olomouc, Na Výsluní 9, 77900","requestedServices":[{"serviceId":"svc_internet_dsl_100","quantity":1}],"requestedProducts":[{"productId":"prod_router_standard","quantity":1}],"contractTermMonths":24}
