package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import exceptions.InvalidInputException;

import java.io.Serializable;
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
        if (name == null) throw new InvalidInputException("Неверный формат в элементе");
        if (weight != null && weight <= 0) throw new InvalidInputException("Неверный формат в элементе");
        if (age <= 0) throw new InvalidInputException("Неверный формат в элементе");
        if (speaking == null) throw new InvalidInputException("Неверный формат в элементе");
        coordinates.validate();
        if (killer != null && !killer.isEmpty()) killer.validate();
    }
}
