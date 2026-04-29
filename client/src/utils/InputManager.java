package utils;

import models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;

/**
 * Содержит статичсекие методы, преобразующие вводимые данные в обьекты.
 */
public class InputManager {

    private static final Deque<String> argumentsBuffer = new ArrayDeque<>(); // буфер аргументов

    /**
     * Заполняет буфер аргументами из команды
     */
    public static void loadArgs(String[] args) {
        argumentsBuffer.addAll(Arrays.asList(args));
    }

    /**
     * Очищает буфер
     */
    public static void clearBuffer() {
        argumentsBuffer.clear();
    }

    /**
     * Берет строку из буфера или из ридера
     */
    private static String getNextLine(Reader reader) {
        if (!argumentsBuffer.isEmpty()) {
            return argumentsBuffer.poll();
        }
        return reader.getLine();
    }

    /**
     * Предлагает ввода только если буфер пуст
     */
    private static void prompt(String message) {
        if (argumentsBuffer.isEmpty()) {
            OutputManager.println(message);
        }
    }

    /**
     * Создает обьект {@code Integer} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Integer}.
     */
    public static Integer inputInt(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                if (argumentsBuffer.isEmpty()) OutputManager.println("Неправильный формат числа");
                else throw new RuntimeException("Ошибка парсинга числа в скрипте: " + str);
            }
        }
    }

    /**
     * Создает обьект {@code Long} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Long}.
     */
    public static Long inputLong(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e) {
                if (argumentsBuffer.isEmpty()) OutputManager.println("Неправильный формат");
                else throw new RuntimeException("Ошибка парсинга Long в скрипте");
            }
        }
    }

    /**
     * Создает обьект {@code Float} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Float}.
     */
    public static Float inputFloat(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException e) {
                if (argumentsBuffer.isEmpty()) OutputManager.println("Неправильный формат");
                else throw new RuntimeException("Ошибка парсинга Float в скрипте");
            }
        }
    }

    /**
     * Создает обьект {@code Double} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Double}.
     */
    public static Double inputDouble(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                if (argumentsBuffer.isEmpty()) OutputManager.println("Неправильный формат");
                else throw new RuntimeException("Ошибка парсинга Double в скрипте");
            }
        }
    }

    /**
     * Создает обьект {@code String} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code String}.
     */
    public static String inputString(Reader reader, boolean nullable) {
        String str = getNextLine(reader);
        if (str == null || (nullable && str.isEmpty())) return null;
        if (!str.isEmpty()) return str;
        if (argumentsBuffer.isEmpty()) return inputString(reader, nullable);
        throw new RuntimeException("Пустая строка в данных скрипта");
    }

    /**
     * Создает обьект {@code Boolean} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Boolean}.
     */
    public static Boolean inputBool(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(str);
            }
            if (argumentsBuffer.isEmpty()) OutputManager.println("Введите true или false");
            else throw new RuntimeException("Ошибка: ожидалось boolean в скрипте");
        }
    }

    /**
     * Создает обьект {@code Date} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Date}.
     */
    public static Date inputDate(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return new SimpleDateFormat("dd.MM.yyyy").parse(str);
            } catch (ParseException e) {
                if (argumentsBuffer.isEmpty()) OutputManager.println("Введите дату в формате dd.MM.yyyy");
                else throw new RuntimeException("Ошибка парсинга даты в скрипте");
            }
        }
    }

    /**
     * Создает обьект {@code Color} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Color}.
     */
    public static Color inputColor(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Color.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException e) {
                if (argumentsBuffer.isEmpty()) OutputManager.println("Неверный цвет");
                else throw new RuntimeException("Ошибка цвета в скрипте: " + str);
            }
        }
    }

    /**
     * Создает обьект {@code Country} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Country}.
     */
    public static Country inputCountry(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Country.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException e) {
                if (argumentsBuffer.isEmpty()) OutputManager.println("Неверная страна");
                else throw new RuntimeException("Ошибка страны в скрипте: " + str);
            }
        }
    }

    /**
     * Создает обьект {@code Coordinates} из пользовательского ввода.
     *
     * @param reader обьект, реализующий чтение.
     * @return Вводимый обьект {@code Coordinates}.
     */
    public static Coordinates inputCoordinates(Reader reader) {
        prompt("Введите X : ");
        float x = inputFloat(reader, false);
        prompt("Введите Y (>-433) : ");
        double y = inputDouble(reader, false);
        return new Coordinates(x, y);
    }

    /**
     * Создает обьект {@code Location} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Location}.
     */
    public static Location inputLocation(Reader reader, boolean nullable) {
        if (nullable) {
            prompt("Ввести Location? (true/false) : ");
            if (!inputBool(reader, false)) return null;
        }
        prompt("Введите X: ");
        int x = inputInt(reader, false);
        prompt("Введите Y: ");
        int y = inputInt(reader, false);
        prompt("Введите Z: ");
        int z = inputInt(reader, false);
        prompt("Введите name: ");
        String name = inputString(reader, false);
        return new Location(x, y, z, name);
    }

    /**
     * Создает обьект {@code Person} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Person}.
     */
    public static Person inputPerson(Reader reader, boolean nullable) {
        if (nullable) {
            prompt("Ввести Person? (true/false) : ");
            if (!inputBool(reader, false)) return null;
        }
        prompt("Введите name : ");
        String name = inputString(reader, false);
        prompt("Введите birthday (dd.MM.yyyy) : ");
        Date birthday = inputDate(reader, false);
        prompt("Введите passportID : ");
        String pid = inputString(reader, true);
        prompt("Введите nationality : ");
        Country nat = inputCountry(reader, true);
        prompt("Введите location : ");
        Location loc = inputLocation(reader, true);
        return new Person(name, birthday, pid, nat, loc);
    }

    /**
     * Создает обьект {@code Dragon} из пользовательского ввода.
     *
     * @param reader обьект, реализующий чтение.
     * @return Вводимый обьект {@code Dragon}.
     */
    public static Dragon inputDragon(Reader reader) {
        prompt("Введите name : ");
        String name = inputString(reader, false);
        Coordinates coord = inputCoordinates(reader);
        prompt("Введите age (>0) : ");
        long age = inputLong(reader, false);
        prompt("Введите weight (>0) : ");
        Integer weight = inputInt(reader, true);
        prompt("Введите speaking (true/false) : ");
        Boolean speaking = inputBool(reader, false);
        prompt("Введите color : ");
        Color color = inputColor(reader, true);
        Person killer = inputPerson(reader, true);
        return new Dragon(-1, name, coord, new Date(), age, weight, speaking, color, killer);
    }
}
