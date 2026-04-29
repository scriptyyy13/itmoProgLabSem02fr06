package tools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import exceptions.XmlSaveException;
import models.Dragon;

import java.io.FileWriter;
import java.util.ArrayDeque;

/**
 * Класс, реализующий запись XML-файлов.
 */
public class XMLWriter {
    /**
     * Реализует запись коллекции в XML-файл.
     *
     * @param collection сохраняемая коллекция.
     * @param path       путь до сохраняемого файла.
     */
    public void dequeToXML(ArrayDeque<Dragon> collection, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(new CollectionWrapper(collection));
            writer.write(xml);
        } catch (Exception e) {
            throw new XmlSaveException("Не удалось сохранить коллекцию");
        }
    }

}

