/**
 * Серверная часть.
 */
module server {
    requires shared;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires java.sql;

    exports serverMainFiles;
    exports serverCommands;
    exports serverTools;
    exports serverExceptions;
}