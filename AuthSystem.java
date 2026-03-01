import java.util.Scanner;

class AuthException extends Exception {
    public AuthException(String message) { super(message); }
}

class RegistrationException extends AuthException {
    public RegistrationException(String message) { super(message); }
}

class AuthenticationException extends AuthException {
    public AuthenticationException(String message) { super(message); }
}

class UserNotFoundException extends AuthException {
    public UserNotFoundException(String message) { super(message); }
}

class StorageFullException extends AuthException {
    public StorageFullException(String message) { super(message); }
}


public class AuthSystem {

    private static final int MAX_USERS = 15;
    private static String[] usernames = new String[MAX_USERS];
    private static String[] passwords = new String[MAX_USERS];
    private static int userCount = 0;

    private static String[] forbiddenWords = {"admin", "pass", "password", "qwerty", "ytrewq"};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                printMenu();
                String input = sc.nextLine();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1: registerUser(sc); break;
                    case 2: deleteUser(sc); break;
                    case 3: authenticateUser(sc); break;
                    case 4: return;
                    default: System.out.println("Оберіть від 1 до 4.");
                }
            } catch (AuthException e) {

                System.out.println("ПОМИЛКА: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Непередбачена помилка.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- МЕНЮ (1-додати, 2-видалити, 3-дія) ---"); // [cite: 41]
        System.out.println("1. Реєстрація");
        System.out.println("2. Видалення");
        System.out.println("3. Вхід (Дія)");
        System.out.println("4. Вихід");
        System.out.print("Вибір: ");
    }

    public static void registerUser(Scanner sc) throws AuthException {
        if (userCount >= MAX_USERS) throw new StorageFullException("Ліміт 15 користувачів!"); // [cite: 16]

        System.out.print("Логін (мін. 5 симв.): ");
        String login = sc.nextLine();
        if (login.length() < 5 || login.contains(" ")) throw new RegistrationException("Невірний логін!"); // [cite: 19, 21]

        System.out.print("Пароль: ");
        String pass = sc.nextLine();
        validatePassword(pass);

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] == null) {
                usernames[i] = login;
                passwords[i] = pass;
                userCount++;
                System.out.println("Успішно!");
                return;
            }
        }
    }

    private static void validatePassword(String pass) throws RegistrationException {
        if (pass.length() < 10) throw new RegistrationException("Пароль мін. 10 симв.!"); // [cite: 34]
        if (pass.contains(" ")) throw new RegistrationException("Пароль без пробілів!"); // [cite: 30]

        boolean hasDigit = false, hasSpecial = false;
        String specials = "!@#$%^&*";
        for (char c : pass.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true; // [cite: 28]
            if (specials.indexOf(c) >= 0) hasSpecial = true; // [cite: 26]
        }
        if (!hasDigit || !hasSpecial) throw new RegistrationException("Має бути цифра та спецсимвол!");

        for (String word : forbiddenWords) {
            if (pass.toLowerCase().

            contains(word)) throw new RegistrationException("Заборонене слово!"); // [cite: 35]
        }
    }

    public static void deleteUser(Scanner sc) throws AuthException {
        System.out.print("Ім'я для видалення: ");
        String login = sc.nextLine();
        for (int i = 0; i < MAX_USERS; i++) {
            if (login.equals(usernames[i])) {
                usernames[i] = null; passwords[i] = null;
                userCount--;
                System.out.println("Видалено."); return;
            }
        }
        throw new UserNotFoundException("Не знайдено!"); // [cite: 37]
    }

    public static void authenticateUser(Scanner sc) throws AuthException {
        System.out.print("Логін: "); String login = sc.nextLine();
        System.out.print("Пароль: "); String pass = sc.nextLine();
        for (int i = 0; i < MAX_USERS; i++) {
            if (login.equals(usernames[i]) && pass.equals(passwords[i])) {
                System.out.println("Аутентифіковано!"); return; // [cite: 40]
            }
        }
        throw new AuthenticationException("Помилка входу!"); // [cite: 39]
    }
}