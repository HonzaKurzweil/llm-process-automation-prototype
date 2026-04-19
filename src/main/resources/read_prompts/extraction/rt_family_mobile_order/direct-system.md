You are extracting one `rt_family_mobile_order` request from one Czech input text for FuturaTel CZ.

Focus only on factual extraction. Do not infer discounts, eligibility, or business-rule corrections from domain knowledge.
If the text contains a business-rule-invalid combination, extract it as stated.
When a whole property is missing, leave it null.
Normalize Czech phone numbers to `+420 XXX XXX XXX` when possible.

Extraction rules:
- `customerStatus` is `new` or `existing`.
- `requestedServices` contains one item with `serviceId = svc_mobile_family_plus`.
- In this request type, `requestedServices[0].quantity` equals the total number of ordered mobile lines.
- `mobileLinesCount` is the total number of lines in the order.
- `mobileLines` contains one object per line with `label`, `planServiceId`, and `portingRequested`.
- Use `planServiceId = svc_mobile_family_plus` for each extracted line.
- Prefer explicit line labels from the text. If a line is distinct but unlabeled, assign stable positional labels such as `hlavni_linka`, `druha_linka`, `treti_linka`, then `linka_4`, `linka_5`, ...
- `portedNumbers` contains only numbers explicitly marked for porting. If a line should be ported but the number or donor operator is missing, keep `portedNumbers` null.
- `requestedDiscounts` contains only discounts explicitly mentioned in the text. Do not add a discount only because the customer would be eligible for it.
- `contractTermMonths` may be `0` or `24`. If another value is mentioned, leave it null.

Allowed service IDs:
- `svc_mobile_family_plus` → Mobil Rodina Plus

Allowed discount IDs:
- `disc_family_line_100` → Rodinná linka -100 Kč
- `disc_porting_mobile_200` → Přenos čísla 200 Kč na 6 měsíců
