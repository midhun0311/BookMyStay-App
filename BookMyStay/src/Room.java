import java.util.Scanner;
public abstract class Room
{

            protected int numberOfBeds;
            protected int squareFeet;
            protected double pricePerNight;

            // Constructor
            public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
                this.numberOfBeds = numberOfBeds;
                this.squareFeet = squareFeet;
                this.pricePerNight = pricePerNight;
            }

            // Display method
            public void displayRoomDetails(int available) {
                System.out.println("Beds: " + numberOfBeds);
                System.out.println("Size: " + squareFeet + " sqft");
                System.out.println("Price per night: " + pricePerNight);
                System.out.println("Available: " + available);
                System.out.println();
            }


        // SingleRoom Class
        static class SingleRoom extends Room {
            public SingleRoom() {
                super(1, 250, 1500.0);
            }
        }

        // DoubleRoom Class
        static class DoubleRoom extends Room {
            public DoubleRoom() {
                super(2, 400, 2500.0);
            }
        }

        // SuiteRoom Class
        static class SuiteRoom extends Room {
            public SuiteRoom() {
                super(3, 750, 5000.0);
            }
        }

        // Main Method
        public static void main(String[] args) {

            System.out.println("Hotel Room Initialization\n");

            SingleRoom single = new SingleRoom();
            DoubleRoom dbl = new DoubleRoom();
            SuiteRoom suite = new SuiteRoom();

            System.out.println("Single Room:");
            single.displayRoomDetails(5);

            System.out.println("Double Room:");
            dbl.displayRoomDetails(3);

            System.out.println("Suite Room:");
            suite.displayRoomDetails(2);
    }
}

