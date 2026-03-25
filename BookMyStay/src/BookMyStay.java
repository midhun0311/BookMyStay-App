import java.io.*;
import java.util.*;

// Booking class
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    String customerName;
    int roomsBooked;

    public Booking(String customerName, int roomsBooked) {
        this.customerName = customerName;
        this.roomsBooked = roomsBooked;
    }

    @Override
    public String toString() {
        return customerName + " booked " + roomsBooked + " room(s)";
    }
}

// Inventory class
class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    int totalRooms;
    int availableRooms;

    public Inventory(int totalRooms) {
        this.totalRooms = totalRooms;
        this.availableRooms = totalRooms;
    }

    public boolean bookRooms(int rooms) {
        if (rooms <= availableRooms) {
            availableRooms -= rooms;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Available Rooms: " + availableRooms + "/" + totalRooms;
    }
}

// Wrapper class to persist system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    List<Booking> bookings;
    Inventory inventory;

    public SystemState(List<Booking> bookings, Inventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("✅ System state saved successfully.");
        } catch (IOException e) {
            System.out.println("⚠️ Error saving system state: " + e.getMessage());
        }
    }

    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("⚠️ No saved data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("✅ System state restored successfully.");
            return (SystemState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ Corrupted or unreadable data. Starting fresh.");
            return null;
        }
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Step 1: Load previous state
        SystemState state = PersistenceService.load();

        List<Booking> bookings;
        Inventory inventory;

        if (state == null) {
            // Fresh system
            inventory = new Inventory(10);
            bookings = new ArrayList<>();
        } else {
            inventory = state.inventory;
            bookings = state.bookings;
        }

        // Step 2: Display current state
        System.out.println("\n📊 Current Inventory:");
        System.out.println(inventory);

        System.out.println("\n📘 Booking History:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }

        // Step 3: Simulate new booking
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter rooms to book: ");
        int rooms = sc.nextInt();

        if (inventory.bookRooms(rooms)) {
            Booking booking = new Booking(name, rooms);
            bookings.add(booking);
            System.out.println("✅ Booking successful!");
        } else {
            System.out.println("❌ Not enough rooms available.");
        }

        // Step 4: Save state before shutdown
        SystemState newState = new SystemState(bookings, inventory);
        PersistenceService.save(newState);

        System.out.println("\n🔁 Restart the program to see persistence in action.");
    }
}