package serverTools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import models.Dragon;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Класс-обёртка для коллекции.
 */
@JacksonXmlRootElement
public class CollectionWrapper {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Dragon")
    private ConcurrentLinkedDeque<Dragon> collection;

    public CollectionWrapper() {
    }

    public CollectionWrapper(ConcurrentLinkedDeque<Dragon> collection) {
        this.collection = collection;
    }

    public ConcurrentLinkedDeque<Dragon> getCollection() {
        return this.collection;
    }
}
