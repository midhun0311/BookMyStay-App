import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a booking request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName + ", RoomType=" + roomType + "]";
    }
}

// Booking Request Queue Manager
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View all queued requests
    public void viewRequests() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests in Queue (FIFO Order):");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    // Process next request (without allocation logic)
    public Reservation processNextRequest() {
        if (requestQueue.isEmpty()) {
            System.out.println("No requests to process.");
            return null;
        }

        Reservation r = requestQueue.poll();
        System.out.println("Processing request: " + r);
        return r;
    }
}

// Main class
 class UseCase5BookingRequestQueue {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulating incoming booking requests
        queue.addRequest(new Reservation("Alice", "Deluxe"));
        queue.addRequest(new Reservation("Bob", "Standard"));
        queue.addRequest(new Reservation("Charlie", "Suite"));

        // View queued requests
        queue.viewRequests();

        // Process requests in FIFO order
        System.out.println("\n--- Processing Requests ---");
        queue.processNextRequest();
        queue.processNextRequest();

        // View remaining requests
        queue.viewRequests();
    }
}