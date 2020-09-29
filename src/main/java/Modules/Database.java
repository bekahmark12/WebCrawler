package Modules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {
    static String databaseUrl = "jdbc:mysql://localhost:3306/webcrawler?useSSL=false";
    static String user = "root";
    static String password = "horses12";

    public static String writeToDatabase(String url, String content) {
        String sql = "INSERT INTO websites (url, content) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(databaseUrl, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, url);
            pst.setString(2, content);
            pst.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return sql;
    }

    public static HashSet<String> returnURL() {
        HashSet<String> urls = new HashSet<>();
        String sql = "Select * from websites";
        try (Connection con = DriverManager.getConnection(databaseUrl, user, password);
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while(rs.next()) {
                String url = rs.getString(2);
                urls.add(url);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return urls;
    }

    public static void search(String searchString) {

        String sql = "SELECT * FROM websites";

        try (Connection con = DriverManager.getConnection(databaseUrl, user, password);
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery();) {

            HashMap<String, String> dataMap = new HashMap<>();

            while (rs.next()) {
                String url = rs.getString(2);
                String content = rs.getString(3);
                dataMap.put(url, content);
            }

            Map<Integer, String> output = new TreeMap<>(Collections.reverseOrder());
            //Prepare the result
            for (String k : dataMap.keySet()) {
                String checkContent = dataMap.get(k);
                Pattern p = Pattern.compile(searchString.toLowerCase());
                Matcher m = p.matcher(checkContent.toLowerCase());
                int numOfHits = 1;
                while (m.find()) {
                    numOfHits++;
                }
                output.put(numOfHits, k);
            }

            //Output the result
            for (Map.Entry<Integer, String> entry : output.entrySet()) {

                Integer key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key + " hits => " + value);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
