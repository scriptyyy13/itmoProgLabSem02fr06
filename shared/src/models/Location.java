package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Представляет {@code Location} со всеми характеристиками.
 */
@JacksonXmlRootElement
public class Location implements Serializable {
    /**
     * Координата x.
     * Поле не может быть null.
     */
    @JacksonXmlProperty
    private Integer x;
    /**
     * Координата y.
     * Поле не может быть null.
     */
    @JacksonXmlProperty
    private Integer y;
    /**
     * Координата z.
     * Поле не может быть null.
     */
    @JacksonXmlProperty
    private Integer z;
    /**
     * Название {@code Location}.
     * Поле не может быть null.
     */
    @JacksonXmlProperty
    private String name;

    public Location() {
    }

    /**
     * Создание экземпляра {@code Location}.
     */
    public Location(int x, int y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "null";
        return "Location : \n" +
                "x = " + this.x + "\n" +
                "y = " + this.y + "\n" +
                "z = " + this.z + "\n" +
                "name = " + this.name + "\n";
    }

    /**
     * Валидация полей {@code Location}
     *
     * @throws InvalidInputException выбрасывается при неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (x == null || y == null || z == null || name==null||name.isEmpty())
            throw new InvalidInputException("Неверный формат в элементе");
    }

    /**
     * Проверяет поля на null.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return (name == null && x == null && y==null && z == null);
    }
    @Override
    public boolean equals(Object oth) {
        if (this == oth) return true;
        if (oth == null || getClass() != oth.getClass()) return false;
        Location location = (Location) oth;
        return ((name == null && location.getName() ==null)||name.equals( location.getName()))&&
                ((x == null && location.getX() ==null)||x.equals( location.getX())  )&&
                        ((y == null && location.getY() ==null)||y.equals( location.getY())  )&&
                                ((z == null && location.getZ() ==null)||z.equals(location.getZ()) );
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, x,y,z);
    }

    public String getName(){return name;}
    public Integer getX(){return x;}
    public Integer getY(){return y;}
    public Integer getZ(){return z;}
}
