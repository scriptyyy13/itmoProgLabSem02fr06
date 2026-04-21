package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import exceptions.InvalidInputException;

/**
 * Представляет {@code Location} со всеми характеристиками.
 */
@JacksonXmlRootElement
public class Location {
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

    public Location(){}

    /**
     * Создание экземпляра {@code Location}.
     */
    public Location(int x, int y, int z, String name){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public String toString(){
        if(isEmpty()) return "null";
        return "Location : \n" +
                "x = " + this.x + "\n" +
                "y = " + this.y + "\n" +
                "z = " + this.z + "\n" +
                "name = " + this.name + "\n";
    }

    /**
     * Валидация полей {@code Location}
     * @throws InvalidInputException выбрасывается при неуспешной валидации.
     */
    public void validate() throws InvalidInputException{
        if(x==null || y==null || z==null|| name.isEmpty()) throw new InvalidInputException("Неверный формат в элементе");
    }

    /**
     * Проверяет поля на null.
     */
    @JsonIgnore
    public boolean isEmpty(){
        return x==null && y==null && z == null && name == null;
    }

}
