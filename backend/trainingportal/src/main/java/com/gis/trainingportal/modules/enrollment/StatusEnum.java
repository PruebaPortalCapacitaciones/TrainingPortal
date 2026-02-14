package com.gis.trainingportal.modules.enrollment;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/* Enum para representar los estados de una inscripción. */
public enum StatusEnum {

    INSCRITO("Inscrito"),
    INICIADO("Iniciado"),
    EN_PROGRESO("En progreso"),
    COMPLETADO("Completado");

    private final String displayName;

    StatusEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static StatusEnum fromString(String text) {
        if (text == null)
            return null;

        return Arrays.stream(StatusEnum.values())
                .filter(status -> status.displayName.equalsIgnoreCase(text)
                        || status.name().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado Inválido: " + text));
    }

}
