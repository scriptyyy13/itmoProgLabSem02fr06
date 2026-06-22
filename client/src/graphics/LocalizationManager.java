package graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Менеджер для динамической смены локали без перезапуска приложения.
 */
public class LocalizationManager {
    /**
     * Относительный путь к папке с файлами локализации от корня проекта.
     */
    private static final String TRANSLATIONS_PATH = "client/src/graphics/resources/translations/";

    /**
     * Свойство, хранящее текущий пакет ресурсов.
     */
    private static final ObjectProperty<ResourceBundle> bundle = new SimpleObjectProperty<>();

    static {
        // По умолчанию ставим русскую локаль
        setLocale(new Locale("ru"));
    }

    /**
     * Возвращает текущий пакет ресурсов.
     *
     * @return текущий {@code ResourceBundle}.
     */
    public static ResourceBundle getBundle() {
        return bundle.get();
    }

    /**
     * Возвращает свойство пакета ресурсов для связывания (binding).
     *
     * @return свойство {@code ObjectProperty<ResourceBundle>}.
     */
    public static ObjectProperty<ResourceBundle> bundleProperty() {
        return bundle;
    }

    /**
     * Смена языка на лету. Читает файлы напрямую из файловой системы по пути {@link #TRANSLATIONS_PATH}.
     *
     * @param locale новая локаль для установки.
     */
    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        try {
            File file = new File(TRANSLATIONS_PATH + "messages_" + locale.getLanguage() + ".properties");

            if (!file.exists()) {
                file = new File(TRANSLATIONS_PATH + "messages.properties");
            }

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file);
                     InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {

                    ResourceBundle rb = new PropertyResourceBundle(isr);
                    bundle.set(rb);
                }
            } else {
                System.err.println("Файл локализации не найден по пути: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Создает привязку для простых строк без аргументов.
     *
     * @param key ключ локализации из файла свойств.
     * @return строковая привязка {@code StringBinding}.
     */
    public static StringBinding createStringBinding(String key) {
        return Bindings.createStringBinding(() -> {
            try {
                return bundle.get().getString(key.trim());
            } catch (Exception e) {
                return key; // Если ключ не найден, возвращаем сам ключ
            }
        }, bundle);
    }

    /**
     * Метод для форматирования строк.
     *
     * @param serverResponse сырой ответ от сервера, содержащий код и ключ с аргументами.
     * @return локализованная строка.
     */
    public static String getLocalizedMessage(String serverResponse) {
        if (serverResponse == null) {
            return "";
        }

        String cleanResponse = serverResponse.trim();
        if (cleanResponse.contains(":")) {
            cleanResponse = cleanResponse.substring(cleanResponse.indexOf(":") + 1);
        }

        try {
            if (cleanResponse.contains(";")) {
                String[] parts = cleanResponse.split(";");
                String key = parts[0].trim();
                Object[] args = java.util.Arrays.copyOfRange(parts, 1, parts.length);

                String rawMessage = bundle.get().getString(key);
                return MessageFormat.format(rawMessage, args);
            } else {
                return bundle.get().getString(cleanResponse);
            }
        } catch (Exception e) {
            return cleanResponse;
        }
    }
}