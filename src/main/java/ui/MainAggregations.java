package ui;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.bson.types.ObjectId;
import services.AggregationsServices;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainAggregations {

    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        AggregationsServices serv = container.select(AggregationsServices.class).get();


        int opcion = 0;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Seleccione:" + "\n" + "1. Exercise A" + "\n" + "2. Exercise B"
                    + "\n" + "3. Exercise C" + "\n" + "4. Exercise D"
                    + "\n" + "5. Exercise E" + "\n" + "6. Exercise F"
                    + "\n" + "7. Exercise G" + "\n" + "8. Exercise H"
                    + "\n" + "9. Exercise I" + "\n" + "10. Exercise J"
                    + "\n" + "11. Exercise K" + "\n" + "12. Exercise L"
                    + "\n" + "13. Exercise M" + "\n" + "14. Salir");

            System.out.println("Introduce un número");

            try {
                opcion = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Introduce un número");

                opcion = sc.nextInt();
            }


            switch (opcion) {
                case 1:
                    serv.ExA().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 2:
                    sc.nextLine();
                    System.out.println("Introduce un id");
                    String id = sc.nextLine();
                    serv.ExB(new ObjectId(id)).peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 3:
                    serv.ExC().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 4:
                    serv.ExD().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 5:
                    serv.ExE().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 6:
                    serv.ExF().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 7:
                    sc.nextLine();
                    System.out.println("Introduce un id");
                    String id2 = sc.nextLine();
                    serv.ExG(new ObjectId(id2)).peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 8:
                    serv.ExH().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 9:
                    serv.ExI().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 10:
                    serv.ExJ().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 11:
                    serv.ExK().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 12:
                    serv.ExL().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 13:
                    serv.ExM().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 14:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Introduce una opcion valida");
            }

        } while (opcion != 14);
    }
}
