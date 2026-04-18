--- EXAMPLE 1 (broker email — new bundle with sports pack and bundle discount) ---
INPUT:
Dobrý den,
zasílám objednávku balíčku internet + TV pro nového zákazníka Jiřího Beneše.
Telefon: 603 444 777
E-mail: jiri.benes@email.cz
Instalační adresa: K Rybníku 12, Praha, 10000
Internet: optika 1000 Mb/s
TV: TV Family
Produkty: set-top box, sportovní balíček, router pro
Závazek: 24 měsíců
Sleva: nový bundle 10 %
Poznámka: instalace ideálně v sobotu

OUTPUT:
{"customer_status":"new","customer_name":"Jiří Beneš","contact_phone":"+420 603 444 777","installation_address":{"street":"K Rybníku 12","city":"Praha","zip_code":"10000"},"requested_services":[{"service_id":"svc_internet_fiber_1000","quantity":1},{"service_id":"svc_tv_family","quantity":1}],"contract_term_months":24,"requested_products":[{"product_id":"prod_set_top_box","quantity":1},{"product_id":"prod_tv_sports_pack","quantity":1},{"product_id":"prod_router_pro","quantity":1}],"requested_discounts":[{"discount_id":"disc_bundle_10_new"}],"contact_email":"jiri.benes@email.cz","notes":"instalace ideálně v sobotu"}

--- EXAMPLE 2 (CRM ticket — existing customer, basic bundle without products) ---
INPUT:
Stávající zákazník: Alena Svobodová
Tel. 777 121 212
Adresa instalace: Lesní 4, Liberec, 46001
Požadavek: internet fiber 300 + TV Basic
Závazek 12 měsíců
Bez dalších produktů a bez slevy.

OUTPUT:
{"customer_status":"existing","customer_name":"Alena Svobodová","contact_phone":"+420 777 121 212","installation_address":{"street":"Lesní 4","city":"Liberec","zip_code":"46001"},"requested_services":[{"service_id":"svc_internet_fiber_300","quantity":1},{"service_id":"svc_tv_basic","quantity":1}],"contract_term_months":12,"requested_products":null,"requested_discounts":null,"contact_email":null,"notes":null}

--- EXAMPLE 3 (call transcript — DSL bundle with set-top box) ---
INPUT:
Operátor: Co si přejete?
Zákazník: Chtěl bych internet s televizí.
Operátor: Jaké služby konkrétně?
Zákazník: DSL stovku a TV Family.
Operátor: Jméno a kontakt?
Zákazník: Jsem nový zákazník, jmenuju se Radek Tichý, telefon 608999111.
Operátor: Adresa instalace?
Zákazník: Smetanova 25, Pardubice, 53002.
Operátor: Budete chtít set-top box nebo router?
Zákazník: Set-top box ano, router standardní taky. Závazek na 24 měsíců.
Operátor: Dobře.

OUTPUT:
{"customer_status":"new","customer_name":"Radek Tichý","contact_phone":"+420 608 999 111","installation_address":{"street":"Smetanova 25","city":"Pardubice","zip_code":"53002"},"requested_services":[{"service_id":"svc_internet_dsl_100","quantity":1},{"service_id":"svc_tv_family","quantity":1}],"contract_term_months":24,"requested_products":[{"product_id":"prod_set_top_box","quantity":1},{"product_id":"prod_router_standard","quantity":1}],"requested_discounts":null,"contact_email":null,"notes":null}
