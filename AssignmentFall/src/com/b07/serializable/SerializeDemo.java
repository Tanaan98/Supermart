package com.b07.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeDemo {
  // All data stored in the database should be outputted into a file called
  // database_copy.ser
  private static final String filepath = "database_copy.ser";

  /**
   * Serializes the given object to the file at the filepath.
   * 
   * @param obj The object to serialize.
   * @throws IOException If the ids do not align with the ones in your Enum classes, or if the one
   *         who runs the operation is no longer in the database
   */
  public static void serialize(Object obj) throws IOException {
    try {
      FileOutputStream fileOut = new FileOutputStream(filepath);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(obj);
      out.close();
      fileOut.close();
      System.out.printf("Serialized data is saved in" + filepath);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Deserializes the next object in the given serialized file.
   * 
   * @throws IOException If the user is no longer in the database, or if the database fails to load.
   * @throws ClassNotFoundException If a superclass of the deserialized object has since been
   *         modified.
   */
  public static Object deserialize() throws IOException, ClassNotFoundException {
    Object deserialized = null;
    try {
      FileInputStream fileIn = new FileInputStream(filepath);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      deserialized = in.readObject();
      in.close();
      fileIn.close();
      return deserialized;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return deserialized;
  }
}
