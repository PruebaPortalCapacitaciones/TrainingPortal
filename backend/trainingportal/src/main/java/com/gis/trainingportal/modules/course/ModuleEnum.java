package com.gis.trainingportal.modules.course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum ModuleEnum {

    FULLSTACK("Fullstack"),
    APIS("APIs e Integraciones"),
    CLOUD("Cloud"),
    DATA_ENGINEER("Data Engineer");

    private final String displayName;

    ModuleEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static ModuleEnum fromString(String text) {
        if (text == null) return null;
        
        return Arrays.stream(ModuleEnum.values())
                .filter(module -> module.displayName.equals(text))
                .findFirst()
                .orElseThrow(() -> 
                    new IllegalArgumentException("Módulo no válido: " + text + 
                        ". Valores permitidos: " + Arrays.toString(ModuleEnum.values())));
    }
}