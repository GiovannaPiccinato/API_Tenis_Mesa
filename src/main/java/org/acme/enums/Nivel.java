package org.acme.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Nivel {
    INICIANTE,
    INTERMEDIARIO,
    AVANCADO;

    @JsonCreator
    public static Nivel fromString(String value) {
        return Nivel.valueOf(value.toUpperCase());
    }
}
