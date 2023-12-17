package com.system.tasks.enums;

public enum TaskStatus {

    WAITING("В ожидании"),
    IN_PROCESSED("В процессе"),
    COMPLETED("Завершена");

    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
