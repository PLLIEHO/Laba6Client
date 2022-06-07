package client;

import common.*;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommandChecker {

    private final Pattern pSpace = Pattern.compile(" ");
    Request request;
    private Sender sender = new Sender();
    private InetAddress address;
    private DatagramSocket socket;

    public CommandChecker(InetAddress address, DatagramSocket socket){
        this.address = address;
        this.socket = socket;
    }


    public void check() throws IOException {
        Serializer serializer = new Serializer();
        while (true) {
            System.out.println("Введите команду: ");
            String[] values = pSpace.split(InputClaimer.input());
            String command = values[0].toLowerCase();
            try {
                switch (command) {
                    case "help":
                        request = new Request(CommandList.HELP, null, Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "info":
                        request = new Request(CommandList.INFO, null, Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "show":
                        request = new Request(CommandList.SHOW, null, Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "add":
                        List<String> arg = new ArrayList<>();
                        add(arg);
                        request = new Request(CommandList.ADD, new Pack(arg, null), Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "update":
                        String element = values[2];
                        List<String> updArg = new ArrayList<>();
                        updArg.add(element);
                        update(updArg, element);
                        request = new Request(CommandList.UPDATE, new Pack(updArg, values[1]), Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "remove_by_id":
                        try {
                            long id = Long.parseLong(values[1]);
                            request = new Request(CommandList.REMOVE_BY_ID, new Pack(null, values[1]), Client.userLogin, Client.password);
                            sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                            break;
                        } catch (NumberFormatException e){
                            System.out.println("ID введен неверно.");
                            check();
                        }
                    case "execute_script":
                        File file = new File(values[1]);
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(values[1]));
                            InputClaimer.setScriptFlag(true);
                            String line;
                            while ((line = reader.readLine()) != null) {
                                InputClaimer.getScriptList().add(line);
                                InputClaimer.incScriptCounter();
                            }
                            break;
                        } catch (FileNotFoundException e) {
                            if (!file.exists()) {
                                System.out.println("Имя файла введено неверно. Пожалуйста, повторите ввод.");
                            } else {
                                System.out.println("Файл заблокирован для чтения.");
                            }
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        request = new Request(CommandList.EXECUTE, new Pack(null, values[1]), Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "add_if_max":
                        if (values.length > 1) {
                            List<String> argMax = new ArrayList<>();
                            add(argMax);
                            request = new Request(CommandList.ADD_IF_MAX, new Pack(argMax, values[1]), Client.userLogin, Client.password);
                            sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        } else {
                            System.out.println("Вы не ввели аргументы!");
                        }
                        break;
                    case "max_by_real_hero":
                        request = new Request(CommandList.MAX_BY_REAL_HERO, null, Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "history":
                        request = new Request(CommandList.HISTORY, null, Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "filter_contains_name":
                        if(values.length>1) {
                            request = new Request(CommandList.FILTER, new Pack(null, values[1]), Client.userLogin, Client.password);
                            sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                            break;
                        } else {
                            System.out.println("Вы не ввели аргумент!");
                            check();
                        }
                    case "print_descending":
                        request = new Request(CommandList.PRINT_DESCENDING, null, Client.userLogin, Client.password);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "exit":
                        request = new Request(CommandList.EXIT, null, Client.userLogin, Client.password);
                        sender.exitFlag = true;
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        socket.close();
                        System.exit(0);
                        break;
                    case "remove_greater":
                        if(values.length>1) {
                            List<String> tag = new ArrayList<>();
                            tag.add(values[1]);
                            request = new Request(CommandList.REMOVE_GREATER, new Pack(tag, values[2]), Client.userLogin, Client.password);
                            sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                            break;
                        } else {
                            System.out.println("Вы не ввели аргумент!");
                            check();
                        }
                    default:
                        System.out.println("Команда не распознана. Повторите ввод.");
                        this.check();
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Вы не ввели аргумент.");
            }
        }
    }






    private void update(List<String> updArg, String element) throws IOException {
        switch (element) {

            case ElementList.NAME:
                System.out.println("Введите новое имя: ");
                String name = InputClaimer.input();
                if (!name.equals("")) {
                    updArg.add(name);
                } else {
                    System.out.println("Имя не должно быть пустым.");
                    updArg.clear();
                    update(updArg, element);
                }
                break;

            case ElementList.COORDSX:
                System.out.println("Введите новые координаты (X): ");
                String X = InputClaimer.input();
                try {
                    float floatX = Float.parseFloat(X);
                    if (floatX > -316) {
                        updArg.add(X);
                    } else {
                        System.out.println("Данные по полю coordsX не верны.");
                        updArg.clear();
                        update(updArg, element);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Данные по полю coordsX не верны.");
                    updArg.clear();
                    update(updArg, element);
                }
                break;

            case ElementList.COORDSY:
                System.out.println("Введите новые координаты (Y): ");
                String Y = InputClaimer.input();
                try {
                    double doubleY = Double.parseDouble(Y);
                    updArg.add(Y);
                } catch (NumberFormatException e) {
                    System.out.println("Данные по полю coordsY не верны.");
                    updArg.clear();
                    update(updArg, element);
                }
                break;

            case ElementList.REALHERO:
                System.out.println("Этот человек герой?: (да/нет)");
                String hero = InputClaimer.input();
                hero = hero.toUpperCase();
                if (hero.equals("ДА")) {
                    updArg.add("true");
                } else if (hero.equals("НЕТ")) {
                    updArg.add("false");
                } else {
                    System.out.println("Данные по полю isRealHero не верны.");
                    updArg.clear();
                    update(updArg, element);
                }
                break;

            case ElementList.HASTOOTHPICK:
                System.out.println("У человека есть зубочистка?: (да/нет)");
                String toothPick = InputClaimer.input();
                toothPick=toothPick.toUpperCase();
                switch (toothPick) {
                    case "ДА":
                        updArg.add("true");
                        break;
                    case "НЕТ":
                        updArg.add("false");
                        break;
                    case "":
                        updArg.add("null");
                        break;
                    default:
                        System.out.println("Данные по полю hasToothpick не верны.");
                        updArg.clear();
                        update(updArg, element);
                }
                break;

            case ElementList.IMPACTSPEED:
                System.out.println("Введите новую скорость воздействия: ");
                String speed = InputClaimer.input();
                if (!speed.equals("")) {
                    try {
                        long longSpeed = Long.parseLong(speed);
                        if (longSpeed > -902) {
                            updArg.add(speed);
                        } else {
                            System.out.println("Данные по полю impactSpeed не верны.");
                            updArg.clear();
                            update(updArg, element);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Данные по полю impactSpeed не верны.");
                        updArg.clear();
                        update(updArg, element);
                    }
                } else {
                    updArg.add("null");
                }
                break;

            case ElementList.WEAPONTYPE:
                System.out.println("Введите новый тип оружия: (AXE, PISTOL, SHOTGUN, RIFLE)");
                String weapon = InputClaimer.input();
                weapon = weapon.toLowerCase();
                switch (weapon) {
                    case "axe":
                    case "pistol":
                    case "shotgun":
                    case "rifle":
                        updArg.add(weapon);
                        break;
                    default:
                        System.out.println("Данные по полю weaponType не распознаны.");
                        updArg.clear();
                        update(updArg, element);
                }
                break;

            case ElementList.MOOD:
                System.out.println("Введите новое настроение: (SADNESS, GLOOM, APATHY, CALM, RAGE)");
                String moodType = InputClaimer.input();
                moodType = moodType.toLowerCase();
                switch (moodType) {
                    case "sadness":
                    case "gloom":
                    case "apathy":
                    case "calm":
                    case "rage":
                        updArg.add(moodType);
                        break;
                    default:
                        System.out.println("Данные по полю mood не распознаны.");
                        updArg.clear();
                        update(updArg, element);
                }
                break;

            case ElementList.CARNAME:
                System.out.println("Введите новое название машины:");
                String carname = InputClaimer.input();
                if (!carname.equals("")) {
                    updArg.add(carname);
                } else {
                    System.out.println("Имя машины не должно быть пустым.");
                    updArg.clear();
                    update(updArg, element);
                }
                break;

            case ElementList.CARCOOL:
                System.out.println("Машина-то крутая? (да/нет)");
                String cool = InputClaimer.input();
                cool = cool.toUpperCase();
                if (cool.equals("ДА")) {
                    updArg.add("true");
                } else if (cool.equals("НЕТ")) {
                    updArg.add("false");
                } else {
                    System.out.println("Данные по полю carCool не верны.");
                    updArg.clear();
                    update(updArg, element);
                }
                break;

            default:
                System.out.println("Введен неверный элемент, повторите ввод.");
                this.check();
        }
    }

    private void add(List<String> arg){
        System.out.println("Введите имя: ");
        String name = InputClaimer.input();
        if (!name.equals("")) {
            arg.add(name);
        } else {
            System.out.println("Имя не должно быть пустым.");
            arg.clear();
            add(arg);
        }

        System.out.println("Введите координату X: ");
        String X = InputClaimer.input();
        try {
            float floatX = Float.parseFloat(X);
                if (floatX > -316) {
                    arg.add(X);
                } else {
                    System.out.println("Данные по полю coordsX не верны.");
                    arg.clear();
                    add(arg);
                }
        } catch (NumberFormatException e) {
            System.out.println("Данные по полю coordsX не верны.");
            arg.clear();
            add(arg);
        }

        System.out.println("Введите координату Y: ");
        String Y = InputClaimer.input();
        try {
            double doubleY = Double.parseDouble(Y);
            arg.add(Y);
        } catch (NumberFormatException e) {
            System.out.println("Данные по полю coordsY не верны.");
            arg.clear();
            add(arg);
        }

        System.out.println("Этот персонаж - герой? (да/нет)");
        String hero = InputClaimer.input().toUpperCase();
            if (hero.equals("ДА")) {
                arg.add("true");
            } else if (hero.equals("НЕТ")) {
                arg.add("false");
            } else {
                System.out.println("Данные по полю isRealHero не верны.");
                arg.clear();
                add(arg);
            }

        System.out.println("У него есть зубочистка? (да/нет)");
        String toothPick = InputClaimer.input().toUpperCase();
        switch (toothPick) {
            case "ДА":
                arg.add("true");
                break;
            case "НЕТ":
                arg.add("false");
                break;
            case "":
                arg.add("null");
                break;
            default:
                System.out.println("Данные по полю hasToothpick не верны.");
                arg.clear();
                add(arg);
        }


        System.out.println("Введите скорость воздействия:");
        String speed = InputClaimer.input();
        if (!speed.equals("")) {
            try {
                    long longSpeed = Long.parseLong(speed);
                    if (longSpeed > -902) {
                        arg.add(speed);
                    } else {
                        System.out.println("Данные по полю impactSpeed не верны.");
                        arg.clear();
                        add(arg);
                    }
            } catch (NumberFormatException e) {
                System.out.println("Данные по полю impactSpeed не верны.");
                arg.clear();
                add(arg);
            }
        } else {
            arg.add("null");
        }

        System.out.println("Выберите тип оружия: (AXE, PISTOL, SHOTGUN, RIFLE)");
        String weapon = InputClaimer.input().toLowerCase();
        switch (weapon) {
            case "axe":
            case "pistol":
            case "shotgun":
            case "rifle":
                arg.add(weapon);
                break;
            default:
                System.out.println("Данные по полю weaponType не распознаны.");
                arg.clear();
                add(arg);
        }

        System.out.println("Выберите настроение: (SADNESS, GLOOM, APATHY, CALM, RAGE)");
        String moodType = InputClaimer.input().toLowerCase();
        switch (moodType) {
            case "sadness":
            case "gloom":
            case "apathy":
            case "calm":
            case "rage":
                arg.add(moodType);
                break;
            default:
                System.out.println("Данные по полю mood не распознаны.");
                arg.clear();
                add(arg);
        }

        System.out.println("Введите название машины: ");
        String carname = InputClaimer.input();
        if (!carname.equals("")) {
            arg.add(carname);
        } else {
            System.out.println("Имя машины не должно быть пустым.");
            arg.clear();
            add(arg);
        }

        System.out.println("Машина крутая? (да/нет)");
        String cool = InputClaimer.input().toUpperCase();
        if (cool.equals("ДА")) {
            arg.add("true");
        } else if (cool.equals("НЕТ")) {
            arg.add("false");
        } else {
            System.out.println("Данные по полю carCool не верны.");
            arg.clear();
            add(arg);
        }
    }
}
