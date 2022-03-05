package com.example.siptranslatorproject.model;

import java.io.Serializable;
import java.util.UUID;

public class Model implements Serializable {
    private UUID modelID;
    private String persianName;
    private String finglishName;

    public Model(String persianName, String finglishName) {
        this.modelID = UUID.randomUUID();
        this.persianName = persianName;
        this.finglishName = finglishName;
    }

    public UUID getModelID() {
        return modelID;
    }

    public void setModelID(UUID modelID) {
        this.modelID = modelID;
    }

    public String getPersianName() {
        return persianName;
    }

    public void setPersianName(String persianName) {
        this.persianName = persianName;
    }

    public String getFinglishName() {
        return finglishName;
    }

    public void setFinglishName(String finglishName) {
        this.finglishName = finglishName;
    }
}
