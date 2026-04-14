import javafx.application.Application;

public class Main {
    static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        Application.launch(mainWindow.getClass(), args);


        // Weitere Ideen:
        // - Verlauf aller Rechnungen bisher wo man aber auch alte Rechnungen wieder laden kann damit man sie verändern kann
        // - Wenn man die öffnende Klammer setzt, wird die schließende auch gleich gesetzt und der Cursor zwischen die beiden Klammern gepackt
        // - Konstanten Panel (Ein Panel wo alle wichtigen Konstanten drin stehen, die man dann wie eine Variable nutzen kann direkt (z.b. Pi, Gravitationskonstante g, Eulersche Zahl e))
        // - Die Möglichkeit einfach Zehnerpotenzen einzugeben
        // - vllt. Bracket Matching (Die Klammer die zu einer anderen gehört wird farblich hervorgehoben)

        // Weitere Operatoren: Binomialkoeffizient, Arcsinus, -cosinus und -tangens, logarithmus & ln
    }
}
