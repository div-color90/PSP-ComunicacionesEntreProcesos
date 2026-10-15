Nombre: Angee Carrión Ordóñez

Asignatura: Programación de servicios y procesos

# Ejercicio 1
### Clase Tuberia
El ejercicio 1 te pide ejecutar 2 comandos MS-DOS, cada uno en un proceso hijo con sus respectivos argumentos. 
La salida estándar del primero debe ser la entrada estándar del segundo. El proceso padre es el main de mi programa y sirve
para comunicar los 2 procesos hijos. 
El proceso padre lee lo que DIR envía del proceso1 y se lo escribe al proceso2 por FIND. Por último lo que produce el proceso2 
se lo envía al padre y lo muestra por pantalla.
En el pdf Tema1_Prog_Multiproceso.pdf página 15 se puede ver en la imagen cómo funciona la comunicación padre e hijo.

- DIR: lista el contenido de la carpeta.
- FIND: filtrar solo las líneas que contienen .gitignore.

### Explicación del código

Importamos todas las clases de entrada y salida y las excepciones sin hacerlo de una en una.
```java
import java.io.*;
```
Es la parte principal del programa la que ejecutamos con el Run.
```java
public static void main(String[] args)
```

Creación de los 2 procesos hijos: Utilizamos ProcessBuilder para crear los comandos y argumentos. Después con el método
start() creamos los procesos. Proceso1 ejecuta DIR y proceso2 ejecuta FIND.
/C indica que el intérprete de comandos debe ejecutar el comando y terminar.
```java
ProcessBuilder primerProceso = new ProcessBuilder("CMD", "/C", "DIR");
Process proceso1 = primerProceso.start();
ProcessBuilder segundoProceso = new ProcessBuilder("FIND", "\".gitignore\"");
Process proceso2 = segundoProceso.start();
```
Ponemos el código dentro del try así si falla algo podemos lanzar una excepción en el catch. El método start() y readLine() lanza una 
IOException y waitFor() lanza InterruptedException.
```java
    catch (InterruptedException e) {
        System.out.println(e.getMessage());
    } 
    catch (IOException e){
        System.out.println(e.getMessage());
        }
```
proceso1.getInputStream() el proceso padre lee los datos DIR que pasamos desde el proceso hijo.
proceso2.getOutputStream() el proceso padre escribe al proceso2 los datos leidos del proceso1.
InputStreamReader y OutputStreamWriter para poder manejar caracteres en lugar de bytes.
```java
BufferedReader br = new BufferedReader(new InputStreamReader(proceso1.getInputStream()));
BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(proceso2.getOutputStream()));
```


String linea guarda cada línea leída. readLine lee una línea de DIR y acaba cuando devuelve null, que significa que ha terminado y no tiene más datos.
Lee cada línea con readLine() y la escribe en el FIND con bw.write(linea) y usamos bw.newLine() para que FIND sepa cuando empieza una línea y acaba.
```java
String linea;
while ((linea = br.readLine()) != null) {
bw.write(linea);
bw.newLine();
}
```

Cerramos los flujos: br.close() porque ya ha terminado de leer de DIR y bw.close() para que FIND sepa cuando dejar de filtrar.
Si no cerramos el buffered FIND se quedaría esperando y no terminaría.
```java
br.close();
bw.close();
```

proceso2.getInputStream(): proceso2 le pasa por salida estandar a la entrada del proceso padre los datos ya filtrado por FIND.
Como dice en el enunciado mostramos solo los datos del segundo proceso hijo.
Usamos readLine() para leer línea por línea e imprime la variable linea2 por pantalla. Después 
cerramos el flujo. 
```java
 BufferedReader br2 = new BufferedReader(new InputStreamReader(proceso2.getInputStream()));
            String linea2;
            while ((linea2 = br2.readLine()) != null) {
                System.out.println(linea2);
            }
            br2.close();
```

Con el método waitFor() el proceso padre espera hasta que el proceso hijo acabe e imprimimos para avisar que ha terminado la ejecución.

```java
 proceso2.waitFor();
            System.out.println("Finalizada la ejecución ");
```

# Ejercicio 2
### Clase Lector
El ejercicio 2 te pide que escribas un número por pantalla y este deberá mostrar los 10 números siguientes al número recibido.
Nos pide crear la clase Lector que será el encargado de pedir el número al usuario por teclado y el proceso Escritor que se encargará
de recibir el número enviado e iterarlo 10 veces. Lector será quien muestre los 10 números al usuario por consola. 

### Explicación del código

Importamos las clases de entrada/salida y excepciones. Importamos la librería utilizada en la clase Lector para utilizar Entrada 
que será la que nos permite realizar la entrada por teclado.
```java
import org.iesalandalus.programacion.utilidades.Entrada;
import java.io.*;
```
El encargado de ejecutar nuestro programa.
```java
public static void main(String[] args)
```
Usamos try/catch al igual que en el ejercicio 1 para escribir nuestro código dentro de un espacio protegido y nos lance
excepciones si algo falla. El método start() y BufferedWriter y BufferedReader lanza IOExcepcion.
```java
try{...}
        catch (IOException e){
        System.out.println(e.getMessage());
        }
```


Escribimos los comando y argumentos del proceso. Después creamos el proceso con el método .start() en el sistema.
El start() va después del directory y redirectError por crea los procesos con los atributos ya configurados en el ProcessBuilder.
```java
 ProcessBuilder proceso = new ProcessBuilder("java", "org.example.Escritor");
Process procesoEscritor = proceso.start();
```
Establecemos el directorio de trabajo con el método .directory(). Tenemos que asegurarnos que dicho directorio existe porque este no la crea.
java busca los .class a partir del directorio de trabajo .
```java
proceso.directory(new File("C:\\Users\\ajco1\\OneDrive\\Documentos\\2DAM\\ProgramaciónProcesos\\ComunicaciónProcesos\\build\\classes\\java\\main"));
```
Hace que los errores del hijo salgan por la consola del padre. Lo usa para depurar si falla Escritor imprime por consola el error que ocurre. 
```java
proceso.redirectError(ProcessBuilder.Redirect.INHERIT);
```
Para utilizar la clase Entrada de nuestra librería importada. System.out.print solo muestra el mensaje y Entrada.entero() solo lee el número que escribimos.
```java
 System.out.print("Introduzca un número: ");
    int numero = Entrada.entero();
```

El proceso padre le escribe datos al procesoEscritor es decir al proceso hijo por la salida getOutputStream hacia la entrada estándar del hijo.
Con String.valueOf(numero) convertimos número int en un string porque bw.write envía texto. Para que sepa cuando empieza y acaba una línea usamos bw.newLine() y nos 
aseguramos siempre de cerrar el flujo para vaciar el buffer de modo que el número llega de verdad a Escritor y le indica que no van a llegar más datos. 
```java
BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(procesoEscritor.getOutputStream()));
bw.write(String.valueOf(numero));
bw.newLine();
bw.close();
```

Los datos escritos al procesoEscritor los pasa al proceso padre por getInputStream para que este los lea y acabe cuando readLine() devuelve un null.
La variable linea guarda el valor leido por el proceso padre y cerramos el flujo.
```java
BufferedReader br = new BufferedReader(new InputStreamReader(procesoEscritor.getInputStream()));
String linea;
            while ((linea = br.readLine()) != null) {
        System.out.println(linea);
            }
                    br.close();
```
El proceso padre espera a que el hijo termine y va al final de todo para no provocar un bloqueo. Y lanza la excepción InterruptedException.
```java
procesoEscritor.waitFor();
```
### Clase Escritor

Utilizamos System.in porque está conectado al getOutputStream del proceso padre Lector. 
Lo que lector escribe con bw.write es lo que recibe Escritor por System.in. System.in es el canal por donde le llegan los datos a Escritor y como llega en bytes
utilizamos new InputStreamReader que convierte esos bytes en caracteres. 

```java
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

```
Como readLine() devuelve un String es decir texto lo tenemos que convertir en un int, en un número. 
```java
int numero = Integer.parseInt(br.readLine());
```

Creamos un bucle con for para imprimir los 10 números siguientes. Cada println es una línera que el Lector leerá. 
```java
for (int i = 1; i <= 10; i++){
System.out.println(numero + i);
}
br.close();
```
