/**
 * Клиентская часть.
 */
module client {
    requires shared;

    exports clientCommands;
    exports clientMainFiles;
    exports network;
    exports clientExceptions;
    exports utils;
}