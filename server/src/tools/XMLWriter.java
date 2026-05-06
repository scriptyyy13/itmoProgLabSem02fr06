package tools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import exceptions.XmlSaveException;
import models.Dragon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static void dequeToXML(ArrayDeque<Dragon> collection, String path) {
        try {
            Path filePath = Paths.get(path).toAbsolutePath();
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                XmlMapper xmlMapper = new XmlMapper();
                String xml = xmlMapper.writeValueAsString(new CollectionWrapper(collection));
                writer.write(xml);
            }
        } catch (Exception e) {
            throw new XmlSaveException("Не удалось сохранить коллекцию: " + e.getMessage());
        }
    }

}

