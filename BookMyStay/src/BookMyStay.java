import java.util.*;

// Booking Request Model
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> roomInventory = new HashMap<>();

    public InventoryService() {
        // Initial inventory setup
        roomInventory.put("DELUXE", 2);
        roomInventory.put("STANDARD", 3);
    }

    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking Service
class BookingService {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToRooms = new HashMap<>();
    private InventoryService inventoryService;

    private int roomCounter = 1;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Add booking request (FIFO)
    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Generate unique Room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 3).toUpperCase() + "-" + roomCounter++;
        } while (allocatedRoomIds.contains(roomId)); // Ensure uniqueness

        return roomId;
    }

    // Process bookings
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();

            System.out.println("\nProcessing request for: " + request.customerName);

            // Check availability
            if (!inventoryService.isAvailable(request.roomType)) {
                System.out.println("No rooms available for type: " + request.roomType);
                continue;
            }

            // Atomic allocation block
            String roomId = generateRoomId(request.roomType);

            // Record room ID (uniqueness)
            allocatedRoomIds.add(roomId);

            // Map room type to allocated rooms
            roomTypeToRooms
                    .computeIfAbsent(request.roomType, k -> new HashSet<>())
                    .add(roomId);

            // Update inventory immediately
            inventoryService.decrementRoom(request.roomType);

            // Confirm reservation
            System.out.println("Booking Confirmed!");
            System.out.println("Customer: " + request.customerName);
            System.out.println("Room Type: " + request.roomType);
            System.out.println("Allocated Room ID: " + roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");
        for (Map.Entry<String, Set<String>> entry : roomTypeToRooms.entrySet()) {
            System.out.println(entry.getKey() + " Rooms -> " + entry.getValue());
        }
    }
}

// Main Class
 class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Add booking requests (FIFO Queue)
        bookingService.addRequest(new BookingRequest("Alice", "DELUXE"));
        bookingService.addRequest(new BookingRequest("Bob", "DELUXE"));
        bookingService.addRequest(new BookingRequest("Charlie", "DELUXE")); // Should fail (only 2 available)
        bookingService.addRequest(new BookingRequest("David", "STANDARD"));

        // Process all bookings
        bookingService.processBookings();

        // Display results
        bookingService.displayAllocations();
        inventoryService.displayInventory();
    }
}