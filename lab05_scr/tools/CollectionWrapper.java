package tools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import models.Dragon;

import java.util.ArrayDeque;

/**
 * Класс-обёртка для коллекции.
 */
@JacksonXmlRootElement
public class CollectionWrapper {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Dragon")
    private ArrayDeque<Dragon> collection;

    public CollectionWrapper(){}

    public CollectionWrapper(ArrayDeque<Dragon> collection){this.collection = collection;}

    public ArrayDeque<Dragon> getCollection(){return this.collection;}
}
