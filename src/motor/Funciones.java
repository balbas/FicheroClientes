package motor;

/**
 * Clase que contiene métodos con funciones para tratar Strings
 * @author jose
 */
public class Funciones {
    public static String Left(String text, int length) {
        return text.substring(0, length);
    }

    public static String Right(String text, int length) {
        return text.substring(text.length() - length, text.length());
    }
}
