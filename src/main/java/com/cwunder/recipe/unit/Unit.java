package com.cwunder.recipe.unit;

import com.cwunder.recipe._shared.AppEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Unit")
public class Unit extends AppEntity {
    @Size(min = 1, max = 255)
    private String unit;

    public Unit() {
        initialize();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
