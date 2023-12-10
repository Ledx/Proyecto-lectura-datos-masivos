import java.util.ArrayList;

public class Worker implements Runnable{


    private int id;
    private int inicio;
    private int fin;
    private CSV csv;
    private ArrayList<Integer> pedazos;

    public Worker(int inicio,int fin,int id,ArrayList<Integer> pedazos, CSV csv) {

        this.csv = csv;
        this.pedazos = pedazos;
        this.id = id;
        this.inicio = inicio;
        this.fin = fin;
    }
    @Override
    public void run() {

        csv.escribirNLineas(this.inicio,this.fin);
        //System.out.println("Soy un hilo " + csv.getarchivoSalida().getNombre());
        //csv.escribirLinea(csv.getcabeceraF());
        /*for(int i=0;i<this.pedazos.get(this.id);i++){
            System.out.println("Limites: "+i*this.pedazos.get(this.id)+" "+i*this.pedazos.get(this.id)+this.pedazos.get(this.id));
            //csv.escribirLinea(csv.leerLinea());
        }*/
        csv.terminarEscritura();
    }
}
