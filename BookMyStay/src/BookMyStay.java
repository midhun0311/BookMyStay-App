import java.util.*;
import java.util.concurrent.*;

// Booking Request Class
class BookingRequest {
    private String guestName;
    private int roomsRequested;

    public BookingRequest(String guestName, int roomsRequested) {
        this.guestName = guestName;
        this.roomsRequested = roomsRequested;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomsRequested() {
        return roomsRequested;
    }
}

// Shared Inventory Class (Critical Section handled here)
class HotelInventory {
    private int availableRooms;

    public HotelInventory(int rooms) {
        this.availableRooms = rooms;
    }

    // synchronized ensures thread safety
    public synchronized boolean bookRooms(String guest, int rooms) {
        System.out.println(Thread.currentThread().getName() +
                " processing booking for " + guest);

        if (rooms <= availableRooms) {
            System.out.println("Rooms available. Booking " + rooms + " for " + guest);
            availableRooms -= rooms;

            // simulate processing delay
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            System.out.println("Booking successful for " + guest +
                    ". Remaining rooms: " + availableRooms);
            return true;
        } else {
            System.out.println("Booking FAILED for " + guest +
                    ". Not enough rooms.");
            return false;
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor implements Runnable {
    private Queue<BookingRequest> bookingQueue;
    private HotelInventory inventory;

    public BookingProcessor(Queue<BookingRequest> queue, HotelInventory inventory) {
        this.bookingQueue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // synchronized access to shared queue
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            if (request != null) {
                inventory.bookRooms(
                        request.getGuestName(),
                        request.getRoomsRequested()
                );
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        // Shared Booking Queue
        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        // Add booking requests (Simulating multiple guests)
        bookingQueue.add(new BookingRequest("Guest-1", 2));
        bookingQueue.add(new BookingRequest("Guest-2", 3));
        bookingQueue.add(new BookingRequest("Guest-3", 1));
        bookingQueue.add(new BookingRequest("Guest-4", 4));
        bookingQueue.add(new BookingRequest("Guest-5", 2));

        // Shared Inventory
        HotelInventory inventory = new HotelInventory(7);

        // Create multiple threads (Concurrent Processors)
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll booking requests processed safely.");
    }
}