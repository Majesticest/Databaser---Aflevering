import java.sql.SQLException;
import java.util.Scanner;
import org.example.Main;
import org.example.productBrowser;

public class menu {
    public static void profileMenu() throws SQLException {
        Scanner reader = new Scanner(System.in);
        while (Main.accountChoice==Main.selectUser()){
            int menuID = reader.nextInt();
            switch (menuID){
                case 1:
                    productBrowser.displayProducts();

            }
        }

    }
}
