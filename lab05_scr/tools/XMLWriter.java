package tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import commands.Command;
import exceptions.XmlSaveException;
import models.Dragon;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

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

    /**
     * Реализует запись журнала команд в XML-файл.
     *
     * @param journal сохраняемый журнал.
     */
    public void journalToXML(CommandList journal) {
        try (FileWriter writer = new FileWriter("journal.xml")) {
            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(journal);
            writer.write(xml);
        } catch (Exception e) {
            throw new XmlSaveException("Не удалось сохранить журнал");
        }

    }
}

