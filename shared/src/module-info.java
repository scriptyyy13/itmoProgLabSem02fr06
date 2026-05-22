/**
 * Модуль с общими данными для клиента и сервера
 */

module shared {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.dataformat.xml;
    exports sharedTools;
    exports commands;
    exports exceptions;
    exports models;
}