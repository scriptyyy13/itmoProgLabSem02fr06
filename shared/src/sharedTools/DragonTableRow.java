package sharedTools;
import java.util.Date;

public class DragonTableRow {
    private final long id;
    private final long creatorId;
    private final String name;
    private final Float x;
    private final Double y;
    private final Date creationDate;
    private final long age;
    private final Integer weight;
    private final Boolean speaking;
    private final String color; // храним как строку для отображения
    private final String killerName;
    private final Date killerBirthday;
    private final String killerPassport;
    private final String killerNat;
    private final Integer locX;
    private final Integer locY;
    private final Integer locZ;
    private final String locName;

    public DragonTableRow(long id, long creatorId, String name, Float x, Double y,
                          Date creationDate, long age, Integer weight, Boolean speaking,
                          String color, String killerName, Date killerBirthday,
                          String killerPassport, String killerNat, Integer locX,
                          Integer locY, Integer locZ, String locName) {
        this.id = id;
        this.creatorId = creatorId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
        this.age = age;
        this.weight = weight;
        this.speaking = speaking;
        this.color = color;
        this.killerName = killerName;
        this.killerBirthday = killerBirthday;
        this.killerPassport = killerPassport;
        this.killerNat = killerNat;
        this.locX = locX;
        this.locY = locY;
        this.locZ = locZ;
        this.locName = locName;
    }

    public long getId() { return id; }
    public long getCreatorId() { return creatorId; }
    public String getName() { return name; }
    public Float getX() { return x; }
    public Double getY() { return y; }
    public Date getCreationDate() { return creationDate; }
    public long getAge() { return age; }
    public Integer getWeight() { return weight; }
    public Boolean getSpeaking() { return speaking; }
    public String getColor() { return color; }
    public String getKillerName() { return killerName; }
    public Date getKillerBirthday() { return killerBirthday; }
    public String getKillerPassport() { return killerPassport; }
    public String getKillerNat() { return killerNat; }
    public Integer getLocX() { return locX; }
    public Integer getLocY() { return locY; }
    public Integer getLocZ() { return locZ; }
    public String getLocName() { return locName; }
}