package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import exceptions.InvalidInputException;
import sharedTools.DragonTableRow;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Представляет {@code Dragon} с описанием всех характеристик
 */
@JacksonXmlRootElement(localName = "Dragon")
public class Dragon implements Comparable<Dragon>, Serializable {
    /**
     * Уникальный индефикатор {@code Dragon}.
     * Поле не может быть null, Значение поля должно быть больше 0.
     */
    @JacksonXmlProperty
    private long id;

    /**
     * Уникальный индефикатор создателя {@code Dragon}.
     * Поле не может быть null, всегда есть создатель.
     */
    @JacksonXmlProperty
    private long creatorId;

    /**
     * Имя {@code Dragon}.
     * Поле не может быть null, Строка не может быть пустой.
     */
    @JacksonXmlProperty
    private String name;

    /**
     * Местоположение {@code Dragon}.
     * Поле не может быть null.
     */
    @JacksonXmlProperty
    private Coordinates coordinates;

    /**
     * Время инициализации эземпляра {@code Dragon}.
     * Поле не может быть null.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JacksonXmlProperty
    private Date creationDate;

    /**
     * Возраст {@code Dragon}.
     * Значение поля должно быть больше 0.
     */
    @JacksonXmlProperty
    private long age;

    /**
     * Вес {@code Dragon}.
     * Значение поля должно быть больше 0, Поле может быть null.
     */
    @JacksonXmlProperty
    private Integer weight;

    /**
     * Может ли {@code Dragon} говорить.
     * Поле не может быть null.
     */
    @JacksonXmlProperty
    private Boolean speaking;

    /**
     * Цвет {@code Dragon}.
     * Поле может быть null.
     */
    @JacksonXmlProperty
    private Color color;

    /**
     * Убийца {@code Dragon}.
     * Поле может быть null.
     */
    @JacksonXmlProperty
    private Person killer;

    public Dragon() {
    }

    /**
     * Создает экземпляр {@code Dragon}.
     */
    public Dragon(long id, String name, Coordinates coordinates, Date creationDate, long age, Integer weight, Boolean speaking, Color color, Person killer, Long creatorId) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.weight = weight;
        this.speaking = speaking;
        this.color = color;
        this.killer = killer;
        this.creatorId = creatorId;
    }

    public Dragon(long id, String name, Coordinates coordinates, Date creationDate, long age, Integer weight, Boolean speaking, Color color, Person killer) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.weight = weight;
        this.speaking = speaking;
        this.color = color;
        this.killer = killer;
    }

    /**
     * Превращаем {@code Dragon} в строку csv таблицы.
     */
    public String toCSV() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        String coordX = coordinates != null ? String.valueOf(coordinates.getX()) : "";
        String coordY = coordinates != null ? String.valueOf(coordinates.getY()) : "";

        String killerName = killer != null ? killer.getName() : "";
        String killerBirthday = (killer != null && killer.getBirthday() != null) ? sdf.format(killer.getBirthday()) : "";
        String killerPassport = (killer != null && killer.getPassportID() != null) ? killer.getPassportID() : "";
        String killerNat = (killer != null && killer.getNationality() != null) ? killer.getNationality().toString() : "";

        var loc = killer != null ? killer.getLocation() : null;
        String locX = loc != null ? String.valueOf(loc.getX()) : "";
        String locY = loc != null ? String.valueOf(loc.getY()) : "";
        String locZ = loc != null ? String.valueOf(loc.getZ()) : "";
        String locName = (loc != null && loc.getName() != null) ? loc.getName() : "";
        String creator_id = String.valueOf(this.getCreatorId());

        return String.join(",",
                String.valueOf(id),
                name != null ? name : "",
                coordX,
                coordY,
                creationDate != null ? sdf.format(creationDate) : "",
                String.valueOf(age),
                weight != null ? String.valueOf(weight) : "",
                String.valueOf(speaking),
                color != null ? color.toString() : "",
                killerName,
                killerBirthday,
                killerPassport,
                killerNat,
                locX,
                locY,
                locZ,
                locName,
                creator_id
        );
    }

    public static Dragon fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", -1);

        if (parts.length < 18) {
            throw new IllegalArgumentException("Некорректная строка CSV: " + csvLine);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");


        long id = Long.parseLong(parts[0]);


        String name = parts[1].isEmpty() ? null : parts[1];


        Float coordX = parts[2].isEmpty() ? null : Float.parseFloat(parts[2]);
        Double coordY = parts[3].isEmpty() ? null : Double.parseDouble(parts[3]);
        Coordinates coordinates = (coordX != null && coordY != null) ? new Coordinates(coordX, coordY) : null;


        Date creationDate = null;
        if (!parts[4].isEmpty()) {
            try {
                creationDate = sdf.parse(parts[4]);
            } catch (Exception e) {
            }
        }

        int age = parts[5].isEmpty() ? 0 : Integer.parseInt(parts[5]);

        Integer weight = parts[6].isEmpty() ? null : Integer.parseInt(parts[6]);

        boolean speaking = !parts[7].isEmpty() && Boolean.parseBoolean(parts[7]);

        Color color = parts[8].isEmpty() ? null : Color.valueOf(parts[8]);

        Long createID = parts[17].isEmpty() ? null : Long.parseLong(parts[17]);

        Person killer = null;
        boolean hasKillerData = !parts[9].isEmpty() || !parts[10].isEmpty() ||
                !parts[11].isEmpty() || !parts[12].isEmpty();

        if (hasKillerData) {
            String killerName = parts[9].isEmpty() ? null : parts[9];


            Date killerBirthday = null;
            if (!parts[10].isEmpty()) {
                try {
                    killerBirthday = sdf.parse(parts[10]);
                } catch (Exception e) {
                }
            }

            String killerPassport = parts[11].isEmpty() ? null : parts[11];

            Country killerNat = null;
            if (!parts[12].isEmpty()) {
                try {
                    killerNat = Country.valueOf(parts[12]);
                } catch (IllegalArgumentException e) {

                }
            }

            Integer locX = parts[13].isEmpty() ? null : Integer.parseInt(parts[13]);
            Integer locY = parts[14].isEmpty() ? null : Integer.parseInt(parts[14]);
            Integer locZ = parts[15].isEmpty() ? null : Integer.parseInt(parts[15]);
            String locName = parts[16].isEmpty() ? null : parts[16];


            Location location = null;
            if (locX != null && locY != null && locZ != null && locName != null) {
                location = new Location(locX, locY, locZ, locName);
            }

            killer = new Person(killerName, killerBirthday, killerPassport, killerNat, location);
        }

        return new Dragon(id, name, coordinates, creationDate, age, weight, speaking, color, killer,createID);
    }

    public static DragonTableRow mapToTableRow(Dragon d) {
        // 1. Координаты самого дракона (это Float, а у вас в Coordinates Float)
        Float x = (d.getCoordinates() != null) ? d.getCoordinates().getX() : null;
        Double y = (d.getCoordinates() != null) ? d.getCoordinates().getY() : null;

        // 2. Данные убийцы
        Person killer = d.getKiller();
        String killerName = null;
        Date killerBirthday = null;
        String killerPassport = null;
        String killerNat = null;
        Integer locX = null;
        Integer locY = null;
        Integer locZ = null;
        String locName = null;

        if (killer != null) {
            killerName = killer.getName();
            killerBirthday = killer.getBirthday();
            killerPassport = killer.getPassportID();
            killerNat = (killer.getNationality() != null) ? killer.getNationality().name() : null;

            Location loc = killer.getLocation();
            if (loc != null) {
                locX = loc.getX();
                locY = loc.getY();
                locZ = loc.getZ();
                locName = loc.getName();
            }
        }

        // 3. Возвращаем строку таблицы
        return new DragonTableRow(
                d.getId(),
                d.getCreatorId(),
                d.getName(),
                x,
                y,
                d.getCreationDate(),
                d.getAge(),
                d.getWeight(),
                d.getSpeaking(),
                (d.getColor() != null) ? d.getColor().name() : null,
                killerName,
                killerBirthday,
                killerPassport,
                killerNat,
                locX,
                locY,
                locZ,
                locName
        );
    }

    public static List<Dragon> loadDragonsFromCSV(String csv) {
        List<String> lines = List.of(csv.split("\n"));
        return lines.stream()
                .skip(1)
                .map(Dragon::fromCSV)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Dragon:\n" +
                "id = " + (this.id) + "\n"
                + "name = " + this.name + "\n"
                + "coordinates = {" + (this.coordinates).toString() + "}\n"
                + "creationDate = " + this.creationDate + "\n"
                + "age = " + this.age + "\n"
                + "weight = " + this.weight + "\n"
                + "speaking = " + this.speaking + "\n"
                + "color = " + this.color + "\n"
                + "killer = {" + (this.killer == null ? "null" : (this.killer).toString()) + "}\n\n";
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getAge() {
        return age;
    }

    public Integer getWeight() {
        return weight;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Boolean getSpeaking() {
        return speaking;
    }

    public Color getColor() {
        return color;
    }

    public Person getKiller() {
        return killer;
    }

    public void setCreatorId(long id) {
        this.creatorId = id;
    }

    public long getCreatorId() {
        return creatorId;
    }

    /**
     * Реализует сравнение.
     *
     * @param other сравниваемый {@code Dragon}.
     * @return Положительное число если экземпляр больше, иначе отрицательное.
     */
    @Override
    public int compareTo(Dragon other) {
        if (other == null) return 1;
        return this.getName().compareTo(other.getName());
    }

    /**
     * Проверяет на равенство объектов.
     *
     * @param obj Сравниваемый обьект
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.hashCode() == obj.hashCode()) return true;
        if (getClass() != obj.getClass()) return false;

        Dragon other = (Dragon) obj;
        return this.id == other.id;
    }

    /**
     * Получает хэш-код объекта.
     *
     * @return хэш-код.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, age, weight, speaking, color, killer);
    }

    /**
     * Валидация полей {@code Dragon}.
     *
     * @throws InvalidInputException выбрасывается при неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (name == null) throw new InvalidInputException("error.model.validate (Dragon.name)");
        if (weight != null && weight <= 0)
            throw new InvalidInputException("error.model.validate (Dragon.weight)");
        if (age <= 0) throw new InvalidInputException("error.model.validate (Dragon.age)");
        if (speaking == null) throw new InvalidInputException("error.model.validate (Dragon.speaking)");
        coordinates.validate();
    }
}
