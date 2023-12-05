import java.util.ArrayList;

public class ProcesaCSV {

    private int CPUs;
    private int nHilos;

    public ProcesaCSV (String rutaE,String nombre, String camposF, String registrosF, Boolean hilos){

        this.CPUs = Runtime.getRuntime().availableProcessors();
        this.nHilos = 4 * this.CPUs;
        Archivo entrada = new Archivo(rutaE,nombre);
        int lineas = entrada.getnLineas();
        System.out.println("Numero de CPUs: " + CPUs );
        System.out.println("Numero de hilos: " + nHilos );
        System.out.println("Numero de lineas: "+ lineas);
        ArrayList<Integer> pedazos = calcularPedazo(lineas);
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
}
