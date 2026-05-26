package database;

import models.*;
import serverTools.ConfigManager;

import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DatabaseManager {
    /**
     * Экземпляр класса {@code DatabaseManager}.
     */
    private static DatabaseManager instance;
    /**
     * Соединение с базой данных.
     */
    private final Connection connection;

    /**
     * Создает экземпляр {@code DatabaseManager}.
     */
    private DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(ConfigManager.dbUrl, ConfigManager.dbLogin, ConfigManager.dbPassword);
            initializeDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации БД: " + e.getMessage());
        }
    }

    /**
     * Единственный экземпляр менеджера базы данных.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    /**
     * Инициализация таблиц базы данных.
     */
    private void initializeDatabase() {
        try (InputStream is = getClass().getResourceAsStream("/database/init.sql");
             Scanner scanner = new Scanner(is);
             Statement statement = connection.createStatement()) {

            scanner.useDelimiter(";");
            while (scanner.hasNext()) {
                statement.execute(scanner.next());
            }
        } catch (Exception e) {
            System.err.println("Не удалось выполнить init.sql: " + e.getMessage());
        }
    }

    /**
     * Получаем актуальную коллекцию.
     */
    public ConcurrentLinkedDeque<Dragon> fetchAllDragons() throws SQLException {
        ConcurrentLinkedDeque<Dragon> collection = new ConcurrentLinkedDeque<>();
        String sql = "SELECT * FROM dragons";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                collection.add(mapRowToDragon(rs));
            }
        }
        return collection;
    }

    /**
     * Создаем экземпляр {@code Dragon}.
     */
    private Dragon mapRowToDragon(ResultSet rs) throws SQLException {
        // Простые поля
        long id = rs.getLong("id");
        long creatorId = rs.getLong("creator_id");
        String name = rs.getString("name");

        // Координаты (Coordinates)
        Coordinates coordinates = new Coordinates(
            rs.getFloat("coord_x"),
            rs.getDouble("coord_y")
        );

        java.util.Date creationDate = new java.util.Date(rs.getTimestamp("creation_date").getTime());
        long age = rs.getLong("age");

        Integer weight = rs.getInt("weight");
        if (rs.wasNull()) weight = null; // Проверка на NULL

        Boolean speaking = rs.getBoolean("speaking");

        Color color = null;
        String colorStr = rs.getString("color");
        if (colorStr != null) color = Color.valueOf(colorStr);

        // Убийца (Person)
        Person killer = null;
        String killerName = rs.getString("killer_name");

        if (killerName != null) {
            java.util.Date birthday = new java.util.Date(rs.getDate("killer_birthday").getTime());
            String passportId = rs.getString("killer_passport_id");

            Country nationality = null;
            String natStr = rs.getString("killer_nationality");
            if (natStr != null) nationality = Country.valueOf(natStr);

            // Локация (Location) внутри убийцы
            Location location = null;
            String locName = rs.getString("killer_loc_name");
            // Если имя локации есть, значит и сама локация была сохранена
            if (locName != null) {
                location = new Location(
                    rs.getInt("killer_loc_x"),
                    rs.getInt("killer_loc_y"),
                    rs.getInt("killer_loc_z"),
                    locName
                );
            }
            killer = new Person(killerName, birthday, passportId, nationality, location);
        }

        // Собираем все в объект
        Dragon dragon = new Dragon(id, name, coordinates, creationDate, age, weight, speaking, color, killer);
        dragon.setCreatorId(creatorId);

        return dragon;
    }
}