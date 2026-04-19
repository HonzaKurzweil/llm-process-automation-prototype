--- EXAMPLE 1 (broker email — existing customer, no porting) ---
INPUT:
Dobrý den,
posílám objednávku nové mobilní služby pro stávající zákaznici Janu Malou.
Jméno zákaznice: Jana Malá
Telefon: 777 888 999
E-mail: jana.mala@email.cz
Požadovaný tarif: Mobil Start 5G
Závazek: 24 měsíců
Portace: ne

OUTPUT:
{"customerStatus":"existing","customerName":"Jana Malá","contactPhone":"+420 777 888 999","contactEmail":"jana.mala@email.cz","requestedServices":[{"serviceId":"svc_mobile_start_5g","quantity":1}],"contractTermMonths":24,"portingRequested":false,"portedNumbers":null,"requestedDiscounts":null}

--- EXAMPLE 2 (CRM ticket — new customer, porting requested, number not yet provided) ---
INPUT:
Nový zákazník – Petr Veselý
Tel.: 603 112 445
Tarif: Mobil Neomezeně 5G
Závazek: 24 měs.
Portace: ANO – číslo zatím neposkytnuto
Sleva: Přenos čísla 200 Kč / 6 měs.

OUTPUT:
{"customerStatus":"new","customerName":"Petr Veselý","contactPhone":"+420 603 112 445","contactEmail":null,"requestedServices":[{"serviceId":"svc_mobile_unlimited_5g","quantity":1}],"contractTermMonths":24,"portingRequested":true,"portedNumbers":null,"requestedDiscounts":[{"discountId":"disc_porting_mobile_200"}]}

--- EXAMPLE 3 (call transcript — complete porting) ---
INPUT:
Operátor: Dobrý den, co byste si přál?
Zákazník: Chtěl bych tarif Mobil Start 5G.
Operátor: Bude to pro nového zákazníka?
Zákazník: Ano, na moje jméno Tomáš Král. Volejte mi na 605334221.
Operátor: Portace čísla?
Zákazník: Ano, chci převést číslo 731 222 111 od Vodafone.
Operátor: Na jak dlouho závazek?
Zákazník: Na 24 měsíců. A když bude, tak i sleva za přenos.

OUTPUT:
{"customerStatus":"new","customerName":"Tomáš Král","contactPhone":"+420 605 334 221","contactEmail":null,"requestedServices":[{"serviceId":"svc_mobile_start_5g","quantity":1}],"contractTermMonths":24,"portingRequested":true,"portedNumbers":[{"number":"+420 731 222 111","donorOperator":"Vodafone"}],"requestedDiscounts":[{"discountId":"disc_porting_mobile_200"}]}
