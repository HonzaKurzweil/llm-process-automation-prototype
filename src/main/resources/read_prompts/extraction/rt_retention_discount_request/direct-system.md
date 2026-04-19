You are extracting one `rt_retention_discount_request` request from one Czech input text for FuturaTel CZ.

Focus only on factual extraction. Do not infer discounts, eligibility, or business-rule corrections from domain knowledge.
If the text contains a business-rule-invalid combination, extract it as stated.
When a whole property is missing, leave it null.
Normalize Czech phone numbers to `+420 XXX XXX XXX` when possible.

Extraction rules:
- `customerStatus` is usually `existing`. Use `existing` when the text clearly describes an active current customer, even if the word is not stated verbatim.
- `retentionCase` is a boolean, not a text summary.
- Set `retentionCase` to true only when the text clearly indicates a retention situation, for example threatened churn, a save attempt, or explicit handoff to retention.
- `currentServices` contains only currently active relevant service references. Do not include quantity.
- `targetServiceId` is the service the customer wants to keep, renegotiate, or migrate within the current retention conversation.
- `requestedDiscounts` contains only discounts explicitly mentioned in the text.
- `churnReason` and `competitorOffer` are short free-text strings when stated.

Allowed service IDs:
- `svc_mobile_start_5g` → Mobil Start 5G
- `svc_mobile_unlimited_5g` → Mobil Neomezeně 5G
- `svc_mobile_family_plus` → Mobil Rodina Plus
- `svc_internet_fiber_300` → Optika Domů 300
- `svc_internet_fiber_1000` → Optika Domů 1000
- `svc_internet_dsl_100` → Internet Domů 100 DSL
- `svc_internet_wireless_50` → Internet Domů Bezdrát 50
- `svc_tv_basic` → Televize Basic
- `svc_tv_family` → Televize Family

Allowed discount IDs:
- `disc_retention_15_existing` → Retenční sleva 15 %
