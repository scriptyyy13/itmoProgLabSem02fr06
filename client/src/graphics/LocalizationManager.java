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
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class LocalizationManager {
    private static final String TRANSLATIONS_PATH = "client/src/graphics/resources/translations/";
    private static final ObjectProperty<ResourceBundle> bundle = new SimpleObjectProperty<>();

    static {
        setLocale(new Locale("ru"));
    }

    public static ResourceBundle getBundle() { return bundle.get(); }
    public static ObjectProperty<ResourceBundle> bundleProperty() { return bundle; }

    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        try {
            String suffix = locale.getCountry().isEmpty() ? locale.getLanguage() : locale.getLanguage() + "_" + locale.getCountry();
            File file = new File(TRANSLATIONS_PATH + "messages_" + suffix + ".properties");

            if (!file.exists()) {
                file = new File(TRANSLATIONS_PATH + "messages_" + locale.getLanguage() + ".properties");
            }
            if (!file.exists()) {
                file = new File(TRANSLATIONS_PATH + "messages.properties");
            }

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file);
                     InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                    ResourceBundle rb = new PropertyResourceBundle(isr);
                    bundle.set(rb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StringBinding createStringBinding(String key) {
        return Bindings.createStringBinding(() -> {
            try { return bundle.get().getString(key.trim()); }
            catch (Exception e) { return key; }
        }, bundle);
    }

    public static String getLocalizedMessage(String serverResponse) {
        if (serverResponse == null) return "";
        String cleanResponse = serverResponse.trim();
        if (cleanResponse.contains(":")) {
            cleanResponse = cleanResponse.substring(cleanResponse.indexOf(":") + 1);
        }
        try {
            if (cleanResponse.contains(";")) {
                String[] parts = cleanResponse.split(";");
                String key = parts[0].trim();
                Object[] args = java.util.Arrays.copyOfRange(parts, 1, parts.length);
                return MessageFormat.format(bundle.get().getString(key), args);
            } else {
                return bundle.get().getString(cleanResponse);
            }
        } catch (Exception e) {
            return cleanResponse;
        }
    }

    /**
     * Создает MenuBar. Вызывается автоматически базовым классом окон.
     */
    public static MenuBar createGlobalMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu mainMenu = new Menu();
        mainMenu.textProperty().bind(createStringBinding("gui.menu.main"));

        MenuItem helpItem = new MenuItem();
        helpItem.textProperty().bind(createStringBinding("gui.menu.help"));
        helpItem.setOnAction(e -> showHelpAlert());

        MenuItem infoItem = new MenuItem();
        infoItem.textProperty().bind(createStringBinding("gui.menu.info"));
        infoItem.setOnAction(e -> showInfoAlert());

        mainMenu.getItems().addAll(helpItem, infoItem);

        Menu langMenu = new Menu();
        langMenu.textProperty().bind(createStringBinding("gui.menu.language"));

        MenuItem ruLang = new MenuItem("Русский");
        ruLang.setOnAction(e -> setLocale(new Locale("ru")));

        MenuItem fiLang = new MenuItem("Suomi");
        fiLang.setOnAction(e -> setLocale(new Locale("fi")));

        MenuItem ltLang = new MenuItem("Lietuvių");
        ltLang.setOnAction(e -> setLocale(new Locale("lt")));

        MenuItem ieLang = new MenuItem("English (Ireland)");
        ieLang.setOnAction(e -> setLocale(new Locale("en", "IE")));

        langMenu.getItems().addAll(ruLang, fiLang, ltLang, ieLang);
        menuBar.getMenus().addAll(mainMenu, langMenu);

        return menuBar;
    }

    private static void showHelpAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().bind(createStringBinding("gui.help.title"));
        alert.headerTextProperty().bind(createStringBinding("gui.help.header"));
        alert.contentTextProperty().bind(createStringBinding("gui.help.content"));
        alert.showAndWait();
    }

    private static void showInfoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().bind(createStringBinding("gui.info.title"));
        alert.headerTextProperty().bind(createStringBinding("gui.info.header"));
        alert.contentTextProperty().bind(createStringBinding("gui.info.content"));
        alert.showAndWait();
    }
}