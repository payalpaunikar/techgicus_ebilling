package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration;

public enum State {
    ANDHRA_PRADESH("37", "Andhra Pradesh"),
    ARUNACHAL_PRADESH("12", "Arunachal Pradesh"),
    ASSAM("18", "Assam"),
    BIHAR("10", "Bihar"),
    CHHATTISGARH("22", "Chhattisgarh"),
    GOA("30", "Goa"),
    GUJARAT("24", "Gujarat"),
    HARYANA("06", "Haryana"),
    HIMACHAL_PRADESH("02", "Himachal Pradesh"),
    JHARKHAND("20", "Jharkhand"),
    KARNATAKA("29", "Karnataka"),
    KERALA("32", "Kerala"),
    MADHYA_PRADESH("23", "Madhya Pradesh"),
    MAHARASHTRA("27", "Maharashtra"),
    MANIPUR("14", "Manipur"),
    MEGHALAYA("17", "Meghalaya"),
    MIZORAM("15", "Mizoram"),
    NAGALAND("13", "Nagaland"),
    ODISHA("21", "Odisha"),
    PUNJAB("03", "Punjab"),
    RAJASTHAN("08", "Rajasthan"),
    SIKKIM("11", "Sikkim"),
    TAMIL_NADU("33", "Tamil Nadu"),
    TELANGANA("36", "Telangana"),
    TRIPURA("16", "Tripura"),
    UTTAR_PRADESH("09", "Uttar Pradesh"),
    UTTARAKHAND("05", "Uttarakhand"),
    WEST_BENGAL("19", "West Bengal"),
    OTHER("99", "Other / Union Territory");

    private final String code;
    private final String displayName;

    State(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Optional: helper to get enum from code
    public static State fromCode(String code) {
        for (State s : values()) {
            if (s.code.equals(code)) {
                return s;
            }
        }return OTHER;
    }
}
