/**
 * Клиентская часть.
 */
module client {
    requires shared;
    requires javafx.graphics;
    requires javafx.controls;

    exports clientCommands;
    exports clientMainFiles;
    exports network;
    exports clientExceptions;
    exports utils;
}