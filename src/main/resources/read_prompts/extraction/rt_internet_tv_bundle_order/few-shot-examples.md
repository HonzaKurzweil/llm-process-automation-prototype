--- EXAMPLE 1 (broker email — new bundle with sports pack and bundle discount) ---
INPUT:
Dobrý den,
zasílám objednávku balíčku internet + TV pro nového zákazníka Jiřího Beneše.
Telefon: 603 444 777
E-mail: jiri.benes@email.cz
Instalační adresa: K Rybníku 12, Praha, 10000
Internet: Optika Domů 1000
TV: Televize Family
Produkty: set-top box, Sport Plus, router pro
Závazek: 24 měsíců
Sleva: Balíček 10 % pro nové zákazníky

OUTPUT:
{"customerStatus":"new","customerName":"Jiří Beneš","contactPhone":"+420 603 444 777","contactEmail":"jiri.benes@email.cz","installationAddress":"Praha, K Rybníku 12, 10000","requestedServices":[{"serviceId":"svc_internet_fiber_1000","quantity":1},{"serviceId":"svc_tv_family","quantity":1}],"requestedProducts":[{"productId":"prod_set_top_box","quantity":1},{"productId":"prod_tv_sports_pack","quantity":1},{"productId":"prod_router_pro","quantity":1}],"contractTermMonths":24,"requestedDiscounts":[{"discountId":"disc_bundle_10_new"}]}

--- EXAMPLE 2 (CRM ticket — existing customer, basic bundle without products) ---
INPUT:
Stávající zákazník: Alena Svobodová
Tel. 777 121 212
Adresa instalace: Lesní 4, Liberec, 46001
Požadavek: Optika Domů 300 + Televize Basic
Závazek 24 měsíců
Bez dalších produktů a bez slevy.

OUTPUT:
{"customerStatus":"existing","customerName":"Alena Svobodová","contactPhone":"+420 777 121 212","contactEmail":null,"installationAddress":"Liberec, Lesní 4, 46001","requestedServices":[{"serviceId":"svc_internet_fiber_300","quantity":1},{"serviceId":"svc_tv_basic","quantity":1}],"requestedProducts":null,"contractTermMonths":24,"requestedDiscounts":null}

--- EXAMPLE 3 (call transcript — DSL bundle with set-top box) ---
INPUT:
Operátor: Co si přejete?
Zákazník: Chtěl bych internet s televizí.
Operátor: Jaké služby konkrétně?
Zákazník: DSL stovku a Televizi Family.
Operátor: Jméno a kontakt?
Zákazník: Jsem nový zákazník, jmenuju se Radek Tichý, telefon 608999111.
Operátor: Adresa instalace?
Zákazník: Smetanova 25, Pardubice, 53002.
Operátor: Budete chtít set-top box nebo router?
Zákazník: Set-top box ano, router standardní taky. Závazek na 24 měsíců.

OUTPUT:
{"customerStatus":"new","customerName":"Radek Tichý","contactPhone":"+420 608 999 111","contactEmail":null,"installationAddress":"Pardubice, Smetanova 25, 53002","requestedServices":[{"serviceId":"svc_internet_dsl_100","quantity":1},{"serviceId":"svc_tv_family","quantity":1}],"requestedProducts":[{"productId":"prod_set_top_box","quantity":1},{"productId":"prod_router_standard","quantity":1}],"contractTermMonths":24,"requestedDiscounts":null}
