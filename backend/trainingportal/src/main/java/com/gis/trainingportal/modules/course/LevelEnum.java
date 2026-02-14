package com.gis.trainingportal.modules.course;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/* Enum para representar los niveles de los cursos. */
public enum LevelEnum {

    BASICO("Básico"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado");

    private final String displayName;

    LevelEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static LevelEnum fromString(String text) {
        if (text == null)
            return null;

        return Arrays.stream(LevelEnum.values())
                .filter(level -> level.displayName.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nivel Inválido: " + text +
                        ". Valores Permitidos: " + Arrays.toString(LevelEnum.values())));
    }
}