package utils;

import models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;

/**
 * Содержит статические методы, преобразующие вводимые данные в объекты.
 */
public class InputManager {

    /**
     * Буфер аргументов.
     */
    private static final Deque<String> argumentsBuffer = new ArrayDeque<>();

    /**
     * Заполняет буфер аргументами из команды.
     */
    public static void loadArgs(String[] args) {
        argumentsBuffer.addAll(Arrays.asList(args));
    }

    /**
     * Очищает буфер.
     */
    public static void clearBuffer() {
        argumentsBuffer.clear();
    }

    /**
     * Берет строку из буфера или из ридера.
     */
    private static String getNextLine(Reader reader) {
        if (!argumentsBuffer.isEmpty()) {
            return argumentsBuffer.poll();
        }
        return reader.getLine();
    }

    /**
     * Создает объект Integer из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Integer.
     */
    public static Integer inputInt(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Integer.parseInt(str.trim());
            } catch (NumberFormatException e) {
                throw new RuntimeException("error.parse.integer");
            }
        }
    }

    /**
     * Создает объект Long из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Long.
     */
    public static Long inputLong(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Long.parseLong(str.trim());
            } catch (NumberFormatException e) {
                throw new RuntimeException("error.parse.long");
            }
        }
    }

    /**
     * Создает объект Float из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Float.
     */
    public static Float inputFloat(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) {
                if (nullable) return null;
                throw new RuntimeException("error.parse.float_required");
            }
            if (nullable && str.isEmpty()) return null;
            try {
                return Float.parseFloat(str.trim());
            } catch (NumberFormatException e) {
                throw new RuntimeException("error.parse.float");
            }
        }
    }

    /**
     * Создает объект Double из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Double.
     */
    public static Double inputDouble(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Double.parseDouble(str.trim());
            } catch (NumberFormatException e) {
                throw new RuntimeException("error.parse.double");
            }
        }
    }

    /**
     * Создает объект String из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект String.
     */
    public static String inputString(Reader reader, boolean nullable) {
        String str = getNextLine(reader);
        if (str == null || (nullable && str.isEmpty())) return null;
        if (!str.isEmpty()) return str;
        return inputString(reader, nullable);
    }

    /**
     * Создает объект Boolean из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Boolean.
     */
    public static Boolean inputBool(Reader reader, boolean nullable) {
        while (true) {
            String line = getNextLine(reader);
            if (line == null) {
                if (nullable) return null;
                throw new RuntimeException("error.parse.boolean_required");
            }
            String str = line.trim().toLowerCase();
            if (str.isEmpty()) {
                if (nullable) return null;
            } else {
                if (str.equals("true") || str.equals("t")) {
                    return true;
                }
                if (str.equals("false") || str.equals("f")) {
                    return false;
                }
            }
            throw new RuntimeException("error.parse.boolean");
        }
    }

    /**
     * Создает объект Date из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Date.
     */
    public static Date inputDate(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return new SimpleDateFormat("dd.MM.yyyy").parse(str.trim());
            } catch (ParseException e) {
                throw new RuntimeException("error.parse.date");
            }
        }
    }

    /**
     * Создает объект Color из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Color.
     */
    public static Color inputColor(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Color.valueOf(str.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("error.parse.color");
            }
        }
    }

    /**
     * Создает объект Country из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Country.
     */
    public static Country inputCountry(Reader reader, boolean nullable) {
        while (true) {
            String str = getNextLine(reader);
            if (str == null) return null;
            if (nullable && str.isEmpty()) return null;
            try {
                return Country.valueOf(str.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("error.parse.country");
            }
        }
    }

    /**
     * Создает объект Coordinates из пользовательского ввода.
     *
     * @param reader объект, реализующий чтение.
     * @return Вводимый объект Coordinates.
     */
    public static Coordinates inputCoordinates(Reader reader) {
        float x = inputFloat(reader, false);
        double y = inputDouble(reader, false);
        return new Coordinates(x, y);
    }

    /**
     * Создает объект Location из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Location.
     */
    public static Location inputLocation(Reader reader, boolean nullable) {
        if (nullable) {
            if (!inputBool(reader, false)) return null;
        }
        int x = inputInt(reader, false);
        int y = inputInt(reader, false);
        int z = inputInt(reader, false);
        String name = inputString(reader, false);
        return new Location(x, y, z, name);
    }

    /**
     * Создает объект Person из пользовательского ввода.
     *
     * @param reader   объект, реализующий чтение.
     * @param nullable может ли быть вводимый объект null.
     * @return Вводимый объект Person.
     */
    public static Person inputPerson(Reader reader, boolean nullable) {
        if (nullable) {
            if (!inputBool(reader, false)) return null;
        }
        String name = inputString(reader, false);
        Date birthday = inputDate(reader, false);
        String pid = inputString(reader, true);
        Country nat = inputCountry(reader, true);
        Location loc = inputLocation(reader, true);
        return new Person(name, birthday, pid, nat, loc);
    }

    /**
     * Создает объект Dragon из пользовательского ввода.
     *
     * @param reader объект, реализующий чтение.
     * @return Вводимый объект Dragon.
     */
    public static Dragon inputDragon(Reader reader) {
        String name = inputString(reader, false);
        Coordinates coord = inputCoordinates(reader);
        long age = inputLong(reader, false);
        Integer weight = inputInt(reader, true);
        Boolean speaking = inputBool(reader, false);
        Color color = inputColor(reader, true);
        Person killer = inputPerson(reader, true);
        return new Dragon(-1, name, coord, new Date(), age, weight, speaking, color, killer);
    }
}