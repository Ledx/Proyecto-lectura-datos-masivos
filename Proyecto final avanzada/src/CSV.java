import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

public class CSV {

    public Archivo archivoEntrada;
    public Archivo archivoSalida;
    public String cabecera;
    private  FileReader fr;
    private BufferedReader extractor_linea;

    private  FileWriter fw;
    private BufferedWriter inyector_linea;
    private Boolean fh;
    private Boolean fv;
    private String filtroHorizontal;

    private String filtroVertical;

    public CSV(String rutaE,String nombre, String camposF, String registrosF, Boolean hilos,int id){

        this.archivoEntrada = new Archivo(rutaE,nombre);
        String rutacsv = this.archivoEntrada.getRutaE();
        this.inicializarLector(rutacsv);

        String r = new File("").getAbsolutePath() + "\\src";
        String nombreSalida;
        r = r + rutaE;
        this.crearDirectorioSalida(r);
        try {
            nombreSalida = this.nombrarArchivoSalida(nombre,String.valueOf(id));
            File f = new File(r+"" + "temp\\"  + nombreSalida);

            if (f.createNewFile()) {
                this.archivoSalida = new Archivo(rutaE +"" + "temp\\",nombreSalida);
            }
        } catch (IOException e) {
            System.out.println("El archivo de salida no pudo ser creado.");
            e.printStackTrace();
        }
        rutacsv = this.archivoSalida.getRutaE();
        this.inicializarEscritor(rutacsv);


        this.extraerCabecera();
        this.fh = this.extraerFiltroH(camposF);
        if (this.fh.equals(Boolean.TRUE)) {
            this.fh = this.validarFiltroH(camposF);
            this.filtroHorizontal = camposF;
        }
        fv = this.extraerFiltroV(registrosF);
        if (fv.equals(Boolean.TRUE)) {
            fv = this.validarFiltroV(registrosF);
            this.filtroVertical = registrosF;
        }
        escribirLinea( this.cabecera );
        String prueba = leerLinea();
        System.out.println(prueba);

    }

    public Boolean getFh(){
        return this.fh;
    }

    public void inicializarLector(String rutacsv){
        try {
            fr=new FileReader(rutacsv);
            extractor_linea = new BufferedReader(fr);
        }
        catch(FileNotFoundException e) {

            System.out.println("El archivo de entrada no pudo ser localizado.");
            exit(-1);
        }
    }

    public void inicializarEscritor(String rutacsv){
        try {
            fw=new FileWriter(rutacsv);
            inyector_linea = new BufferedWriter(fw);
        }
        catch (IOException e) {
            System.out.println("El archivo de salida no pudo ser creado.");
            exit(-1);
            throw new RuntimeException(e);
        }
    }

    private void extraerCabecera(){

        try {
            cabecera = extractor_linea.readLine();
            System.out.println("La cabecera del archivo sin filtrar es: " + cabecera);

        } catch (IOException e) {
            System.out.println("El archivo de entrada no pudo ser leido.");
            exit(-2);
            throw new RuntimeException(e);
        }

    }

    public String leerLinea() {

        String linea = "";

        try {
            linea = extractor_linea.readLine();

            if (fv.equals(Boolean.TRUE))
                linea = filtrarRegistros(linea);
            if (fh.equals(Boolean.TRUE) && !linea.equals(""))
                linea = filtrarCampos(linea);

        } catch (IOException e) {
            System.out.println("El archivo de entrada no pudo ser leido.");
            exit(-2);
            throw new RuntimeException(e);
        }

        return linea;

    }

    public void leerLinea(int n) {

    }

    public void escribirLinea(String linea){

        try {
            inyector_linea.write(linea);

        } catch (IOException e) {
            System.out.println("El archivo de salida no pudo ser abierto.");
            exit(-2);
            throw new RuntimeException(e);
        }
    }

    public Boolean extraerFiltroH(String campos){

        Boolean filtradoH;

        if (campos.equals("*")) {
            filtradoH = Boolean.FALSE;
            System.out.println("No se ha seleccionado ningun campo para filtrar, se mostraran todos.");
        }
        else {
            filtradoH = Boolean.TRUE;
            System.out.println("Campos establecidos para filtrar: " + campos);
        }

        return filtradoH;
    }

    public Boolean extraerFiltroV(String registros){

        Boolean filtradoV;

        if (registros.equals("*")) {
            filtradoV = Boolean.FALSE;
            System.out.println("No se ha seleccionado ningun registro para filtrar, se mostraran todos.");
        }
        else {
            filtradoV = Boolean.TRUE;
            System.out.println("Criterio para filtrar registros: " + registros);
        }

        return filtradoV;
    }

    public Boolean validarFiltroH(String campos){

        Boolean filtro_valido= Boolean.FALSE;
        String[] camposSeparados = campos.split(",");
        for (int i=0 ; i< camposSeparados.length; i++){
            if (!cabecera.contains(camposSeparados[i])){
                return filtro_valido;
            }
        }
        filtro_valido= Boolean.TRUE;

        return filtro_valido;
    }

    public Boolean validarFiltroV(String registros){

        Boolean filtro_valido= Boolean.FALSE;
        String campo = registros.split(this.determinarToken(registros))[0];
        if (validarFiltroH(campo))
            filtro_valido= Boolean.TRUE;

        return filtro_valido;
    }

    public String filtrarCampos(String linea){

        String cadenaF = "";
        Map<String, String> lineaDiccionario = new HashMap<String, String>();
        String[] lineaPartida = linea.split(",");
        String[] cabeceraPartida = this.cabecera.split(",");
        String[] filtroHorizontalPartido = this.filtroHorizontal.split(",");
        for (int i=0 ; i< cabeceraPartida.length; i++){
            lineaDiccionario.put(cabeceraPartida[i], lineaPartida[i]);
        }
        for (int i=0 ; i< filtroHorizontalPartido.length; i++){
            cadenaF += lineaDiccionario.get(filtroHorizontalPartido[i]) + ',' ;
        }
        cadenaF = cadenaF.substring(0,cadenaF.length()-1);
        return cadenaF;
    }

    public String filtrarRegistros(String linea){

        Map<String, String> lineaDiccionario = new HashMap<String, String>();
        String[] lineaPartida = linea.split(",");
        String[] cabeceraPartida = this.cabecera.split(",");
        for (int i=0 ; i< cabeceraPartida.length; i++){
            lineaDiccionario.put(cabeceraPartida[i], lineaPartida[i]);
        }
        if (!determinarCondicion(lineaDiccionario))
            linea = "";
        return linea;
    }

    public boolean determinarCondicion(Map linea){

        String operacion = this.determinarToken(this.filtroVertical);
        boolean valido = false;

        String campo = this.filtroVertical.split(operacion)[0];
        String valor = this.filtroVertical.split(operacion)[1];

        if(operacion.equals("=")){
            valido = valor.equals(linea.get(campo));
        }else if(operacion.equals(">")){
            if (Float.valueOf(linea.get(campo).toString()) > Float.valueOf(valor))
                valido = true;
        }else if(operacion.equals("<")) {
            if (Float.valueOf(linea.get(campo).toString())  < Float.valueOf(valor))
                valido = true;
        }

        return valido;
    }

    public String determinarToken(String linea){

        String token = "";
        if(linea.contains("=")){
            token = "=";
        }else if(linea.contains(">")){
            token = ">";
        }else if(linea.contains("<")) {
            token = "<";
        }

        return token;
    }

    public void crearDirectorioSalida(String ruta){

        try {

            Path path = Paths.get(ruta + "\\temp");
            Files.createDirectories(path);

        } catch (IOException e) {

            System.err.println("El directorio temporal no pudo ser creado." + e.getMessage());
            throw new RuntimeException(e);

        }
    }

    public String nombrarArchivoSalida(String nombre, String id){

        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date tiempo = new Date();
        String hora = dateFormat.format(tiempo);
        dateFormat = new SimpleDateFormat("mm");
        Date minuto = new Date();

        String dia = Integer.toString(c.get(Calendar.DATE));
        if (dia.length() == 1)
            dia = '0' + dia;
        String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        String anio = Integer.toString(c.get(Calendar.YEAR));
        //System.out.println("Archivo de salida: "+ this.nombreE.substring(0,this.nombreE.length()-4) + "_filtered(" + anio+ mes+ dia + '_' + hora + dateFormat.format(minuto) + ")" + this.nombreE.substring(this.nombreE.length()-4));

        return nombre.substring(0,nombre.length()-4) + "_filtered(" + anio+ mes+ dia + '_' + hora + dateFormat.format(minuto) + ")"+ id + nombre.substring(nombre.length()-4 );

    }

}
