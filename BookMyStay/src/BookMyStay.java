import java.util.*;

// Booking class to represent a reservation
class Booking {
    String bookingId;
    String roomType;
    String roomId;
    boolean isCancelled;

    Booking(String bookingId, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }
}

// Main system class
public class UseCase10BookingCancellation {

    // Inventory: roomType -> available count
    private static Map<String, Integer> inventory = new HashMap<>();

    // Booking storage: bookingId -> Booking object
    private static Map<String, Booking> bookings = new HashMap<>();

    // Available rooms pool: roomType -> list of roomIds
    private static Map<String, List<String>> availableRooms = new HashMap<>();

    // Rollback stack (LIFO)
    private static Stack<String> rollbackStack = new Stack<>();

    public static void main(String[] args) {

        // Initialize inventory and rooms
        initializeInventory();

        // Create sample booking
        createBooking("B001", "Deluxe");

        // Attempt cancellation
        cancelBooking("B001");

        // Try invalid cancellation
        cancelBooking("B002");

        // Try duplicate cancellation
        cancelBooking("B001");
    }

    private static void initializeInventory() {
        inventory.put("Deluxe", 2);

        availableRooms.put("Deluxe", new ArrayList<>(Arrays.asList("D1", "D2")));
    }

    private static void createBooking(String bookingId, String roomType) {
        List<String> rooms = availableRooms.get(roomType);

        if (rooms == null || rooms.isEmpty()) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }

        String allocatedRoom = rooms.remove(0);
        inventory.put(roomType, inventory.get(roomType) - 1);

        Booking booking = new Booking(bookingId, roomType, allocatedRoom);
        bookings.put(bookingId, booking);

        System.out.println("Booking confirmed: " + bookingId + " | Room: " + allocatedRoom);
    }

    private static void cancelBooking(String bookingId) {
        System.out.println("\nProcessing cancellation for: " + bookingId);

        // Step 1: Validate booking existence
        if (!bookings.containsKey(bookingId)) {
            System.out.println("Cancellation failed: Booking does not exist.");
            return;
        }

        Booking booking = bookings.get(bookingId);

        // Step 2: Check if already cancelled
        if (booking.isCancelled) {
            System.out.println("Cancellation failed: Booking already cancelled.");
            return;
        }

        // Step 3: Record room ID in rollback stack
        rollbackStack.push(booking.roomId);

        // Step 4: Restore inventory
        inventory.put(booking.roomType, inventory.get(booking.roomType) + 1);

        // Step 5: Release room back to pool
        availableRooms.get(booking.roomType).add(booking.roomId);

        // Step 6: Update booking state
        booking.isCancelled = true;

        // Step 7: Log history
        System.out.println("Cancellation successful for booking: " + bookingId);
        System.out.println("Room released: " + booking.roomId);

        // Debug state
        printSystemState();
    }

    private static void printSystemState() {
        System.out.println("\n--- System State ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Available Rooms: " + availableRooms);
        System.out.println("Rollback Stack: " + rollbackStack);
        System.out.println("---------------------\n");
    }
}