import java.io.*;
import java.io.FileNotFoundException;
import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {

        long tInicial = 0,tFinal = 0;
        FileReader fr = null;
        BufferedReader parametros = null;
        String rutaP = new File("").getAbsolutePath() + "\\src\\Configuracion\\parametros.txt";
        String rutaE, nombre, campos, registros, multiprocesamiento;
        Boolean filtradoH,filtradoV;

        try {
            fr=new FileReader(rutaP);
            parametros = new BufferedReader(fr);
        }
        catch(FileNotFoundException e) {

            System.out.println("El archivo de parametros no pudo ser localizado");
            exit(-1);
        }
        try {
            nombre = parametros.readLine();
            rutaE = parametros.readLine();
            campos = parametros.readLine();
            registros = parametros.readLine();
            multiprocesamiento = parametros.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CSV csv = new CSV(rutaE,nombre);
        System.out.println("El numero de lineas es: " + csv.archivoEntrada.getnLineas());
        tInicial = System.currentTimeMillis();



        tFinal = System.currentTimeMillis() - tInicial;


        System.out.println("El tiempo de procesamiento fue de: " + tFinal);

    }
}