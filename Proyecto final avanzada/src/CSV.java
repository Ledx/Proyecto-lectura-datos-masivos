import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class CSV {

    public Archivo archivoEntrada;

    public CSV(String rutaE,String nombre){

        this.archivoEntrada = new Archivo(rutaE,"",nombre);
    }

    public void leerLinea() {

    }

    public void escribirLinea(){

    }
}
