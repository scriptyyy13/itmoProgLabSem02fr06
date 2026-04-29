package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import exceptions.InvalidInputException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Представялет {@code Person} co всеми характеристиками.
 */
@JacksonXmlRootElement
public class Person implements Serializable {
    /**
     * Имя {@code Person}.
     */
    @JacksonXmlProperty
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    /**
     * Дата рождения {@code Person}.
     */
    @JacksonXmlProperty
    private java.util.Date birthday;
    /**
     * ID паспорта {@code Person}.
     */
    @JacksonXmlProperty
    private String passportID;
    /**
     * Национальность {@code Person}.
     */
    @JacksonXmlProperty
    private Country nationality;
    /**
     * Местоположение {@code Person}.
     */
    @JacksonXmlProperty
    private Location location;

    public Person() {
    }

    /**
     * Созданике экземпляра {@code Person}.
     */
    public Person(String name, Date birthday, String passportID, Country nationality, Location location) {
        this.name = name;
        this.birthday = birthday;
        this.passportID = passportID;
        this.nationality = nationality;
        this.location = location;
    }

    @Override
    public String toString() {
        if (this.name == null) return "null\n";
        return "Person: " + "\n" +
                "name = " + this.name + "\n" +
                "birthday = " + (new SimpleDateFormat("dd.MM.yyyy")).format(this.birthday) + "\n" +
                "passportID = " + this.passportID + "\n" +
                "nationality = " + this.nationality + "\n" +
                "location = {" + (this.location == null ? "null" : (this.location).toString()) + "}\n";
    }

    /**
     * Валидация полей {@code Person}
     *
     * @throws InvalidInputException выбрасывается при неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (name.isEmpty() || birthday == null || passportID == null)
            throw new InvalidInputException("Неверный формат в элементе");
    }

    /**
     * Проверяет поля на null.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return name == null && birthday == null && passportID == null && location == null && nationality == null;
    }
}
