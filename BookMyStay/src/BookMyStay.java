import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;
    private double pricePerNight;

    public Reservation(String guestName, String roomType, int nights, double pricePerNight) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    public double getTotalCost() {
        return nights * pricePerNight;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights +
                ", Total Cost: ₹" + getTotalCost();
    }
}

// Booking History class
class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    // Store confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations); // return copy (immutability)
    }
}

// Booking Report Service
class BookingReportService {

    // Generate summary report
    public void generateReport(List<Reservation> reservations) {
        System.out.println("\n===== Booking Report =====");

        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        double totalRevenue = 0;
        int totalBookings = reservations.size();

        for (Reservation r : reservations) {
            System.out.println(r);
            totalRevenue += r.getTotalCost();
        }

        System.out.println("\n----- Summary -----");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("Arun", "Deluxe", 2, 3000);
        Reservation r2 = new Reservation("Priya", "Suite", 3, 5000);
        Reservation r3 = new Reservation("Rahul", "Standard", 1, 1500);

        // Add to booking history (in order)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin retrieves and views report
        List<Reservation> storedReservations = history.getAllReservations();
        reportService.generateReport(storedReservations);
    }
}