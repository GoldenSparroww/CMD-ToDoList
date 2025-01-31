import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoList {

    final private static String dataDir = System.getenv("APPDATA") + System.getProperty("file.separator") + "ToDoList";
    final private static String dataFileName = dataDir + System.getProperty("file.separator") + "savedTasks.txt";

    final private static Scanner sc = new Scanner(System.in);
    private ArrayList<Task> taskList = new ArrayList<>();

    public ToDoList() {
        this.taskList = loadSavedTasks();
    }

    private static ArrayList<Task> loadSavedTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        File f = new File(dataFileName);
        try {
            Scanner fileReader = new Scanner(f);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] splitLine = line.split(";");
                Task task = new Task();
                task.setCheck(splitLine[0]);
                task.setName(splitLine[1]);
                taskList.add(task);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {}
        return taskList;
    }

    private static void initDir() {
        File dir = new File(dataDir);
        if (!dir.exists()){
            dir.mkdirs();
        }
    }

    private boolean isListEmpty() {
        if (taskList.isEmpty()) {
            System.out.println("[Info: Seznam je prazdny]");
            return true;
        }
        return false;
    }

    private void resaveFile() {
        try {
            FileWriter writer = new FileWriter(dataFileName);
            for (Task task : taskList) {
                writer.append(task.toStringForSave());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("[Info: Nastala chyba pri ukladani souboru");
            System.out.println("[Info: Zkuste to znovu nebo restartujte aplikaci");
            System.out.println("[Tip: Zkontrolujte zda ma aplikace dostatecna opravneni]");
        }
    }

    private void addTask() {
        System.out.print("Napiste nazev ukolu: ");
        String taskName = sc.nextLine();
        Task newTask = new Task();
        newTask.setName(taskName);
        taskList.add(newTask);
        saveTask(newTask);
    }

    private void saveTask(Task taskToSave) {
        try {
            FileWriter writer = new FileWriter(dataFileName, true);
            writer.append(taskToSave.toStringForSave());
            writer.close();
            System.out.println("[Info: Zmeny ulozeny]");
            System.out.println("[Tip: Prohlednete si sve ukoly v \"2. Zobrazit vsechny ukoly\"]");
        } catch (IOException e) {
            System.out.println("[Info: Nastala chyba pri ukladani ukolu]");
            System.out.println("[Info: Proces opakujte nebo restartujte aplikaci]");
            System.out.println("[Info: Zmeny neulozeny]");
        }
    }

    private boolean showTasks() {
        if (this.isListEmpty()) return false;
        System.out.println("Seznam ukolu: (X - zaskrtnuto / O - nezaskrtnuto)");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1)+".\t\t"+taskList.get(i));
        }
        return true;
    }

    private boolean doYouWantToContinue() {
        if (this.isListEmpty()) return false;
        System.out.println("Prejete si pokracovat v upravach? (yes/no) (enter = yes)");
        String repeat = sc.nextLine();
        if (repeat.isEmpty() || repeat.equalsIgnoreCase("y") || repeat.equalsIgnoreCase("ye") || repeat.equalsIgnoreCase("yes")) {
            this.showTasks();
            return true;
        }
        else if (repeat.equalsIgnoreCase("n") || repeat.equalsIgnoreCase("no")) {
            return false;
        } else {
            System.out.println("[Info: Nevalidni odpoved, v upravach nebude pokracovano.]");
            return false;
        }
    }

    private void checkOrUncheckTasks() {
        if (!(this.showTasks())) return;
        System.out.println("[Info: Pro navrat do menu napiste \"exit\"]");
        while (true) {
            System.out.print("Napiste cislo ukolu (bez tecky), ktery chcete (za/od)skrtnout: ");
            int lineNumber;
            try {
                String answer = sc.nextLine();
                if (answer.equalsIgnoreCase("exit")) break;
                lineNumber = Integer.parseInt(answer);
                if (!(lineNumber > 0 && lineNumber <= taskList.size())) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("[Info: ukol s timto cislem neexistuje]");
                continue;
            }

            System.out.print("Vyberte akci (X - zaskrtnout / O - odskrtnout): ");
            String symbol = sc.nextLine().toUpperCase();
            if (symbol.equalsIgnoreCase("exit")) break;
            if (!(symbol.equals("X") || symbol.equals("O"))) {
                System.out.println("[Info: Lze provest jen zaskrtnuti (X) nebo odkskrtnuti (O) ulohy!]");
                continue;
            }

            int position = lineNumber - 1;
            taskList.get(position).setCheck(symbol);
            this.resaveFile();
            System.out.println("[Info: Zmeny ulozeny]");
            if (!(this.doYouWantToContinue())) break;
        }
    }

    private void deleteTasks() {
        if (!(this.showTasks())) return;
        System.out.println("[Info: Pro navrat do menu napiste \"exit\"]");
        while (true) {
            System.out.print("Napiste cislo ukolu (bez tecky), ktery chcete odstranit: ");
            try {
                String answer = sc.nextLine();
                if (answer.equalsIgnoreCase("exit")) break;
                int lineNumber = Integer.parseInt(answer);
                if (!(lineNumber > 0 && lineNumber <= taskList.size())) throw new NumberFormatException();
                taskList.remove(lineNumber - 1);
                System.out.println("[Info: Zmeny ulozeny]");
                this.resaveFile();
            } catch (NumberFormatException e) {
                System.out.println("[Info: ukol s timto cislem neexistuje]");
                continue;
            }

            if (!(this.doYouWantToContinue())) break;
        }
    }

    private static void optionsInput(ToDoList myTaskList) {
        while (true) {
            System.out.println("1. Pridat ukol");
            System.out.println("2. Zobrazit vsechny ukoly");
            System.out.println("3. Oznacit ukol jako (ne)splneny");
            System.out.println("4. Smazat ukol");
            System.out.println("5. Ukoncit program");
            System.out.print("Vyberte akci (napiste cislo): ");
            try{
                int option = Integer.parseInt(sc.nextLine());
                System.out.print("\n");
                switch (option) {
                    case 1:
                        myTaskList.addTask();
                        break;
                    case 2:
                        myTaskList.showTasks();
                        break;
                    case 3:
                        myTaskList.checkOrUncheckTasks();
                        break;
                    case 4:
                        myTaskList.deleteTasks();
                        break;
                    case 5:
                        System.out.println("Program ukoncen");
                        System.exit(0);
                        break;
                }
                System.out.print("\n");
            } catch (NumberFormatException e) {
                System.out.println("[Info: Nezdali validni cislo]\n");
            }
        }
    }

    private static void start() {
        initDir();
        System.out.println("Vitejte v ToDoListu!");
        ToDoList myTaskList = new ToDoList();
        optionsInput(myTaskList);
    }

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        start();
    }
}