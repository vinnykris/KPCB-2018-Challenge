package vinodKPCBChallenge;

import java.io.Console;

/**
 * A text-based, interactive program that demonstrates FixedSizeHashMap.
 */
public class Main {
    public static void main(String[] args) {
        // variables
        String input, key, value;
        int size_input, choice;

        // create console
        Console console = System.console();

        // prompt for size of hash map
        input = console.readLine("Enter a positive integer for your desired hash map size: ");

        // continue prompting if the user input is bad
        while (!input.matches("^[1-9]\\d*$")) {
            console.printf("'%s' is not a positive integer.%n", input);
            input = console.readLine("Try again: ");
        }
        size_input = Integer.parseInt(input);
        FixedSizeHashMap<String> hash_map = new FixedSizeHashMap<String>(size_input);
        console.printf("Created a String hash map of size %d.%n", size_input);

        // begin interactive loop
        while (true) {
            // open menu of options
            input = "";
            while (!input.matches("^[1-5]$")) {
                printMenu();
                input = console.readLine("Please enter your selection: ");
            }
            choice = Integer.parseInt(input);
            switch (choice) {
                // user chooses set option
                case 1:
                    while (choice != 0) {
                        key = console.readLine("Enter a key: ");
                        value = console.readLine("Enter a value: ");
                        console.printf("Set key '%s' to be associated with '%s'.%n", key, value);
                        input = console.readLine("Is this correct? (yes/no) ");
                        if (input.toLowerCase().charAt(0) == 'y') {
                            choice = 0;
                            if (hash_map.set(key, value)) {
                                console.printf("%n'%s' successfully associated with '%s'.%n", key, value);
                            } else {
                                System.out.println("\nUh oh. Something went wrong.");
                                console.printf("Either '%s' is already associated with another String, or the hashmap is full. %n", key);
                            }
                        }
                    }
                    break;
                
                // user chooses get option
                case 2:
                    while (choice != 0) {
                        key = console.readLine("Enter a key: ");
                        console.printf("Get the value associated with '%s'.%n", key);
                        input = console.readLine("Is this correct? (yes/no) ");
                        if (input.toLowerCase().charAt(0) == 'y') {
                            choice = 0;
                            value = hash_map.get(key);
                            if (value != null) {
                                console.printf("%n'%s' is the value associated with '%s'.%n", value, key);
                            } else {
                                console.printf("%n'%s' was not found.%n", key);
                            }
                        }
                    }
                    break;
                
                // user chooses delete option
                case 3:
                    while (choice != 0) {
                        key = console.readLine("Enter a key: ");
                        console.printf("Delete '%s' from the hashmap.%n", key);
                        input = console.readLine("Is this correct? (yes/no) ");
                        if (input.toLowerCase().charAt(0) == 'y') {
                            choice = 0;
                            value = hash_map.delete(key);
                            if (value != null) {
                                console.printf("%n'%s' was deleted from the hash map. It was associated with '%s'.%n", key, value);
                            } else {
                                console.printf("%n'%s' was not found.%n", key);
                            }
                        }
                    }
                    break;
                
                // user chooses load factor option
                case 4:
                    console.printf("%nThe current load factor is %.3f.%n", hash_map.load());
                    break;
                
                // user chooses exit option
                case 5:
                default:
                    System.out.println("Thank you.");
                    System.exit(0);
            }
        }
    }
    /**
     * Prints out menu
     */
    public static void printMenu() {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("(1) Insert a key and value");
        System.out.println("(2) Get the value associated with a key");
        System.out.println("(3) Delete a key");
        System.out.println("(4) Show the load factor");
        System.out.println("(5) Exit");
    }
}
