import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcesaCSV {

    private int CPUs;
    private int nHilos;
    private String rutaE;
    private String nombre;
    private String camposF;
    private String registrosF;
    private ArrayList<Integer> pedazos;
    private Boolean multiprocesamiento;

    public ProcesaCSV (String rutaE,String nombre, String camposF, String registrosF, Boolean multiprocesamiento){

        this.CPUs = Runtime.getRuntime().availableProcessors();
        if (multiprocesamiento)
            this.nHilos = 4 * this.CPUs;
        else
            this.nHilos = 1;

        Archivo entrada = new Archivo(rutaE,nombre);
        int lineas = entrada.getnLineas();
        System.out.println("Numero de CPUs: " + CPUs );
        System.out.println("Numero de hilos: " + nHilos );
        System.out.println("Numero de lineas: "+ lineas);
        this.pedazos = calcularPedazo(lineas);
        this.rutaE = rutaE;
        this.nombre = nombre;
        this.camposF = camposF;
        this.registrosF = registrosF;
        this.multiprocesamiento = multiprocesamiento;

    }

    public void procesarArchivo(){

        ExecutorService hilos = Executors.newFixedThreadPool(nHilos);

        //Thread[] hilos = new Thread[this.pedazos.size()];
        CSV csv = new CSV(this.rutaE, this.nombre, this.camposF, this.registrosF,-1);

        for (int i = 0; i < this.pedazos.size(); i++) {

            hilos.execute(new Worker(1+i * this.pedazos.get(0),1+i*this.pedazos.get(0)+this.pedazos.get(i),i,this.pedazos,new CSV(this.rutaE, this.nombre, this.camposF, this.registrosF,i)));
            //hilos[i]= new Thread( new Worker(i,this.pedazos,new CSV(this.rutaE, this.nombre, this.camposF, this.registrosF,i)));

            //hilos[i].start();
        }
        /*for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
            }
        }*/

        hilos.shutdown();
        while (!hilos.isTerminated()) {
        }
        File dir = new File(csv.getRutaDS());
        PrintWriter pw;
        try {
            //pw = new PrintWriter(rutaE + csv.getarchivoSalida().getNombre());
            pw = new PrintWriter("C:\\Users\\colmi\\Documents\\UNAM III Especialidad computo alto rendimiento\\1º Semestre\\Programacion avanzada\\Proyecto-lectura-datos-masivos\\Proyecto final avanzada\\src\\Datos\\" + csv.getarchivoSalida().getNombre());
            pw.println(csv.getcabeceraF());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String[] nombreArchivos = dir.list();
        for (String nombreArchivo : nombreArchivos) {
            File f = new File(dir, nombreArchivo);
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String linea;
            try {
                linea = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (linea != null) {
                pw.println(linea);
                try {
                    linea = br.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            pw.flush();

        }
        System.out.println("Comenzando limpieza:");
        this.borrarTemporales(dir);
    }



    private ArrayList calcularPedazo(int lineas){

        ArrayList<Integer> pedazos = new ArrayList<Integer>();

        if (lineas % this.nHilos  == 0){

            for(int i=0;i<nHilos;i++){
                pedazos.add(lineas / this.nHilos);
            }
        }else{
            for(int i=0;i<nHilos-1;i++){
                pedazos.add(lineas / this.nHilos);
            }
            pedazos.add( lineas - ( (lineas / this.nHilos) * (this.nHilos-1) ) );
        }

        return pedazos;
    }

    public void borrarTemporales(File directorio) {

        File[] contenido = directorio.listFiles();
        if (contenido != null) {
            for (File temp : contenido) {
                borrarTemporales(temp);
            }
        }
        if (!directorio.delete()){
            System.out.println("El archivo "+directorio.getName()+" no pudo ser eliminado");
        }

    }
}
