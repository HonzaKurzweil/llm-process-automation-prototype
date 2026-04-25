--- EXAMPLE 1 ---
INPUT:
Zákazník Jan Novák chce novou SIM kartu s tarifem Mobil Start 5G. Bude portovat číslo od O2.
OUTPUT:
RT_NEW_MOBILE_ORDER

--- EXAMPLE 2 ---
INPUT:
Rodina Procházkových — tatínek, maminka a dvě děti. Zájem o rodinný tarif, 4 linky.
OUTPUT:
RT_FAMILY_MOBILE_ORDER

--- EXAMPLE 3 ---
INPUT:
Zákaznice chce fiber internet 1 Gbit/s a k tomu zapůjčit router Pro. Instalace na adrese Praha 2.
OUTPUT:
RT_FIXED_INTERNET_WITH_HARDWARE_ORDER

--- EXAMPLE 4 ---
INPUT:
Pan Kovář má zájem o balíček internet + TV Family, případně přidat sportovní balíček.
OUTPUT:
RT_INTERNET_TV_BUNDLE_ORDER

--- EXAMPLE 5 ---
INPUT:
Stávající zákazník chce zrušit smlouvu kvůli nabídce Vodafone. Snažíme se ho udržet slevou.
OUTPUT:
RT_RETENTION_DISCOUNT_REQUEST

--- EXAMPLE 6 ---
INPUT:
Zákazník Jan Procházka, stávající zákazník, žádá o retenční slevu na Optiku Domů 300. Důvod: konkurence nabízí stejnou rychlost o 200 Kč levněji. Zákazník zároveň žádá o vystavení duplikátu staré faktury.
OUTPUT:
UNCLASSIFIABLE

--- EXAMPLE 7 ---
INPUT:
Požadavek 1: Petr Urban, stávající zákazník, objednávka pevného internetu DSL s Wi-Fi routerem Pro, instalace Brno. Požadavek 2: tentýž zákazník žádá o retenční slevu na stávající mobilní tarif.
OUTPUT:
UNCLASSIFIABLE
