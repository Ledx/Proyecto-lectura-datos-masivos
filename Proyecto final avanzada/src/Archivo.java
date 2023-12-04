import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static java.lang.System.exit;

public class Archivo {

    public Archivo(String rutaE,String rutaS,String nombre){
        String r = new File("").getAbsolutePath() + "\\src";
        this.rutaE = r + rutaE + nombre;
        System.out.println("Archivo a procesar: " + this.rutaE);
        this.rutaS = rutaS;
        this.nombre = nombre;
        this.archivo = new File(this.rutaE);
        this.calcularLineas();
    }

    private String rutaE;
    private String rutaS;
    private int nLineas;
    public String nombre;

    private File archivo;

    public void calcularLineas(){

        Scanner lector = null;

        try {
            lector = new Scanner(archivo);
        } catch (FileNotFoundException e) {
            System.out.printf("El archivo a procesar no pudo ser localizado.");
            exit(-1);
        }

        int lineas = -1;

        while (lector.hasNextLine()) {
            lineas++;
            lector.nextLine();
        }

        lector.close();
        this.setnLineas(lineas);

    }

    public int getnLineas() {
        return nLineas;
    }

    public void setnLineas(int nLineas) {
        this.nLineas = nLineas;
    }

    public String getrutaS() {
        return rutaS;
    }

    public void setrutaS(String rutaS) {
        this.rutaS = rutaS;
    }
}
