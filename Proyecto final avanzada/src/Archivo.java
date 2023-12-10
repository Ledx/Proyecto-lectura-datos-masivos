import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static java.lang.System.exit;

public class Archivo {

    public Archivo(String ruta,String nombre){
        String r = new File("").getAbsolutePath() + "\\src";
        this.ruta = r + ruta + nombre;
        //System.out.println("Archivo a procesar: " + this.ruta);
        this.nombre = nombre;
        this.archivo = new File(this.ruta);
        this.calcularLineas();
    }

    private String ruta;

    private int nLineas;
    private String nombre;
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

   public String getRutaE() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNombre(){
        return this.nombre;
    }
    public File getArchivo(){
        return this.archivo;
    }




}
