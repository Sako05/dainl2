import java.io.FileInputStream;
import java.sql.*;
import java.util.*;
import models.*;

public class Repository {

    private Properties p = new Properties();

    public Repository(){
        setupDatabaseConnection();
    }

    private void setupDatabaseConnection(){
        try{
            p.load(new FileInputStream("src/DBsettings.properties"));
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Customer login(String userID, String password){
        Customer c = null;
        String loginQuery = "SELECT * FROM customer WHERE username = ? AND password = ?";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(loginQuery)) {

            stmt.setString(1, userID);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int ID = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("surename");
                String address = rs.getString("address");
                String pass = rs.getString("password");

                c = new Customer(ID, firstName, lastName, address, pass);
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("e.mess "+e.getMessage());
            System.out.println("Could not login user" +userID );
        }
        return c;

    }


    public Map<Integer, Shoe> getShoes() {
        Map<Integer, Shoe> shoeMap = new HashMap<>();

        String shoeQuery = "SELECT * FROM shoe " +
                "INNER JOIN shoetype ON shoe.shoeTypeID = shoetype.ID " +
                "INNER JOIN color ON shoe.colorID = color.ID " +
                "INNER JOIN brand ON shoe.brandID = brand.ID";
        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(shoeQuery)) {
            while (rs.next()) {
                shoeMap.put(rs.getInt("ID"),
                        new Shoe(rs.getString("name"),
                                rs.getString("size"),
                                rs.getString("colorName"),
                                rs.getString("brandName"),
                                rs.getInt("price"),
                                rs.getInt("quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoeMap;
    }

    public void addToCart(int input_shoeID, int input_customerID, int input_orderID){
        String query = "CALL addToCart(?, ?, ?)";

        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             CallableStatement stmt = con.prepareCall(query)){
            stmt.setInt(1, input_shoeID);
            stmt.setInt(2, input_customerID);
            stmt.setInt(3, input_orderID);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<Integer> onlyOrderId() {
        List<Integer> OrderIDList = new ArrayList<>();
        String query = "SELECT * FROM orders;";
        try (Connection con = DriverManager.getConnection(p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                OrderIDList.add(rs.getInt("ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OrderIDList;
    }
}
