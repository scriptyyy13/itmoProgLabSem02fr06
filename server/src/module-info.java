/**
 * Серверная часть.
 */
module server {
    requires shared;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.annotation;

    exports serverMainFiles;
    exports serverCommands;
    exports serverTools;
    exports serverExceptions;
}