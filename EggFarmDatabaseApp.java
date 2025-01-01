import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class EggFarmDatabaseApp {
    public static void main(String[] args) {
        // Database credentials and URL
        String url = "jdbc:mysql://localhost:3306/EGG_PRODUCTION";
        String user = "root";
        String password = "12345678";

        // Arrays to store weekly data
        int[] HOUSE_A = new int[7];
        int[] HOUSE_B = new int[7];
        int[] HOUSE_C = new int[7];
        int[] HOUSE_D = new int[7];
        String[] DATE = new String[7];
        String[] DAY = new String[7];

        // Create Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database.");

            // SQL query for inserting data into the table
            String insertQuery = "INSERT INTO DAILY_EGGS (DAY, DATE, HOUSE_A, HOUSE_B, HOUSE_C, HOUSE_D, TOTAL, GRAND_TOTAL) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // Loop for 7 days to collect data for weekly totals
            for (int i = 0; i < 7; i++) {
                System.out.println("Day " + (i + 1) + " Data Entry");

                // Prompt user to enter the date
                System.out.print("Enter date (DD-MM-YYYY): ");
                DATE[i] = scanner.nextLine();

                // Prompt user to enter the day of the week
                System.out.print("Enter DAY: ");
                DAY[i] = scanner.nextLine();

                // Input values for each chicken house
                System.out.print("Enter number of trays for House A: ");
                HOUSE_A[i] = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter number of trays for House B: ");
                HOUSE_B[i] = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter number of trays for House C: ");
                HOUSE_C[i] = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter number of trays for House D: ");
                HOUSE_D[i] = Integer.parseInt(scanner.nextLine());

                System.out.println("Data recorded for " + DATE[i] + "\n");

                // Calculate the daily total and grand total
                int dailyTotal = HOUSE_A[i] + HOUSE_B[i] + HOUSE_C[i] + HOUSE_D[i];
                int grandTotal = dailyTotal; // You can modify this if a cumulative total is needed.

                // Insert the data into the database
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, DATE[i]);
                    pstmt.setInt(2, HOUSE_A[i]);
                    pstmt.setInt(3, HOUSE_B[i]);
                    pstmt.setInt(4, HOUSE_C[i]);
                    pstmt.setInt(5, HOUSE_D[i]);
                    pstmt.setString(6, DAY[i]);
                    pstmt.setInt(7, dailyTotal);
                    pstmt.setInt(8, grandTotal);

                    pstmt.executeUpdate();
                }
            }

            System.out.println("Data successfully inserted into the database.");

        } catch (SQLException e) {
            System.err.println("An error occurred while connecting to the database or inserting data:");
            e.printStackTrace();
        }

        scanner.close();
    }
}