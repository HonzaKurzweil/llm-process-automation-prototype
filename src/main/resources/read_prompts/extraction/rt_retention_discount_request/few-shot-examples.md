--- EXAMPLE 1 (CRM ticket — incomplete reference) ---
INPUT:
Retence
Zákaznice: Iveta Malá
Tel.: 736 410 902
Aktuálně využívá Optiku Domů 300.
Žádá retenční slevu 15 %.
Zvažuje odchod kvůli ceně.
Cílová služba zatím neurčena.

OUTPUT:
{"customerStatus":"existing","customerName":"Iveta Malá","contactPhone":"+420 736 410 902","retentionCase":true,"
currentServices":[{"serviceId":"svc_internet_fiber_300"}],"targetServiceId":null,"
requestedDiscounts":[{"discountId":"disc_retention_15_existing"}],"churnReason":"zvažuje odchod kvůli ceně","
competitorOffer":null}

--- EXAMPLE 2 (broker email — complete valid reference) ---
INPUT:
Dobrý den,
předávám retenční požadavek pro stávajícího zákazníka Pavla Kubíka.
Telefon: 604 321 765
Aktuální služba: Optika Domů 300
Klient chce na této službě zůstat, pokud dostane retenční slevu 15 %.
Důvod: konkurenční nabídka je levnější.
Konkurence nabízí internet 500 Mb/s za 549 Kč.

OUTPUT:
{"customerStatus":"existing","customerName":"Pavel Kubík","contactPhone":"+420 604 321 765","retentionCase":true,"
currentServices":[{"serviceId":"svc_internet_fiber_300"}],"targetServiceId":"svc_internet_fiber_300","
requestedDiscounts":[{"discountId":"disc_retention_15_existing"}],"churnReason":"konkurenční nabídka je levnější","
competitorOffer":"internet 500 Mb/s za 549 Kč"}

--- EXAMPLE 3 (call transcript — complete invalid reference) ---
INPUT:
Operátor: Voláte kvůli prodloužení smlouvy?
Zákazník: Ano. Jmenuju se Zdeněk Rybář, číslo 603998441.
Operátor: Jakou službu máte?
Zákazník: Mobil Neomezeně 5G.
Operátor: Je to retenční případ, zvažujete odchod?
Zákazník: Ne, jen chci lepší cenu při prodloužení. Ale chci retenční slevu 15 %.

OUTPUT:
{"customerStatus":"existing","customerName":"Zdeněk Rybář","contactPhone":"+420 603 998 441","retentionCase":false,"
currentServices":[{"serviceId":"svc_mobile_unlimited_5g"}],"targetServiceId":"svc_mobile_unlimited_5g","
requestedDiscounts":[{"discountId":"disc_retention_15_existing"}],"churnReason":"žádost o lepší cenu při prodloužení","
competitorOffer":null}
