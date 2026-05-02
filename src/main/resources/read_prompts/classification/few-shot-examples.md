Ukázky klasifikačního rozhodování:

Příklad 1 – jeden podporovaný požadavek, ostatní části jsou šum nebo kontext:
Vstup: „Dobrý den, chci si pořídit nový mobilní tarif pro sebe. Byl jsem dříve u jiného
operátora, ale chci přejít k vám. Jinak jste na výstavě v březnu měli hezký stánek."
Výsledek: rt_new_mobile_order
Vysvětlení: Zákaznickým požadavkem je nová mobilní objednávka. Zmínka o výstavě je neprocesní komentář.

Příklad 2 – dva samostatné podporované požadavky v jednom textu:
Vstup: „Pan Kopecký chce objednat mobilní tarif pro sebe a zároveň pevný internet
s routerem na svou novou adresu."
Výsledek: unclassifiable
Vysvětlení: Text popisuje dva samostatné podporované požadavky. Nelze vybrat jen jeden z nich.

Příklad 3 – jeden podporovaný požadavek spolu s požadavkem mimo rozsah:
Vstup: „Zákaznice chce přidat několik mobilních linek pro svou rodinu a zároveň má na účtu
neuhrazený dluh, který je nutné vyřešit."
Výsledek: unclassifiable
Vysvětlení: Neuhrazený dluh není podporovaný request type. Text nelze klasifikovat jako samotnou mobilní objednávku.

Příklad 4 – text bez dostatečného kontextu pro jednoznačnou klasifikaci:
Vstup: „Zákazník se dotazuje na možnosti tarifu."
Výsledek: unclassifiable
Vysvětlení: Z textu nelze bezpečně určit konkrétní request type. Neurčuj typ pouze z jednoho slova bez kontextu.

Příklad 5 – text neobsahuje žádný podporovaný požadavek:
Vstup: „Zákazník se ptá, zda může reklamovat poškozený telefon zakoupený v kamenné prodejně a jak
dlouho trvá vyřízení reklamace."
Výsledek: unclassifiable
Vysvětlení: Text neobsahuje žádný z podporovaných request typů (objednávka tarifu, internetu, televize,
rodinného tarifu ani retenční sleva). Reklamace zboží není v rozsahu systému.

Příklad 6 – formát vstupu (přepis hovoru) neovlivňuje klasifikaci:
Vstup: „Operátor: Dobrý den, jak vám mohu pomoci? Zákazník: Jsem u vás zákazník přes osm let
a přemýšlím o odchodu. Mohl bych dostat výhodnější cenu za svůj tarif?"
Výsledek: rt_retention_discount_request
Vysvětlení: Přepis hovoru se klasifikuje stejně jako jiné formáty. Zákazník žádá o retenční nabídku.
