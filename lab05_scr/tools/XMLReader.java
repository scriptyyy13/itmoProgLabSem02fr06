package tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import commands.Command;
import exceptions.XmlReadingException;
import exceptions.XmlSaveException;
import models.Dragon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс, реализующий чтение XML-файлов.
 */
public class XMLReader {

    /**
     * Читает XML-файл коллекции.
     *
     * @param path путь до файла.
     * @return прочитанная коллекция.
     */
    public ArrayDeque<Dragon> readXmlCollection(String path) {
        try {
            String xml = Files.readString(Paths.get(path));
            Pattern pattern = Pattern.compile("<Dragon>(.*?)</Dragon>");
            Matcher matcher = pattern.matcher(xml);
            XmlMapper xmlMapper = new XmlMapper();
            ArrayDeque<Dragon> collection = new ArrayDeque<Dragon>();
            while (matcher.find()) {
                try {
                    collection.add(xmlMapper.readValue(matcher.group(), Dragon.class));
                } catch (Exception e) {
                    System.out.println("Ошибка в чтении элемента коллекции, пропущен");
                }
            }
            return collection;
        } catch (Exception e) {
            System.out.println("Ошибка чтения файла, возвращена пустая коллекция");
            return new ArrayDeque<Dragon>();
        }
    }

    /**
     * Читает XML-файл журнала команд..
     *
     * @param path путь до файла.
     * @return прочитанный журнал.
     */
    public CommandList readXmlJournal(String path) {
        try {
            String xml = Files.readString(Paths.get(path));
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(xml, CommandList.class);
        } catch (Exception e) {
            throw new XmlReadingException("Не удалось прочитать журнал");
        }
    }
}
