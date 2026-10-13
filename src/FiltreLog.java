import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FiltreLog {

    public static void main(String[] args) {
        try {
            String paraula = "ERROR";

            if (args.length > 0) {
                paraula = args[0];
            }

            BufferedReader entradaDesdePare = new BufferedReader(new InputStreamReader(System.in));

            String linia;
            int comptador = 0;
            boolean textBuit = true;

            while ((linia = entradaDesdePare.readLine()) != null) {

                if (!linia.trim().isEmpty()) {
                    textBuit = false;
                }

                if (linia.contains(paraula)) {
                    comptador++;
                }
            }

            if (textBuit) {
                System.err.println("Error: Text buit");
                System.exit(1);
            }

            System.out.println(comptador);
            System.exit(0);

        } catch (IOException e) {
            System.err.println("Error en llegir els logs: " + e.getMessage());
            System.exit(1);
        }
    }
}