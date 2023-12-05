public class ProcesaCSV {

    private int CPUs;
    private int nHilos;

    public ProcesaCSV (){

        this.CPUs = Runtime.getRuntime().availableProcessors();
        this.nHilos = 4 * this.CPUs;
        System.out.println("\n Numero de CPUs: \t " + CPUs );
    }
}
