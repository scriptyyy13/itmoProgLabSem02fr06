package tools;

import exceptions.DeserializeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserializer {
    public static Object deserializeFromBytes(byte[] bytes){
        try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DeserializeException("Ошибка десериализации");
        }
    }
}
