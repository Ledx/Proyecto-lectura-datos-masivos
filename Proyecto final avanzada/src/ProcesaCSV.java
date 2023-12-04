public class ProcesaCSV {

    private int CPUs;
    private int nHilos;

    public ProcesaCSV (){

        this.CPUs = Runtime.getRuntime().availableProcessors();
        System.out.println("\n Numero de CPUs: \t " + CPUs );
    }
}
