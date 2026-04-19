--- EXAMPLE 1 (broker email — customer threatens to leave due to price) ---
INPUT:
Dobrý den,
předávám retenční požadavek pro stávajícího zákazníka Romana Bílka.
Telefon: 777 654 321
Aktuální služba: Mobil Neomezeně 5G
Zákazník chce službu zachovat, pokud dostane retenční slevu 15 %.
Důvod odchodu: vysoká cena
Konkurenční nabídka: T-Mobile neomezený tarif za 599 Kč měsíčně

OUTPUT:
{"customerStatus":"existing","customerName":"Roman Bílek","contactPhone":"+420 777 654 321","retentionCase":true,"currentServices":[{"serviceId":"svc_mobile_unlimited_5g"}],"targetServiceId":"svc_mobile_unlimited_5g","requestedDiscounts":[{"discountId":"disc_retention_15_existing"}],"churnReason":"vysoká cena","competitorOffer":"T-Mobile neomezený tarif za 599 Kč měsíčně"}

--- EXAMPLE 2 (CRM ticket — internet customer negotiating retention) ---
INPUT:
Retence
Zákazník: Petra Novotná
Tel.: 605 909 808
Aktuálně má Optiku Domů 300.
Hrozí odchod ke konkurenci, pokud nebude zvýhodněn přechod na Optiku Domů 1000.
Požaduje retenční slevu 15 %.
Konkurence nabízí optiku 1 Gbps za 499 Kč.

OUTPUT:
{"customerStatus":"existing","customerName":"Petra Novotná","contactPhone":"+420 605 909 808","retentionCase":true,"currentServices":[{"serviceId":"svc_internet_fiber_300"}],"targetServiceId":"svc_internet_fiber_1000","requestedDiscounts":[{"discountId":"disc_retention_15_existing"}],"churnReason":null,"competitorOffer":"konkurence nabízí optiku 1 Gbps za 499 Kč"}

--- EXAMPLE 3 (call transcript — better price request without true retention case) ---
INPUT:
Operátor: Vidím, že voláte kvůli smlouvě. Co se děje?
Zákazník: Chtěl bych lepší cenu při prodloužení tarifu.
Operátor: Jak se jmenujete?
Zákazník: Zdeněk Rybář. Volat mi můžete na 603998441.
Operátor: Jakou službu nyní máte?
Zákazník: Mobil Neomezeně 5G.
Operátor: Požadujete retenční slevu 15 %?
Zákazník: Ano, pokud to jde.

OUTPUT:
{"customerStatus":"existing","customerName":"Zdeněk Rybář","contactPhone":"+420 603 998 441","retentionCase":false,"currentServices":[{"serviceId":"svc_mobile_unlimited_5g"}],"targetServiceId":"svc_mobile_unlimited_5g","requestedDiscounts":[{"discountId":"disc_retention_15_existing"}],"churnReason":"žádost o lepší cenu při prodloužení","competitorOffer":null}
