--- EXAMPLE 1 ---
INPUT:
Zákazník Jan Novák chce novou SIM kartu s tarifem Mobil Start 5G. Bude portovat číslo od O2.
OUTPUT:
{"request_type":"rt_new_mobile_order"}

--- EXAMPLE 2 ---
INPUT:
Rodina Procházkových — tatínek, maminka a dvě děti. Zájem o rodinný tarif, 4 linky.
OUTPUT:
{"request_type":"rt_family_mobile_order"}

--- EXAMPLE 3 ---
INPUT:
Zákaznice chce fiber internet 1 Gbit/s a k tomu zapůjčit router Pro. Instalace na adrese Praha 2.
OUTPUT:
{"request_type":"rt_fixed_internet_with_hardware_order"}

--- EXAMPLE 4 ---
INPUT:
Pan Kovář má zájem o balíček internet + TV Family, případně přidat sportovní balíček.
OUTPUT:
{"request_type":"rt_internet_tv_bundle_order"}

--- EXAMPLE 5 ---
INPUT:
Stávající zákazník chce zrušit smlouvu kvůli nabídce Vodafone. Snažíme se ho udržet slevou.
OUTPUT:
{"request_type":"rt_retention_discount_request"}
