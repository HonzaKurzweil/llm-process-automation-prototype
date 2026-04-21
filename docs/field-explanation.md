# Vysvětlení polí v doménových YAML souborech

Tento dokument vysvětluje, proč jsou jednotlivá pole v referenčních katalozích a k čemu se později hodí při generování
gold dat a variant vstupů.

## 1. `services.yaml`

### Kořenová metadata

- `operator` — jméno fiktivního operátora. Hodí se pro kontext a případné generování textů v jednotné doméně.
- `catalog_version` — verze katalogu. Užitečné, když budeš data později upravovat a chceš vědět, z jaké verze vznikl
  dataset.
- `currency` — měna cen. Pomáhá držet ceny a slevy ve stejném formátu.
- `services` — seznam hlavních telekomunikačních služeb, které mohou tvořit jádro poptávky.

### Pole jedné služby

- `service_id` — stabilní interní identifikátor. Potřebuješ ho pro propojování pravidel, slev a gold dat bez závislosti
  na textovém názvu.
- `category` — široká obchodní kategorie (`mobile`, `fixed_internet`, `tv`). Hodí se pro bundle logiku, klasifikaci a
  pravidla typu „alespoň dvě různé kategorie“.
- `subcategory` — jemnější typ služby, například `fiber` nebo `family_tariff`. Užitečné pro realističtější generování
  případů a jemnější pravidla.
- `name` — lidsky čitelný název nabídky. Bude se používat při generování e-mailů, ticketů a přepisů hovorů.
- `description` — krátký obchodní popis. Z něj může generátor odvozovat realistické formulace, které se pak objeví ve
  vstupních textech.
- `base_price_czk` — základní cena služby. Hodí se pro realistické poptávky, slevy a případné porovnávání cen v textu.
- `billing_period` — jak často se služba účtuje. V tvé doméně je to hlavně `monthly`, ale pole se hodí pro budoucí
  rozlišení měsíčních a jednorázových položek.
- `contract_term_options_months` — jaké délky závazku dávají obchodně smysl. Pomáhá generovat případy, kde klient chce
  bez závazku nebo na 24 měsíců.
- `availability_model` — zjednodušený model dostupnosti služby. Hodí se pro generování realistických případů podle
  lokality.
    - `nationwide` — služba je pro dataset obecně dostupná všude.
    - `locality_group` — služba je dostupná jen ve vybraných lokalitách definovaných bokem.
    - `network_check_required` — dostupnost nelze odhadnout jen podle města a je potřeba technické ověření.
- `locality_group_ids` — odkazy na skupiny lokalit. Užitečné jen tehdy, když `availability_model` pracuje s lokalitami.
- `requires_installation_address` — říká, zda je pro smysluplnou poptávku potřeba instalační adresa. Hodí se pro
  generování úplných i neúplných vstupů.
- `supports_number_porting_in` — říká, zda u služby dává smysl přenos čísla od jiného operátora. Užitečné pro
  realistické mobilní případy.
- `bundle_eligible` — zda se služba může započítat do balíčku. Hodí se pro generování bundle scénářů a slev.
- `eligible_customer_statuses` — pro jaký typ zákazníka je služba relevantní (`new`, `existing`). Užitečné, když budeš
  generovat retenční nebo změnové případy.
- `mandatory_input_fields` — jaké informace mají být v gold datech nebo ve vstupu přítomné, aby byla poptávka kompletní.
  Teď to nepoužiješ jako validaci modelu, ale jako vodítko pro generování.
- `tags` — volné pomocné štítky. Hodí se pro sampling dat, vyvažování datasetu a později i pro filtraci scénářů.

## 2. `products.yaml`

### Kořenová metadata

- `operator` — stejný význam jako u služeb.
- `catalog_version` — verze katalogu.
- `currency` — měna.
- `products` — seznam doplňkových produktů a add-on položek.

### Pole jednoho produktu

- `product_id` — stabilní interní identifikátor pro pravidla a propojení s gold daty.
- `category` — základní typ produktu (`hardware`, `addon`). Pomáhá odlišit zařízení od doplňkových služeb.
- `subcategory` — jemnější typ produktu, například `wifi_router`, `installation`, `tv_addon`.
- `name` — název produktu použitelný ve vstupních textech.
- `description` — krátké obchodní vysvětlení produktu. Hodí se pro generátor přirozeného jazyka.
- `base_price_czk` — základní cena produktu nebo add-onu.
- `billing_model` — jak se produkt účtuje.
    - `monthly_rental` — pravidelný měsíční pronájem.
    - `monthly` — pravidelná měsíční add-on služba.
    - `one_time` — jednorázový poplatek.
- `parent_service_categories` — ke kterým kategoriím hlavních služeb produkt typicky patří. To pomáhá při generování
  realistických kombinací.
- `parent_service_ids` — užší vazba na konkrétní službu. Například sportovní balíček jen k TV Family.
- `max_quantity_per_order` — jednoduchý limit pro generování realistických objednávek.
- `bundle_eligible` — zda se produkt sám započítává do balíčku. Tady je většinou `false`, aby se bundle sleva počítala
  jen z hlavních služeb.
- `tags` — pomocné štítky pro sampling a popis produktu.

## 3. `discounts.yaml`

### Kořenová metadata

- `operator` — kontext katalogu.
- `catalog_version` — verze katalogu.
- `currency` — měna.
- `discounts` — seznam slev a akcí.

### Pole jedné slevy

- `discount_id` — stabilní interní identifikátor slevy.
- `name` — obchodní název slevy.
- `description` — lidsky čitelný popis. Hodí se pro generování textů i pozdější vysvětlování pravidel.
- `discount_type` — jak se sleva vyjadřuje.
    - `percentage` — procentní sleva.
    - `fixed_amount_czk` — pevná částka v korunách.
- `value` — velikost slevy. Význam závisí na `discount_type`.
- `applies_to_scope` — říká, na jaké úrovni se sleva vztahuje.
    - `category` — na celou kategorii služeb, například mobil nebo internet.
    - `product` — na konkrétní produkt.
    - případně by šlo rozšířit i na `service` nebo `order`.
      To je přesně pole, které nahrazuje nejasné obecné `scope`.
- `applies_to_categories` — seznam kategorií, na něž se sleva může vztáhnout, pokud je scope kategorie.
- `applies_to_ids` — konkrétní položky, na něž se sleva vztahuje, pokud je scope konkrétní produkt nebo služba.
- `billing_scope` — v jakém typu účtování se sleva projeví.
    - `monthly` — sleva na měsíční cenu.
    - `one_time` — sleva na jednorázový poplatek.
- `duration_months` — jak dlouho sleva trvá. U jednorázové slevy je to spíš technická hodnota pro jednoduchost dat.
- `eligible_customer_statuses` — pro jaký stav zákazníka je sleva určená.
- `requires_bundle` — zda se sleva aktivuje jen v balíčku více služeb.
- `min_distinct_service_categories` — kolik různých kategorií služeb je minimálně potřeba. Hodí se pro bundle akce.
- `stackable` — zda se sleva může kombinovat s jinou slevou.
- `exclusivity_group` — skupina vzájemně se vylučujících akcí. Užitečné pro simulaci pravidla „nelze kombinovat dvě
  hlavní promo akce“.
- `requires_porting` — zda je sleva vázaná na přenos čísla.
- `requires_retention_case` — zda je sleva vázaná na retenční situaci. Hodí se pro odlišení běžných akčních a retenčních
  leadů.

## 4. `locality_groups.yaml`

### Kořenová metadata

- `operator` — kontext katalogu.
- `catalog_version` — verze katalogu.
- `locality_groups` — seznam zjednodušených skupin lokalit pro simulaci dostupnosti.

### Pole jedné skupiny lokalit

- `locality_group_id` — interní identifikátor skupiny lokalit. Odkazují na něj služby.
- `name` — lidsky čitelný název skupiny.
- `description` — vysvětlení, k čemu je skupina použita.
- `localities` — seznam měst nebo obcí. Slouží jako referenční slovník pro realistické adresy v gold datech.

## 5. `rules.yaml`

Tento soubor je záměrně rozdělen do více sekcí, protože ne všechna pravidla mají stejný tvar.

### `dependency_rules`

Pravidla typu „A vyžaduje B“.

- `rule_id` — stabilní identifikátor pravidla.
- `source_kind` — zda pravidlo vychází ze `service` nebo `product`.
- `source_id` — identifikátor položky, pro kterou pravidlo platí.
- `requirement_type` — typ požadavku.
    - `requires_category` — musí existovat nějaká položka z dané kategorie.
    - `requires_id` — musí existovat konkrétní položka.
    - `requires_one_of_ids` — musí existovat alespoň jedna z uvedených položek.
- `required_categories` — seznam kategorií, pokud pravidlo pracuje s kategorií.
- `required_ids` — seznam konkrétních ID, pokud pravidlo pracuje s konkrétními položkami.
- `reason` — lidsky čitelný důvod pravidla. Hodí se pro debug a později i pro human review.

### `incompatibility_rules`

Pravidla typu „A a B nemají být spolu“.

- `rule_id` — identifikátor pravidla.
- `left_kind`, `right_kind` — zda se pravidlo týká služby nebo produktu na levé a pravé straně.
- `left_id`, `right_id` — konkrétní dvojice, která je nekompatibilní.
- `reason` — vysvětlení obchodní logiky.

### `eligibility_rules`

Pravidla typu „nárok vzniká jen za určitých podmínek“.

- `rule_id` — identifikátor pravidla.
- `target_kind` — jestli pravidlo cílí na `service` nebo `discount`.
- `target_id` — identifikátor cílové položky.
- `condition_type` — typ podmínky.
    - `min_category_quantity` — minimální počet položek v kategorii.
    - `min_distinct_service_categories` — minimální počet různých kategorií služeb.
    - `requires_flag` — vyžaduje příznak, například `porting_requested`.
    - `requires_customer_status` — vyžaduje konkrétní stav zákazníka.
    - `composite` — složená podmínka s více prvky.
- `condition` — vlastní parametry podmínky. Je to záměrně vnořené pole, aby šlo přidávat další typy podmínek bez rozbití
  schématu.
- `reason` — lidsky čitelný důvod pravidla.

### `quantity_rules`

Jednoduché limity počtu položek.

- `rule_id` — identifikátor pravidla.
- `target_kind` — zda se limit vztahuje na službu nebo produkt.
- `target_scope` — jak se má `target_id` interpretovat.
    - `category` — `target_id` je kategorie.
    - `id` — `target_id` je konkrétní položka.
- `target_id` — cílová kategorie nebo konkrétní položka.
- `max_quantity` — maximální počet kusů v jednom leadu.
- `reason` — vysvětlení limitu.

### `discount_exclusion_rules`

Pravidla pro nesčitatelné akce.

- `rule_id` — identifikátor pravidla.
- `exclusivity_group` — skupina slev, které se navzájem vylučují.
- `reason` — obchodní důvod, proč se akce nesmí kombinovat.
