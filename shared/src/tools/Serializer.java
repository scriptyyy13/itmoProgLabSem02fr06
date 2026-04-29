package tools;

import exceptions.SerializeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {
    public static byte[] serializeToBytes(Serializable obj){
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("Не удалось сериализовать сообщение");
        }
    }


}
