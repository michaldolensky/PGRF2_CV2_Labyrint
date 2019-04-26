import blocks.Teleporter;
import com.sun.xml.internal.bind.v2.model.core.MaybeElement;
import maze.Maze1;
import transforms.Point3D;

import java.io.*;

public class Test {
    public static void main(String[] args) {
        try {
            OutputStream file = new FileOutputStream("test.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                Maze1 cc = Maze1.forName("com.test.Test");
                System.out.println(cc);
                output.writeObject(cc);
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            // use buffering
            InputStream file = new FileInputStream("test.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                // deserialize the class
                Maze1 cc = (Maze1) input
                        .readObject();
                // display
                System.out.println("Recovered Class: " + cc);
            } finally {
                input.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
