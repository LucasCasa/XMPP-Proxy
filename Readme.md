Pasos a seguir para ejecutar el servidor:

Descargar el código fuente del repositorio de BitBucket
Ejecutar en la consola mvn package en el directorio raiz del proyecto
Ir a la carpeta target
Ejecutar java -jar XMPPProxy-1.0-SNAPSHOT.jar ( es necesario que este la carpeta dependencies.jars, En caso de mover el jar tambien es necesario mover dicha carpeta)
 
------------------------------------

Configuracion:

Para agregar host a los cuales poder conectarse en necesario agregarlos mediante el administrador. para esto seguir los siguientes pasos:

1.	nc (direccion donde esta el proxy) 42070
2.	LOGIN muffin muffin
.
3.	HOST (nombre) (ip)
.

Para mayor información sobre los comandos disponibles ver el informe.

 ---------------------------------

Changelog entrega 01/12/2016:

Se agrego descripcion del funcionamiento del proxy al informe
Se creo el README
Se agrego el PDF con la presentacion
Se agrego el binario (carpeta bin)
Se modifico el ABNF para que refleje correctamente el protocolo
Ahora el proxy se fija si la cantidad de bytes que leyo es -1 indicando que se cerro la conexion.
Ahora si durante la conexion se envian los datos por partes el proxy funciona de forma correcta.
Cuando se inicia la conexion con el servidor ya no se hace de forma bloqueante.
No se intenta escribir en un channel sin antes saber si estaba disponible o no ( cuando se iniciaba la comunicacion con el servidor)
Se eliminaron las lineas que intentaban conectarse al servidor xmpp cuando se iniciaba el proxy, ya que no se suponia que tenian que estar ahi
Se mejoro la inicializacion de los StringBuilder
Se agrego la codificacion cuando se creaba un String a partir de un bytebuffer