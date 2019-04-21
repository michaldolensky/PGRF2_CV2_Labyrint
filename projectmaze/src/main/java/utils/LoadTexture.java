package utils;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.io.InputStream;

public class LoadTexture {
    public static Texture load(String filePath) {
        Texture texture;
        InputStream is = LoadTexture.class.getResourceAsStream(filePath); // vzhledem k adresari res v projektu
        if (is == null) System.out.println("File not found");
        else try {
            texture = TextureIO.newTexture(is, true, "jpg");
            return texture;
        } catch (IOException e) {
            System.err.println("Chyba cteni souboru s texturou");
        }
        return null;
    }
}
