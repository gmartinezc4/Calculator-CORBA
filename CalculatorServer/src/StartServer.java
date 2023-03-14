/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zhaowei
 */
import Calculator.Calc;
import Calculator.CalcHelper;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
 
public class StartServer {
 
    public static void main(String args[]) {
        try{
            // crear e inicializar el ORB
            ORB orb = ORB.init(args, null);  
            
            // obtener referencia a rootpoa y activar el POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
 
            // crear servidor y registrarlo en el ORB
            CalcObject calcObj = new CalcObject();
            calcObj.setORB(orb); 
 
            // obtener la referencia del objeto del servidor
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calcObj);
            Calc href = CalcHelper.narrow(ref);
 
            // Obtener el contexto de root naming 
            // NameService invoca el name service
            org.omg.CORBA.Object nsRef =  orb.resolve_initial_references("NameService");
            
            // Utilice NamingContextExt, que es parte de Interoperable
            // Especificación del Servicio de Nombres (INS).
            NamingContextExt ncRef = NamingContextExtHelper.narrow(nsRef);
 
            // vincular la referencia de objeto en la denominación
            NameComponent path[] = ncRef.to_name("Calculator");
            ncRef.rebind(path, href);
 
            System.out.println("CalculatorServer is listening...");
 
            // esperar las invocaciones de los clientes
            orb.run();
            System.out.println("I am out");
        } 
        catch (Exception e) {
            System.err.println("Server Error: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
