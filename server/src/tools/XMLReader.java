package tools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import models.Dragon;

import java.nio.file.Files;
import java.nio.file.Path;
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
    public static ArrayDeque<Dragon> readXmlCollection(String path) {
        try {
            Path filePath = Paths.get(path).toAbsolutePath();

            if (!Files.exists(filePath)) {
                System.out.println("Файл не найден по пути: " + filePath);
                return new ArrayDeque<>();
            }

            String xml = Files.readString(filePath);
            Pattern pattern = Pattern.compile("<Dragon>(.*?)</Dragon>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(xml);
            XmlMapper xmlMapper = new XmlMapper();
            ArrayDeque<Dragon> collection = new ArrayDeque<>();

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

}
