package SHOP;

public class Flags {
    // Feature flag for development-mode
    public static boolean development;

    // Returns flag for development
    public static boolean isDevelopment() {
        return development;
    }

    // Sets flag
    public static void setDevelopment(boolean development) {
        Flags.development = development;
    }
}
