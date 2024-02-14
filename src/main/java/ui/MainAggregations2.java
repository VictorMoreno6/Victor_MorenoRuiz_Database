package ui;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.bson.types.ObjectId;
import services.AggregationsServices;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.mongodb.client.model.Accumulators.addToSet;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;

public class MainAggregations2 {
    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        AggregationsServices serv = container.select(AggregationsServices.class).get();


        int opcion = 0;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Seleccione:" + "\n" + "1. Get the name of the characters of one specie"
                    + "\n" + "2. Get the name and the status of the character who's origin name is abadango"
                    + "\n" + "3. Get the name of the character who appear in the most episodes"
                    + "\n" + "4. Get the name of the characters of one episode"
                    + "\n" + "5. Get the location and image of one character who appear in X episode and is from Y species"
                    + "\n" + "6. Salir");

            System.out.println("Introduce un número");

            try {
                opcion = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Introduce un número");

                opcion = sc.nextInt();
            }

            switch (opcion) {
                case 1:
                    //Human, Animal, Alien, Robot
                    sc.nextLine();
                    System.out.println("Introduce una especie");
                    String specie = sc.nextLine();
                    serv.Ex1part2(specie).peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 2:
                    serv.Ex2part2().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 3:
                    serv.Ex3part2().peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 4:
                    sc.nextLine();
                    System.out.println("Introduce el numero de un episodio");
                    String id = sc.nextLine();
                    serv.Ex4part2(Integer.parseInt(id)).peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 5:
                    //
                    sc.nextLine();
                    System.out.println("Introduce el numero de un episodio");
                    String id2 = sc.nextLine();
                    System.out.println("Introduce una especie");
                    String specie2 = sc.nextLine();
                    serv.Ex5part2(Integer.parseInt(id2), specie2).peekLeft(error -> System.out.println("Error: " + error))
                            .peek(rightValue -> System.out.println("Resultado: \n " + rightValue));
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Introduce una opcion valida");
            }

        } while (opcion != 14);
    }
}
