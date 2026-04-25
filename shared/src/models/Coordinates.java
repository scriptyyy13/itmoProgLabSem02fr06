package models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import exceptions.InvalidInputException;

import java.io.Serializable;

/**
 * Содержит информацию о экземпляре {@code Coordinates}
 */
@JacksonXmlRootElement
public class Coordinates implements Serializable {
    /**
     * Координата x.
     * Поле не может быть null.
     */
    @JacksonXmlProperty
    private Float x;

    /**
     * Координата y.
     * Значение поля должно быть больше -433, Поле не может быть null
     */
    @JacksonXmlProperty
    private Double y;

    public Coordinates() {
    }

    /**
     * Создает экземпляр {@code Coordinates}.
     */
    public Coordinates(Float x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates: " + "\n" +
                "X = " + this.x + "\n" +
                "Y = " + this.y + "\n";
    }

    /**
     * Валидация полей экземпляра.
     *
     * @throws InvalidInputException Выбрасывается при неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (x == null) throw new InvalidInputException("Неверный формат в элементе");
        if (y == null || y <= -433) throw new InvalidInputException("Неверный формат в элементе");
    }
}
