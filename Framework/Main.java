import javax.servlet.http.HttpSession;
import util.Util;

public class Main {
    public void processSession(HttpSession session) {
        try {
            String testlist = (String) session.getAttribute("isConnected");
            // Faites quelque chose avec la session...

            Util util = new Util();
            // Utilisez la session et faites quelque chose avec util...
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Vous pouvez également appeler processSession() directement ici
        // Si vous avez besoin d'une session simulée pour les tests, vous pouvez créer une HttpSession factice
        // sinon, vous devrez appeler processSession() depuis un contexte servlet réel
        HttpSession fakeSession = null; // Remplacez null par une session factice si nécessaire
        Main main = new Main();
        main.processSession(fakeSession);
    }

}
