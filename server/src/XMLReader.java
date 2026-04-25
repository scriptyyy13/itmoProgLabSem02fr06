import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import models.Dragon;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
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
                    OutputManager.println("Ошибка в чтении элемента коллекции, пропущен");
                }
            }
            return collection;
        } catch (Exception e) {
            OutputManager.println("Ошибка чтения файла, возвращена пустая коллекция");
            return new ArrayDeque<Dragon>();
        }
    }

}
