# Calculator-CORBA
Una calculadora distribuida que utiliza la arquitectura CORBA.
Consulte la carpeta raíz para ver la descripción del proyecto.

## Como compilar
### Lado del servidor
idlj Calc.idl
javac *.java Calculator/*.java

### Lado del Cliente
javac *.java Calculator/*.java

## Como correr el programa
Necesitaras tres terminales.

Terminal 1:
orbd -ORBInitialPort 1050 -ORBInitialHost localhost

Terminal 2:
java StartServer -ORBInitialPort 1050 -ORBInitialHost localhost

Terminal 3:
java StartClient -ORBInitialPort 1050 -ORBInitialHost localhost
