package com.myth.enums;

public enum City {
    HO_CHI_MINH("Ho Chi Minh"),
    HA_NOI("Ha Noi"),
    NEW_YORK("New York"),
    LONDON("London"),
    TOKYO("Tokyo"),
    SEOUL("Seoul"),
    ;

    public final String value;
    City(String value) {
        this.value = value;
    }
}
