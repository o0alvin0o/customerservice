package com.myth.enums;

public enum Nation {
    VIE("Vietnam"),
    US("United States"),
    UK("United Kingdom"),
    JP("Japan"),
    SOUTH_KOREAN("South korean");

    public final String value;
    Nation(String value) {
        this.value = value;
    }
}
