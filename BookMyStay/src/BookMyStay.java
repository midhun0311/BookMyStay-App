import java.util.*;

// Room domain model
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("---------------------------");
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> availabilityMap;

    public Inventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availabilityMap.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return availabilityMap.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availabilityMap.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {

            int availableCount = inventory.getAvailability(type);

            // Validation: Only show available rooms
            if (availableCount > 0 && roomCatalog.containsKey(type)) {
                Room room = roomCatalog.get(type);

                room.displayDetails();
                System.out.println("Available Count: " + availableCount);
                System.out.println("===========================\n");
            }
        }
    }
}

// Main class
 class UseCase4RoomSearch {
    public static void main(String[] args) {

        // Step 1: Create Room Catalog (Domain Objects)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 2000, Arrays.asList("WiFi", "TV")));

        roomCatalog.put("Double",
                new Room("Double", 3500, Arrays.asList("WiFi", "TV", "AC")));

        roomCatalog.put("Suite",
                new Room("Suite", 6000, Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Step 2: Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);   // Will be filtered out
        inventory.addRoom("Suite", 2);

        // Step 3: Search Service (Read-only)
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Step 4: Guest initiates search
        searchService.searchAvailableRooms();
    }
}