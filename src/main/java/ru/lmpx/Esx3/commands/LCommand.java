package ru.lmpx.Esx3.commands;

import ru.lmpx.Esx3.Functions;

public interface LCommand {

    String getPermission();

    String name();

    default String getPermissionWithRoot() {
        return Functions.permRoot() + getPermission();
    }

}
