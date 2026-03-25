import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Enum for Room Types
enum RoomType {
    STANDARD, DELUXE, SUITE
}

// Hotel Inventory Class
class HotelInventory {
    private Map<RoomType, Integer> roomAvailability;

    public HotelInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put(RoomType.STANDARD, 5);
        roomAvailability.put(RoomType.DELUXE, 3);
        roomAvailability.put(RoomType.SUITE, 2);
    }

    public void validateRoomType(String type) throws InvalidBookingException {
        try {
            RoomType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
    }

    public void validateAvailability(RoomType type, int roomsRequested) throws InvalidBookingException {
        if (roomsRequested <= 0) {
            throw new InvalidBookingException("Room count must be greater than zero.");
        }

        int available = roomAvailability.get(type);
        if (roomsRequested > available) {
            throw new InvalidBookingException(
                    "Not enough rooms available. Requested: " + roomsRequested + ", Available: " + available
            );
        }
    }

    public void bookRoom(RoomType type, int roomsRequested) throws InvalidBookingException {
        validateAvailability(type, roomsRequested);

        // Update inventory safely
        int current = roomAvailability.get(type);
        roomAvailability.put(type, current - roomsRequested);

        System.out.println("Booking successful! " + roomsRequested + " " + type + " room(s) booked.");
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Availability:");
        for (Map.Entry<RoomType, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HotelInventory inventory = new HotelInventory();

        try {
            System.out.print("Enter room type (STANDARD/DELUXE/SUITE): ");
            String roomTypeInput = scanner.nextLine();

            // Validate Room Type
            inventory.validateRoomType(roomTypeInput);
            RoomType roomType = RoomType.valueOf(roomTypeInput.toUpperCase());

            System.out.print("Enter number of rooms: ");
            int rooms = scanner.nextInt();

            // Booking Process (Fail-Fast)
            inventory.bookRoom(roomType, rooms);

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        } finally {
            inventory.displayInventory();
            System.out.println("\nSystem is still running safely.");
        }

        scanner.close();
    }
}