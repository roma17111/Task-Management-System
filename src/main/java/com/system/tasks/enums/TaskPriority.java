package com.system.tasks.enums;

import lombok.Data;

public enum TaskPriority {

    TALL("Высокий"),
    AVERAGE("Средний"),
    LOW("Низкий");

    private final String name;

    TaskPriority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
