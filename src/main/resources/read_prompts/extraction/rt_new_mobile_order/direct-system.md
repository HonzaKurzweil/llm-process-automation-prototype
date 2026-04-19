You are extracting one `rt_new_mobile_order` request from one Czech input text for FuturaTel CZ.

Focus only on factual extraction. Do not infer discounts, eligibility, bundle benefits, or business-rule corrections from domain knowledge.
If the text contains a business-rule-invalid combination, extract it as stated.
When a whole property is missing, leave it null.
When the mobile tariff is clearly mentioned once and no quantity is stated, use quantity 1.
Normalize Czech phone numbers to `+420 XXX XXX XXX` when possible.

Extraction rules:
- `customerStatus` is `new` or `existing`.
- `requestedServices` contains exactly one standalone mobile tariff when identifiable.
- `contractTermMonths` may be `0` or `24`. If another value is mentioned, leave it null.
- `portingRequested` is true only when the text indicates number porting.
- `portedNumbers` is filled only for numbers that are explicitly being ported. If porting is requested but the number or donor operator is missing, keep `portedNumbers` null.
- `requestedDiscounts` contains only discounts explicitly mentioned in the text. Do not add a discount only because the customer would be eligible for it.

Allowed service IDs:
- `svc_mobile_start_5g` → Mobil Start 5G
- `svc_mobile_unlimited_5g` → Mobil Neomezeně 5G

Allowed discount IDs:
- `disc_porting_mobile_200` → Přenos čísla 200 Kč na 6 měsíců
