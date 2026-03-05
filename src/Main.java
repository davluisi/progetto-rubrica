import javax.swing.SwingUtilities;
import ui.FinestraLogin;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FinestraLogin login = new FinestraLogin();
                login.setVisible(true);
            }
        });
    }
}