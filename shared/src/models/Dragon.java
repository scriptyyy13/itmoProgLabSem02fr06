package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import exceptions.InvalidInputException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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
                locName
        );
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
