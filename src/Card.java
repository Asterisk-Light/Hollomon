import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Card implements Comparable<Card> {
    private String id;       // numeric string
    private String name;
    private String rank;     // e.g., "UNIQUE", "COMMON", etc.
    private int lastSalePrice;

    // Constructor
    public Card(String id, String name, String rank, int lastSalePrice) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.lastSalePrice = lastSalePrice;
    }

    // Getters (optional setters if needed)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public int getLastSalePrice() {
        return lastSalePrice;
    }

    // Rank ordering map
    private static final Map<String, Integer> rankOrder = new HashMap<>();
    static {
        rankOrder.put("UNIQUE", 1);
        rankOrder.put("RARE", 2);
        rankOrder.put("COMMON", 3);
        // add other ranks as needed
    }

    @Override
    public int compareTo(Card other) {
        // Compare by rank order
        int rankCompare = Integer.compare(rankOrder.getOrDefault(this.rank, Integer.MAX_VALUE),
                rankOrder.getOrDefault(other.rank, Integer.MAX_VALUE));
        if (rankCompare != 0) {
            return rankCompare;
        }

        // Compare by name (alphabetical)
        int nameCompare = this.name.compareTo(other.name);
        if (nameCompare != 0) {
            return nameCompare;
        }

        // Compare by numeric ID
        int thisId = Integer.parseInt(this.id);
        int otherId = Integer.parseInt(other.id);
        return Integer.compare(thisId, otherId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card other = (Card) o;
        return this.rank.equals(other.rank) &&
                this.name.equals(other.name) &&
                this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, name, id);
    }

    @Override
    public String toString() {
        return String.format("Card{id='%s', name='%s', rank='%s', lastSalePrice=%d}", id, name, rank, lastSalePrice);
    }
}
