package graphics;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Менеджер для динамической смены локали без перезапуска приложения.
 */
public class LocalizationManager {
    private static final String BUNDLE_NAME = "translations";

    // Свойство, хранящее текущий пакет ресурсов
    private static final ObjectProperty<ResourceBundle> bundle = new SimpleObjectProperty<>();

    static {
        // По умолчанию ставим русскую локаль
        setLocale(new Locale("ru"));
    }

    public static ResourceBundle getBundle() {
        return bundle.get();
    }

    public static ObjectProperty<ResourceBundle> bundleProperty() {
        return bundle;
    }

    /**
     * Смена языка на лету
     */
    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        bundle.set(ResourceBundle.getBundle(BUNDLE_NAME, locale));
    }

    /**
     * Создает привязку для простых строк без аргументов.
     */
    public static StringBinding createStringBinding(String key) {
        return Bindings.createStringBinding(() -> {
            try {
                return bundle.get().getString(key);
            } catch (Exception e) {
                return key; // Если ключ не найден, возвращаем сам ключ
            }
        }, bundle);
    }

    /**
     * Метод для ручного форматирования строк с аргументами ({0}, {1}) из ответов сервера.
     */
    public static String getLocalizedMessage(String serverResponse) {
        if (serverResponse == null || !serverResponse.contains(":")) {
            return serverResponse;
        }
        try {
            String cleanResponse = serverResponse.substring(serverResponse.indexOf(":") + 1);
            String[] parts = cleanResponse.split(";");
            String key = parts[0];
            Object[] args = java.util.Arrays.copyOfRange(parts, 1, parts.length);

            String rawMessage = bundle.get().getString(key);
            return MessageFormat.format(rawMessage, args);
        } catch (Exception e) {
            return serverResponse; // В случае ошибки возвращаем сырой ответ
        }
    }
}
