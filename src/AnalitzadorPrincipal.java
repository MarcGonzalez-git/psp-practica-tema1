import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AnalitzadorPrincipal {

    void main() {
        try {
            String classpath = System.getProperty("java.class.path");

            ProcessBuilder pbError = new ProcessBuilder("java", "-cp", classpath, "FiltreLog", "ERROR");
            ProcessBuilder pbWarning = new ProcessBuilder("java", "-cp", classpath, "FiltreLog", "WARNING");

            pbError.redirectError(new File("errors_filtre_A.log"));
            pbWarning.redirectError(new File("errors_filtre_B.log"));

            Process procesError = pbError.start();
            Process procesWarning = pbWarning.start();

            String logs = "2026-09-30 INFO Inici\n"
                        + "2026-09-30 ERROR Fallada 1\n"
                        + "2026-09-30 WARNING Memoria baixa 1\n"
                        + "2026-09-30 ERROR Fallada 2\n"
                        + "2026-09-30 WARNING Memoria baixa 2\n"
                        + "2026-09-30 INFO Connexio establerta\n"
                        + "2026-09-30 WARNING Memoria baixa 3\n"
                        + "2026-09-30 ERROR Fallada 3\n"
                        + "2026-09-30 WARNING Memoria baixa 4\n"
                        + "2026-09-30 WARNING Memoria baixa 5\n";

            // envia a primer fill
            BufferedWriter sortidaCapAlFillError = new BufferedWriter(new OutputStreamWriter(procesError.getOutputStream()));

            sortidaCapAlFillError.write(logs);
            sortidaCapAlFillError.flush();
            sortidaCapAlFillError.close();

            // envia a segon fill
            BufferedWriter sortidaCapAlFillWarning = new BufferedWriter(new OutputStreamWriter(procesWarning.getOutputStream()));

            sortidaCapAlFillWarning.write(logs);
            sortidaCapAlFillWarning.flush();
            sortidaCapAlFillWarning.close();

            // llegeix resultat primer fill
            BufferedReader entradaDesdeError = new BufferedReader(new InputStreamReader(procesError.getInputStream()));

            String resultatError = entradaDesdeError.readLine();

            // llegeix resultat segon fill
            BufferedReader entradaDesdeWarning = new BufferedReader(new InputStreamReader(procesWarning.getInputStream()));

            String resultatWarning = entradaDesdeWarning.readLine();

            procesError.waitFor();
            procesWarning.waitFor();

            int exitCodeError = procesError.exitValue();
            int exitCodeWarning = procesWarning.exitValue();

            int exitCode = 0;

            if (exitCodeError != 0 || exitCodeWarning != 0) {
                exitCode = 1;
            }

            System.out.println("RESULTAT: ERRORS=" + resultatError + " | WARNINGS=" + resultatWarning + " | EXIT_CODE=" + exitCode);

            entradaDesdeError.close();
            entradaDesdeWarning.close();

        } catch (IOException | InterruptedException e) {
            System.err.println("Error en executar els processos: " + e.getMessage());

            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }
}