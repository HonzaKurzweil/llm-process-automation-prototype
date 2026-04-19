--- EXAMPLE 1 (broker email — complete valid reference) ---
INPUT:
Dobrý den,
posílám objednávku pro nového zákazníka Daniela Procházku.
Telefon: 702 555 910
Adresa instalace: Brno, Královo Pole, Purkyňova 85
Služba: Optika Domů 300
Hardware: router pro, 2x mesh node
Závazek: 24 měsíců

OUTPUT:
{"customerStatus":"new","customerName":"Daniel Procházka","contactPhone":"+420 702 555 910","contactEmail":null,"installationAddress":"Brno, Královo Pole, Purkyňova 85","requestedServices":[{"serviceId":"svc_internet_fiber_300","quantity":1}],"requestedProducts":[{"productId":"prod_router_pro","quantity":1},{"productId":"prod_mesh_node","quantity":2}],"contractTermMonths":24}

--- EXAMPLE 2 (CRM ticket — incomplete reference) ---
INPUT:
Nová zákaznice: Monika Šimečková
Tel.: 731 440 550
Požadavek: Optika Domů 300 + router pro
Závazek: 24 měs.
Adresa bude doplněna později.

OUTPUT:
{"customerStatus":"new","customerName":"Monika Šimečková","contactPhone":"+420 731 440 550","contactEmail":null,"installationAddress":null,"requestedServices":[{"serviceId":"svc_internet_fiber_300","quantity":1}],"requestedProducts":[{"productId":"prod_router_pro","quantity":1}],"contractTermMonths":24}

--- EXAMPLE 3 (call transcript — complete invalid reference) ---
INPUT:
Operátor: Jakou službu chcete objednat?
Zákazník: DSL stovku. Jsem stávající zákaznice Lenka Votápková.
Operátor: Telefon a adresa?
Zákazník: 739661200, Pardubice, Polabiny, Bělehradská 412.
Operátor: Jaký router?
Zákazník: Standardní i pro router zároveň.
Operátor: Závazek?
Zákazník: Bez závazku.

OUTPUT:
{"customerStatus":"existing","customerName":"Lenka Votápková","contactPhone":"+420 739 661 200","contactEmail":null,"installationAddress":"Pardubice, Polabiny, Bělehradská 412","requestedServices":[{"serviceId":"svc_internet_dsl_100","quantity":1}],"requestedProducts":[{"productId":"prod_router_standard","quantity":1},{"productId":"prod_router_pro","quantity":1}],"contractTermMonths":0}
