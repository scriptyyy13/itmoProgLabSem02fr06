package database;

import models.*;
import serverTools.ConfigManager;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * Атомарное добавление дракона в БД.
     *
     * @param dragon    объект для сохранения
     * @param creatorId ID создателя объекта
     * @return сгенерированный базой данных ID, либо -1 в случае ошибки.
     */
    public long insertDragon(Dragon dragon, long creatorId) {
        String sql = "INSERT INTO dragons (" +
                "creator_id, name, coord_x, coord_y, age, weight, speaking, color, " +
                "killer_name, killer_birthday, killer_passport_id, killer_nationality, " +
                "killer_loc_x, killer_loc_y, killer_loc_z, killer_loc_name" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, creation_date;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, creatorId);
            pstmt.setString(2, dragon.getName());
            pstmt.setFloat(3, dragon.getCoordinates().getX());
            pstmt.setDouble(4, dragon.getCoordinates().getY());
            pstmt.setLong(5, dragon.getAge());

            if (dragon.getWeight() != null) pstmt.setInt(6, dragon.getWeight());
            else pstmt.setNull(6, Types.INTEGER);

            pstmt.setBoolean(7, dragon.getSpeaking());

            if (dragon.getColor() != null) pstmt.setString(8, dragon.getColor().name());
            else pstmt.setNull(8, Types.VARCHAR);

            Person killer = dragon.getKiller();
            if (killer != null && killer.getName() != null) {
                pstmt.setString(9, killer.getName());
                pstmt.setDate(10, new java.sql.Date(killer.getBirthday().getTime()));
                pstmt.setString(11, killer.getPassportID());

                if (killer.getNationality() != null) pstmt.setString(12, killer.getNationality().name());
                else pstmt.setNull(12, Types.VARCHAR);

                Location loc = killer.getLocation();
                if (loc != null && !loc.isEmpty()) {
                    pstmt.setInt(13, loc.getX());
                    pstmt.setInt(14, loc.getY());
                    pstmt.setInt(15, loc.getZ());
                    pstmt.setString(16, loc.getName());
                } else {
                    pstmt.setNull(13, Types.INTEGER);
                    pstmt.setNull(14, Types.INTEGER);
                    pstmt.setNull(15, Types.INTEGER);
                    pstmt.setNull(16, Types.VARCHAR);
                }
            } else {
                for (int i = 9; i <= 16; i++) {
                    if (i == 13 || i == 14 || i == 15) pstmt.setNull(i, Types.INTEGER);
                    else pstmt.setNull(i, Types.VARCHAR);
                }
                pstmt.setNull(10, Types.DATE);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Обновляем дату создания прямо из БД, чтобы все было синхронно
                    dragon.setCreationDate(new java.util.Date(rs.getTimestamp("creation_date").getTime()));
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка БД при insertDragon: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Атомарное удаление дракона из БД по ID и ID создателя (проверка прав).
     *
     * @param id        ID удаляемого дракона
     * @param creatorId ID пользователя, инициировавшего удаление
     * @return true, если строка удалена, false если объект не найден или нет прав
     */
    public boolean deleteDragon(long id, long creatorId) {
        String sql = "DELETE FROM dragons WHERE id = ? AND creator_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, creatorId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка БД при deleteDragon: " + e.getMessage());
            return false;
        }
    }

    /**
     * Атомарное обновление дракона в БД.
     *
     * @param id        ID обновляемого дракона
     * @param dragon    Новые данные дракона
     * @param creatorId ID пользователя, инициировавшего обновление
     * @return true, если обновление прошло успешно
     */
    public boolean updateDragon(long id, Dragon dragon, long creatorId) {
        String sql = "UPDATE dragons SET " +
                "name = ?, coord_x = ?, coord_y = ?, age = ?, weight = ?, speaking = ?, color = ?, " +
                "killer_name = ?, killer_birthday = ?, killer_passport_id = ?, killer_nationality = ?, " +
                "killer_loc_x = ?, killer_loc_y = ?, killer_loc_z = ?, killer_loc_name = ? " +
                "WHERE id = ? AND creator_id = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dragon.getName());
            pstmt.setFloat(2, dragon.getCoordinates().getX());
            pstmt.setDouble(3, dragon.getCoordinates().getY());
            pstmt.setLong(4, dragon.getAge());

            if (dragon.getWeight() != null) pstmt.setInt(5, dragon.getWeight());
            else pstmt.setNull(5, Types.INTEGER);

            pstmt.setBoolean(6, dragon.getSpeaking());

            if (dragon.getColor() != null) pstmt.setString(7, dragon.getColor().name());
            else pstmt.setNull(7, Types.VARCHAR);

            Person killer = dragon.getKiller();
            if (killer != null && killer.getName() != null) {
                pstmt.setString(8, killer.getName());
                pstmt.setDate(9, new java.sql.Date(killer.getBirthday().getTime()));
                pstmt.setString(10, killer.getPassportID());

                if (killer.getNationality() != null) pstmt.setString(11, killer.getNationality().name());
                else pstmt.setNull(11, Types.VARCHAR);

                Location loc = killer.getLocation();
                if (loc != null && !loc.isEmpty()) {
                    pstmt.setInt(12, loc.getX());
                    pstmt.setInt(13, loc.getY());
                    pstmt.setInt(14, loc.getZ());
                    pstmt.setString(15, loc.getName());
                } else {
                    pstmt.setNull(12, Types.INTEGER);
                    pstmt.setNull(13, Types.INTEGER);
                    pstmt.setNull(14, Types.INTEGER);
                    pstmt.setNull(15, Types.VARCHAR);
                }
            } else {
                for (int i = 8; i <= 15; i++) {
                    if (i == 12 || i == 13 || i == 14) pstmt.setNull(i, Types.INTEGER);
                    else pstmt.setNull(i, Types.VARCHAR);
                }
                pstmt.setNull(9, Types.DATE);
            }

            pstmt.setLong(16, id);
            pstmt.setLong(17, creatorId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка БД при updateDragon: " + e.getMessage());
            return false;
        }
    }

    /**
     * Очистка всех объектов в БД, принадлежащих конкретному пользователю.
     *
     * @param creatorId ID пользователя
     * @return true, если транзакция прошла успешно
     */
    public boolean clearDragons(long creatorId) {
        String sql = "DELETE FROM dragons WHERE creator_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, creatorId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Ошибка БД при clearDragons: " + e.getMessage());
            return false;
        }
    }

    /**
     * Хэширует пароль пользователя с использованием алгоритма SHA-224.
     * @param password открытый пароль
     * @return строка хэша в hex-формате
     */
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Критическая ошибка: алгоритм SHA-224 не найден!", e);
        }
    }

    /**
     * Регистрирует нового пользователя в базе данных.
     * @param login имя пользователя
     * @param password открытый пароль (будет захэширован)
     * @return сгенерированный ID пользователя, или -1 если логин уже занят
     */
    public long registerUser(String login, String password) {
        String sql = "INSERT INTO users (login, password_hash) VALUES (?, ?) RETURNING id;";
        String hashedPassword = hashPassword(password);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, hashedPassword);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        } catch (SQLException e) {
            // Код ошибки бд при нарушении уникальности — 23505
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Попытка регистрации существующего логина: " + login);
            } else {
                System.err.println("Ошибка при регистрации пользователя: " + e.getMessage());
            }
        }
        return -1;
    }

    /**
     * Проверяет учетные данные пользователя и возвращает его ID.
     * @param login имя пользователя
     * @param password открытый пароль
     * @return ID пользователя из базы, или -1 если данные неверны
     */
    public long validateUser(String login, String password) {
        String sql = "SELECT id, password_hash FROM users WHERE login = ?;";
        String hashedPassword = hashPassword(password);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbHash = rs.getString("password_hash");
                    if (dbHash.equals(hashedPassword)) {
                        return rs.getLong("id");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при валидации пользователя: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Ищет логин пользователя и возвращает его ID.
     * @param login имя пользователя
     * @return ID пользователя из базы, или -1 если данные неверны
     */
    public long getUserIdByLogin(String login) {
        String sql = "SELECT id FROM users WHERE login = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                    }
                } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при валидации пользователя: " + e.getMessage());
        }
        return -1;
    }
}