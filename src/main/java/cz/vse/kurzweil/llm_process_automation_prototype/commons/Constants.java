package cz.vse.kurzweil.llm_process_automation_prototype.commons;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {

    public static final String EXTRACTION_TYPE = "extraction";
    public static final String CLASSIFICATION_TYPE = "classification";
    public static final String SUFFIX_JSON = ".json";
    public static final String RESULTS_DIR_NAME = "results";

    public static final String DIFF_MISSING_FIELD = "missing_field";
    public static final String DIFF_UNEXPECTED_FIELD = "unexpected_field";
    public static final String DIFF_TYPE_MISMATCH = "type_mismatch";
    public static final String DIFF_NULL_MISMATCH = "null_mismatch";
    public static final String DIFF_VALUE_MISMATCH = "value_mismatch";

    public static final List<String> ARRAY_SORT_KEYS = List.of(
            "serviceId",
            "productId",
            "discountId",
            "targetServiceId",
            "label",
            "number",
            "donorOperator"
    );
}
