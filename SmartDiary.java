import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SmartDiary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // User's home directory
        String userHome = System.getProperty("user.home");

        // Define commonly used folders to check
        String[] folderNames = {"Documents", "Downloads", "Pictures", "Videos", "Desktop"};

        // Store only folders that exist
        List<File> availableDirs = new ArrayList<>();

        for (String folder : folderNames) {
            File dir = new File(userHome, folder);
            if (dir.exists() && dir.isDirectory()) {
                availableDirs.add(dir);
            }
        }

        // Add all available drives (like D:\, E:\, etc.)
        File[] roots = File.listRoots();
        for (File root : roots) {
            if (root.exists() && root.canWrite()) {
                availableDirs.add(root);
            }
        }

        // Let user choose from the detected directories
        System.out.println("Choose a location to save your diary:");
        for (int i = 0; i < availableDirs.size(); i++) {
            System.out.println((i + 1) + ": " + availableDirs.get(i).getAbsolutePath());
        }

        int choice = -1;
        while (choice < 1 || choice > availableDirs.size()) {
            System.out.print("Enter your choice (1-" + availableDirs.size() + "): ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline
            } else {
                sc.nextLine(); // discard invalid input
            }
        }

        File selectedPath = availableDirs.get(choice - 1);

        // Ask for subfolder name (optional)
        System.out.print("Enter a folder name inside this location to store your diary: ");
        String subfolder = sc.nextLine();
        File finalDir = new File(selectedPath, subfolder);
        if (!finalDir.exists()) {
            finalDir.mkdirs();
        }

        // Create diary file with today's date
        LocalDate today = LocalDate.now();
        String fileName = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        File diaryFile = new File(finalDir, fileName);

        // Write the diary entry
        System.out.println("Tell me what's on your mind:");
        String diaryEntry = sc.nextLine();

        try (FileWriter fw = new FileWriter(diaryFile)) {
            fw.write("Diary Entry for " + today + ":\n");
            fw.write(diaryEntry);
            System.out.println("✅ Diary saved at: " + diaryFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Error saving diary: " + e.getMessage());
        }
    }
}
