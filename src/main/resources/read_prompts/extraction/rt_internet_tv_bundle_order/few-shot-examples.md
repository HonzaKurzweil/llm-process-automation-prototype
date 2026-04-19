--- EXAMPLE 1 (CRM ticket — incomplete reference) ---
INPUT:
Zákazník: Veronika Jarošová – nová zákaznice
Tel: 774 551 008
Požadavek: Optika Domů 300 + TV Family, set-top box 1x
Závazek: 24 měs.
Instalační adresa zatím neuvedena.

OUTPUT:
{"customerStatus":"new","customerName":"Veronika Jarošová","contactPhone":"+420 774 551 008","contactEmail":null,"installationAddress":null,"requestedServices":[{"serviceId":"svc_internet_fiber_300","quantity":1},{"serviceId":"svc_tv_family","quantity":1}],"requestedProducts":[{"productId":"prod_set_top_box","quantity":1}],"contractTermMonths":24,"requestedDiscounts":null}

--- EXAMPLE 2 (broker email — complete valid reference) ---
INPUT:
Dobrý den,
zasílám objednávku pro nového zákazníka Martina Čermáka.
Telefon: +420 725 118 443
Adresa instalace: Praha 9, Vysočany, Sokolovská 188
Služby: Optika Domů 300 a TV Family
Produkty: 2x set-top box, Sport Plus
Závazek: 24 měsíců
Sleva: Balíček 10 % pro nové zákazníky

OUTPUT:
{"customerStatus":"new","customerName":"Martin Čermák","contactPhone":"+420 725 118 443","contactEmail":null,"installationAddress":"Praha 9, Vysočany, Sokolovská 188","requestedServices":[{"serviceId":"svc_internet_fiber_300","quantity":1},{"serviceId":"svc_tv_family","quantity":1}],"requestedProducts":[{"productId":"prod_set_top_box","quantity":2},{"productId":"prod_tv_sports_pack","quantity":1}],"contractTermMonths":24,"requestedDiscounts":[{"discountId":"disc_bundle_10_new"}]}

--- EXAMPLE 3 (call transcript — complete invalid reference) ---
INPUT:
Operátor: Co chcete objednat?
Zákazník: Internet a televizi. Optiku Domů 300 a TV Basic.
Operátor: Ještě něco dalšího?
Zákazník: Ano, Sport Plus. Jsem nový zákazník, jmenuji se Aleš Konečný, telefon 720991774.
Operátor: Adresa a závazek?
Zákazník: Rooseveltova 61, Olomouc, Nové Sady. Na 24 měsíců.

OUTPUT:
{"customerStatus":"new","customerName":"Aleš Konečný","contactPhone":"+420 720 991 774","contactEmail":null,"installationAddress":"Olomouc, Nové Sady, Rooseveltova 61","requestedServices":[{"serviceId":"svc_internet_fiber_300","quantity":1},{"serviceId":"svc_tv_basic","quantity":1}],"requestedProducts":[{"productId":"prod_tv_sports_pack","quantity":1}],"contractTermMonths":24,"requestedDiscounts":null}
