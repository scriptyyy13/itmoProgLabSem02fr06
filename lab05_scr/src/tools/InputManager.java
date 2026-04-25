package tools;


import exceptions.InvalidInputException;
import models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Содержит статичсекие методы, преобразующие вводимые данные в обьекты.
 */
public class InputManager {
    /**
     * Создает обьект {@code Integer} из пользовательского ввода.
     *
     * @param reader   обьект, реализующий чтение.
     * @param nullable может ли быть вводимый обьект null.
     * @return Вводимый обьект {@code Integer}.
     */
    public static Integer inputInt(Reader reader, boolean nullable) {
        while (true) {
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return Integer.parseInt(str);
            } catch (InvalidInputException | NumberFormatException e) {
                OutputManager.println("Неправильный формат");
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
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return Long.parseLong(str);
            } catch (InvalidInputException | NumberFormatException e) {
                OutputManager.println("Неправильный формат");
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
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return Float.parseFloat(str);
            } catch (InvalidInputException | NumberFormatException e) {
                OutputManager.println("Неправильный формат");
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
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return Double.parseDouble(str);
            } catch (InvalidInputException | NumberFormatException e) {
                OutputManager.println("Неправильный формат");
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
        while (true) {
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return str;
            } catch (InvalidInputException e) {
                OutputManager.println("Неправильный формат");
            }
        }
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
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return Boolean.parseBoolean(str);
            } catch (InvalidInputException e) {
                OutputManager.println("Неправильный формат");
            }
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
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                return format.parse(str);
            } catch (InvalidInputException | ParseException e) {
                OutputManager.println("Неправильный формат");
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
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return Color.valueOf(str);
            } catch (InvalidInputException | IllegalArgumentException e) {
                OutputManager.println("Неправильный формат");
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
            var str = reader.getLine();
            if (nullable && str.isEmpty()) return null;
            try {
                if (str.isEmpty()) throw new InvalidInputException("");
                return Country.valueOf(str);
            } catch (InvalidInputException | IllegalArgumentException e) {
                OutputManager.println("Неправильный формат");
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
        float x;
        double y;
        System.out.print("Введите X : ");
        x = inputFloat(reader, false);
        System.out.print("Введите Y (>-433) : ");
        while (true) {
            try {
                y = inputDouble(reader, false);
                if (y <= -433) throw new InvalidInputException("Число вне диапозона");
                break;
            } catch (InvalidInputException e) {
                OutputManager.println(e.getMessage());
            }
        }
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
            boolean flag;
            System.out.print("Поле не обязательно. Продолжить ввод? (true/false) : ");
            flag = inputBool(reader, false);
            if (!flag) return null;
        }
        int x, y, z;
        String name;
        System.out.print("Введите X: ");
        x = inputInt(reader, false);
        System.out.print("Введите Y: ");
        y = inputInt(reader, false);
        System.out.print("Введите Z: ");
        z = inputInt(reader, false);
        System.out.print("Введите name: ");
        name = inputString(reader, false);
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
            boolean flag;
            System.out.print("Поле не обязательно. Продолжить ввод? (true/false) : ");
            flag = inputBool(reader, false);
            if (!flag) return null;
        }
        String name, passportID;
        Country nationality;
        Location location;
        Date birthday;
        System.out.print("Введите name : ");
        name = inputString(reader, false);
        System.out.print("Введите birthday (DD.MM.YYYY) : ");
        birthday = inputDate(reader, false);
        System.out.print("Введите passportID : ");
        passportID = inputString(reader, true);
        System.out.print("Введите nationality (необязательно) : [CHINA, INDIA, THAILAND, NORTH_KOREA] : ");
        nationality = inputCountry(reader, true);
        System.out.print("Введите location (необязательно) : ");
        location = inputLocation(reader, true);
        return new Person(name, birthday, passportID, nationality, location);
    }

    /**
     * Создает обьект {@code Dragon} из пользовательского ввода.
     *
     * @param reader обьект, реализующий чтение.
     * @return Вводимый обьект {@code Dragon}.
     */
    public static Dragon inputDragon(Reader reader) {
        String name; //Поле не может быть null, Строка не может быть пустой
        Coordinates coordinates; //Поле не может быть null
        long age; //Значение поля должно быть больше 0
        Integer weight; //Значение поля должно быть больше 0, Поле может быть null
        Boolean speaking; //Поле не может быть null
        Color color; //Поле может быть null
        Person killer;
        System.out.print("Введите name : ");
        name = inputString(reader, false);
        OutputManager.println("Введите coordinates : ");
        coordinates = inputCoordinates(reader);
        while (true) {
            System.out.print("Введите age (>0) : ");
            try {
                age = inputLong(reader, false);
                if (age <= 0) throw new InvalidInputException("");
                break;
            } catch (InvalidInputException e) {
                OutputManager.println("Неверный формат");
            }
        }
        while (true) {
            System.out.print("Введите weight (>0) (необязательное поле): ");
            weight = inputInt(reader, true);
            if (weight == null) break;
            try {
                if (weight <= 0) throw new InvalidInputException("");
                break;
            } catch (InvalidInputException e) {
                OutputManager.println("Неверный формат");
            }
        }
        System.out.print("Введите speaking (false/true) : ");
        speaking = inputBool(reader, false);
        System.out.print("Введите color (не обязательное поле) [GREEN, RED, BLACK, BLUE, YELLOW] : ");
        color = inputColor(reader, true);
        System.out.print("Введите person (не обязательное поле) : ");
        killer = inputPerson(reader, true);
        return new Dragon(-1, name, coordinates, new Date(), age, weight, speaking, color, killer);
    }
}
