

import model.User;
import service.ServiceUser;
import service.ServiceUserImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ServiceUser serviceUser = new ServiceUserImpl();

        Scanner consoleInt = new Scanner(System.in);
        Scanner consoleString = new Scanner(System.in);

        serviceUser.load("resources");

        while (true) {
            printMenu();
            int command = consoleInt.nextInt();

            switch (command) {
                case 1:
                    registrations(consoleString, serviceUser);
                    break;
                case 2:
                    Long id = null;
                    try {
                        id = authorizationGamer(consoleString, serviceUser);
                    } catch (NullPointerException e) {
                        System.out.println("Ошибка авторизации " + e.getMessage());
                    }
                    if (id != null) {
                        System.out.println("Вы авторизованы");
                        heretofore: while (true) {
                            printUserMenu();
                            int commandUser = consoleInt.nextInt();

                            switch (commandUser) {
                                case 1:
                                    phraseInput(id, consoleInt, consoleString, serviceUser);
                                    break;
                                case 2:
                                    printListWords(id, serviceUser);
                                    break;
                                case 3:
                                    int score = serviceUser.getScore(id);
                                    System.out.println("Ваш счет: " + score);
                                    break;
                                case 4:
                                    leaderboard(serviceUser);
                                    break;
                                case 0:
                                    id = null;
                                    System.out.println("Вы вышли из учетной записи");
                                    break heretofore;
                                default:
                                    System.out.println("Извините, такой команды пока нет.");
                            }
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Извините, такой команды пока нет.");
            }
        }


    }

    private static void printListWords(Long id, ServiceUser serviceUser) {
        List<String> words = serviceUser.getListWords(id);
        for (String word : words) {
            System.out.println(word);
        }
    }

    private static void leaderboard(ServiceUser serviceUser) {
        List<User> users = serviceUser.getLeaderboard();
        if (users.size() != 0) {
            for (int i = 0; i < users.size(); i++) {
                System.out.println("Место номер: " + (i + 1) + " занимает игрок " + users.get(i).getName() + " со счетом: " + users.get(i).getScore());
            }
        } else {
            System.out.println("Список лидеров пуст");
        }
    }

    private static void phraseInput(Long id, Scanner consoleInt, Scanner consoleString, ServiceUser serviceUser) {
        System.out.println("Введите слово или фразу: ");
        String phrase = consoleString.nextLine();
        serviceUser.phraseInput(id, phrase);
    }

    private static long authorizationGamer(Scanner consoleString, ServiceUser serviceUser) {
        System.out.println("Введите ваш email: ");
        String authorizationEmail = consoleString.nextLine();
        System.out.println("Введите ваш пароль: ");
        String authorizationPassword = consoleString.nextLine();
        return serviceUser.authorization(authorizationEmail, authorizationPassword);
    }

    private static void registrations(Scanner consoleString, ServiceUser serviceUser) {
        System.out.println("Введите ваш ник: ");
        String name = consoleString.nextLine();
        System.out.println("Введите ваш email: ");
        String email = consoleString.nextLine();
        System.out.println("Введите ваш пароль: ");
        String password = consoleString.nextLine();
        User gamer = new User(name, email, password);
        serviceUser.addUser(gamer);
    }

    public static void printMenu() {
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Зарегистрироваться");
        System.out.println("2 - Авторизоваться");
        System.out.println("0 - Выход");
    }

    public static void printUserMenu() {
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Ввести слово или фразу");
        System.out.println("2 - Посмотреть список слов пользователя");
        System.out.println("3 - Узнать личный счет");
        System.out.println("4 - Посмотреть список лидеров");
        System.out.println("0 - Выход");
    }
}
