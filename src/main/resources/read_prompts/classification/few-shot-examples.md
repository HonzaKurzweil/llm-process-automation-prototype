Ukázky klasifikačního rozhodování:

Příklad 1 – jeden podporovaný požadavek, ostatní části jsou šum nebo kontext:
Vstup: „Dobrý den, chci si pořídit nový mobilní tarif pro sebe. Byl jsem dříve u jiného
operátora, ale chci přejít k vám. Jinak jste na výstavě v březnu měli hezký stánek."
Výsledek: rt_new_mobile_order
Vysvětlení: Zákaznickým požadavkem je nová mobilní objednávka. Zmínka o výstavě je neprocesní komentář.

Příklad 2 – požadavek s chybějícími kontaktními údaji nebo neúplnými detaily:
Vstup: „Dobrý den, zákazník chce objednat internet s routerem a mesh jednotkou, ale zatím neposlal
všechny kontaktní údaje."
Výsledek: rt_fixed_internet_with_hardware_order
Vysvětlení: Typ požadavku je jasný. Chybějící kontaktní údaje se týkají zpracování, ne klasifikace.

Příklad 3 – CRM ticket s interními zkratkami a poznámkami:
Vstup: „CRM pozn.: Zákazník chce sam. mob. tarif Start 5G, zatím bez kontaktů. Interní: kontakt po
obědě."
Výsledek: rt_new_mobile_order
Vysvětlení: Zkratky „sam. mob. tarif" a interní poznámky jsou šum. Záměr zákazníka je pořízení
mobilního tarifu.

Příklad 4 – dva samostatné podporované požadavky v jednom textu:
Vstup: „Pan Kopecký chce objednat mobilní tarif pro sebe a zároveň pevný internet
s routerem na svou novou adresu."
Výsledek: unclassifiable
Vysvětlení: Text popisuje dva samostatné podporované požadavky. Nelze vybrat jen jeden z nich.

Příklad 5 – jeden podporovaný požadavek spolu s požadavkem mimo rozsah:
Vstup: „Zákaznice chce přidat několik mobilních linek pro svou rodinu a zároveň má na účtu
neuhrazený dluh, který je nutné vyřešit."
Výsledek: unclassifiable
Vysvětlení: Neuhrazený dluh není podporovaný request type. Text nelze klasifikovat jako samotnou mobilní objednávku.

Příklad 6 – text bez dostatečného kontextu pro jednoznačnou klasifikaci:
Vstup: „Zákazník se dotazuje na možnosti tarifu."
Výsledek: unclassifiable
Vysvětlení: Z textu nelze bezpečně určit konkrétní request type. Neurčuj typ pouze z jednoho slova bez kontextu.

Příklad 7 – text neobsahuje žádný podporovaný požadavek:
Vstup: „Zákazník se ptá, zda může reklamovat poškozený telefon zakoupený v kamenné prodejně a jak
dlouho trvá vyřízení reklamace."
Výsledek: unclassifiable
Vysvětlení: Text neobsahuje žádný z podporovaných request typů (objednávka tarifu, internetu, televize,
rodinného tarifu ani retenční sleva). Reklamace zboží není v rozsahu systému.

Příklad 8 – formát vstupu (přepis hovoru) neovlivňuje klasifikaci:
Vstup: „Operátor: Dobrý den, jak vám mohu pomoci? Zákazník: Jsem u vás zákazník přes osm let
a přemýšlím o odchodu. Mohl bych dostat výhodnější cenu za svůj tarif?"
Výsledek: rt_retention_discount_request
Vysvětlení: Přepis hovoru se klasifikuje stejně jako jiné formáty. Zákazník žádá o retenční nabídku.
