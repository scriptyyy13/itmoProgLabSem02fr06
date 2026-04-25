package tools;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


/**
 * Представляет аргумент команды.
 */
@JacksonXmlRootElement(localName = "arg")
public class Arg {
    /**
     * Значение аргумента.
     */
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "class"
    )
    private Object value;

    public Arg() {
    }

    /**
     * Создает экземпляр аргумента.
     */
    public Arg(Object value) {
        this.value = value;
    }

    /**
     * Преобразует массив {@code Object} в массив {@code Arg}.
     *
     * @param list массив {@code Object}.
     * @return масиив {@code Arg}.
     */
    public static Arg[] toArgList(Object[] list) {
        Arg[] argList = new Arg[list.length];
        for (int i = 0; i < list.length; i++) {
            argList[i] = new Arg(list[i]);
        }
        return argList;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
