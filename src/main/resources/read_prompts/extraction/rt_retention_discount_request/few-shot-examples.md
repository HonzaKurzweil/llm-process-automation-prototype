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
{"customer_status":"existing","customer_name":"Roman Bílek","contact_phone":"+420 777 654 321","retention_case":"zákazník chce zachovat Mobil Neomezeně 5G při poskytnutí retenční slevy","current_services":[{"service_id":"svc_mobile_unlimited_5g","quantity":1}],"target_service_id":"svc_mobile_unlimited_5g","requested_discounts":[{"discount_id":"disc_retention_15_existing"}],"churn_reason":"vysoká cena","competitor_offer":"T-Mobile neomezený tarif za 599 Kč měsíčně","notes":null}

--- EXAMPLE 2 (CRM ticket — internet customer negotiating upgrade retention) ---
INPUT:
Retence
Zákazník: Petra Novotná
Tel.: 605 909 808
Aktuálně má fiber 300.
Hrozí odchod ke konkurenci, pokud nebude zvýhodněn přechod na fiber 1000.
Požaduje retenční slevu 15 %.
Konkurence nabízí optiku 1 Gbps za 499 Kč.
Pozn.: zavolat zpět zítra dopoledne.

OUTPUT:
{"customer_status":"existing","customer_name":"Petra Novotná","contact_phone":"+420 605 909 808","retention_case":"zákaznice zvažuje odchod bez zvýhodněného přechodu na fiber 1000","current_services":[{"service_id":"svc_internet_fiber_300","quantity":1}],"target_service_id":"svc_internet_fiber_1000","requested_discounts":[{"discount_id":"disc_retention_15_existing"}],"churn_reason":null,"competitor_offer":"konkurence nabízí optiku 1 Gbps za 499 Kč","notes":"zavolat zpět zítra dopoledne"}

--- EXAMPLE 3 (call transcript — family tariff at risk) ---
INPUT:
Operátor: Vidím, že voláte kvůli smlouvě. Co se děje?
Zákazník: Uvažuji, že odejdu s rodinným tarifem jinam.
Operátor: Jak se jmenujete?
Zákazník: Lenka Havlíková. Volat mi můžete na 604 111 909.
Operátor: Jakou službu nyní máte?
Zákazník: Family Plus. Konkurence mi dala lepší cenu.
Operátor: Co by vám pomohlo zůstat?
Zákazník: Kdybych dostala těch 15 procent dolů, zůstanu.
Operátor: Dobře, předám to retenci.

OUTPUT:
{"customer_status":"existing","customer_name":"Lenka Havlíková","contact_phone":"+420 604 111 909","retention_case":"zákaznice zvažuje odchod s rodinným tarifem kvůli lepší ceně od konkurence","current_services":[{"service_id":"svc_mobile_family_plus","quantity":1}],"target_service_id":"svc_mobile_family_plus","requested_discounts":[{"discount_id":"disc_retention_15_existing"}],"churn_reason":null,"competitor_offer":"konkurence nabídla lepší cenu","notes":null}
