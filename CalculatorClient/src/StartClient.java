/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zhaowei
 */
import Calculator.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.util.*;
 
public class StartClient {
    private static Calc calcObj;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // crear e inicializar el ORB
	    ORB orb = ORB.init(args, null);
            
            // Obtener el contexto de root naming 
	    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            
            // Utilizar NamingContextExt en lugar de NamingContext. Esto es 
            // parte del Servicio de Nomenclatura Interoperable. 
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            // resolver la referencia de objeto en Naming
	    calcObj = (Calc) CalcHelper.narrow(ncRef.resolve_str("Calculator"));

            while(true) {
                // preguntar por el input y leerlo
                System.out.println("------------------------------------------");
                System.out.println("Enter the parameters in this format [operator][sp][operand1][sp][operand2]."
                        + "\nFor example: + 1 2");
                Scanner c=new Scanner(System.in);
		String input = c.nextLine();
                
                // si el comando es exit, pedir al servidor que se apague
                if (input.toLowerCase().equals("exit")) {
                    calcObj.exit();
                    break;
                }
                
                // probar el input
                String[] inputParams = input.split(" ");
                if (inputParams.length != 3) {
                    System.out.println("Client Exception: Wrong number of parameters. Try again...");
                    continue;
                }
                int operatorCode;
                int operand1;
                int operand2;
                
                // establecer el tipo de operación
                if (inputParams[0].equals("+")) {
                    operatorCode = 1;
                }
                else if (inputParams[0].equals("-")) {
                    operatorCode = 2;
                }
                else if (inputParams[0].equals("*")) {
                    operatorCode = 3;
                }
                else if (inputParams[0].equals("/")) {
                    operatorCode = 4;
                }
                //operación nueva, potencias      
                else if (inputParams[0].equals("E")) {
                	operatorCode = 5;
                }
                else {
                    System.out.println("Client Exception: Un-recognized operation code. Try again...");
                    continue;
                }
                
                // Probar si los operandos de entrada son números enteros
                try {
                    operand1 = Integer.parseInt(inputParams[1]);
                    operand2 = Integer.parseInt(inputParams[2]);
                }
                catch (NumberFormatException e) {
                    System.out.println("Client Exception: Wrong number format. Try again...");
                    continue;
                }
                
                // probar si es dividido por 0
                if (operatorCode == 4 && operand2 == 0) {
                    System.out.println("Client Exception: Can't be divided by zero. Try again...");
                    continue;
                }
                
                // hacer el calculo y devolver el resultado 
		int result = calcObj.calculate(operatorCode, operand1, operand2);
                String resultDisplay = "";
                if (result == Integer.MAX_VALUE) {
                    resultDisplay = "There might be an Integer Overflow. Please try again...";
                }
                else if (result == Integer.MIN_VALUE) {
                    resultDisplay = "There might be an Integer Underflow. Please try again...";
                }
                else {
                    resultDisplay = String.valueOf(result);
                }
		System.out.println("The result is: " + resultDisplay);
            }
        }
        catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
