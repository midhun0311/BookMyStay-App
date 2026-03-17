import java.util.HashMap;
import java.util.Map;

public class BookMyStay {

    // Inner class for RoomInventory
    static class RoomInventory {

        private Map<String, Integer> inventory;

        // Constructor
        public RoomInventory() {
            inventory = new HashMap<>();
        }

        // Add room type
        public void addRoomType(String roomType, int count) {
            inventory.put(roomType, count);
        }

        // Get availability
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        // Update availability
        public void updateAvailability(String roomType, int change) {
            int current = inventory.getOrDefault(roomType, 0);
            int updated = current + change;

            if (updated < 0) {
                System.out.println("Cannot reduce below zero for " + roomType);
            } else {
                inventory.put(roomType, updated);
            }
        }

        // Display inventory
        public void displayInventory() {
            System.out.println("\nCurrent Room Inventory:");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 5);
        inventory.addRoomType("Suite", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Simulate booking
        System.out.println("\nBooking 2 Single rooms...");
        inventory.updateAvailability("Single", -2);

        // Simulate cancellation
        System.out.println("Cancelling 1 Double room...");
        inventory.updateAvailability("Double", 1);

        // Check availability
        System.out.println("\nAvailable Suites: " + inventory.getAvailability("Suite"));

        // Display updated inventory
        inventory.displayInventory();
    }
}