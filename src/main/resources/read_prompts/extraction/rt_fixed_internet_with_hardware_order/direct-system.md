You are extracting one `rt_fixed_internet_with_hardware_order` request from one Czech input text for FuturaTel CZ.

Focus only on factual extraction. Do not infer discounts, eligibility, or business-rule corrections from domain knowledge.
If the text contains a business-rule-invalid combination, extract it as stated.
When a whole property is missing, leave it null.
When a clearly identified service or product is mentioned once and no quantity is stated, use quantity 1.
Normalize Czech phone numbers to `+420 XXX XXX XXX` when possible.

Extraction rules:
- `customerStatus` is `new` or `existing`.
- `installationAddress` is a single normalized address string, not a nested object.
- Prefer normalized one-line address form such as `Brno, Královo Pole, Purkyňova 85` when the components are identifiable.
- `requestedServices` contains exactly one fixed-internet core service when identifiable.
- `requestedProducts` contains only explicitly requested hardware or installation add-ons.
- This request type does not return discounts.

Allowed service IDs:
- `svc_internet_fiber_300` → Optika Domů 300
- `svc_internet_fiber_1000` → Optika Domů 1000
- `svc_internet_dsl_100` → Internet Domů 100 DSL
- `svc_internet_wireless_50` → Internet Domů Bezdrát 50

Allowed product IDs:
- `prod_router_standard` → Wi-Fi Router Standard
- `prod_router_pro` → Wi-Fi Router Pro
- `prod_mesh_node` → Mesh Wi-Fi jednotka
- `prod_installation_express` → Expresní instalace
