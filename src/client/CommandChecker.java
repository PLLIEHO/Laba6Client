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
                        request = new Request(CommandList.HELP, null);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "info":
                        request = new Request(CommandList.INFO, null);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "show":
                        request = new Request(CommandList.SHOW, null);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "add":
                        List<String> arg = new ArrayList<>();
                        add(arg);
                        request = new Request(CommandList.ADD, new Pack(arg, null));
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "update":
                        String element = values[2];
                        List<String> updArg = new ArrayList<>();
                        updArg.add(element);
                        update(updArg, element);
                        request = new Request(CommandList.UPDATE, new Pack(updArg, values[1]));
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "remove_by_id":
                        request = new Request(CommandList.REMOVE_BY_ID, new Pack(null, values[1]));
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
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
                        request = new Request(CommandList.EXECUTE, new Pack(null, values[1]));
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "add_if_max":
                        if (values.length > 1) {
                            List<String> argMax = new ArrayList<>();
                            add(argMax);
                            request = new Request(CommandList.ADD_IF_MAX, new Pack(argMax, values[1]));
                            sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        } else {
                            System.out.println("Вы не ввели аргументы!");
                        }
                        break;
                    case "max_by_real_hero":
                        request = new Request(CommandList.MAX_BY_REAL_HERO, null);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "history":
                        request = new Request(CommandList.HISTORY, null);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "filter_contains_name":
                        request = new Request(CommandList.FILTER, new Pack(null, values[1]));
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "print_descending":
                        request = new Request(CommandList.PRINT_DESCENDING, null);
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
                    case "exit":
                        request = new Request(CommandList.EXIT, null);
                        sender.exitFlag = true;
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        socket.close();
                        System.exit(0);
                        break;
                    case "remove_greater":
                        List<String> tag = new ArrayList<>();
                        tag.add(values[1]);
                        request = new Request(CommandList.REMOVE_GREATER, new Pack(tag, values[2]));
                        sender.send(serializer.serialize(this.request).toByteArray(), address, socket);
                        break;
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
                updArg.add(InputClaimer.input());
                break;
            case ElementList.COORDSX:
                System.out.println("Введите новые координаты (X): ");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.COORDSY:
                System.out.println("Введите новые координаты (Y): ");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.REALHERO:
                System.out.println("Этот человек герой?: (да/нет)");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.HASTOOTHPICK:
                System.out.println("У человека есть зубочистка?: (да/нет)");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.IMPACTSPEED:
                System.out.println("Введите новую скорость воздействия: ");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.WEAPONTYPE:
                System.out.println("Введите новый тип оружия: (AXE, PISTOL, SHOTGUN, RIFLE)");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.MOOD:
                System.out.println("Введите новое настроение: (SADNESS, GLOOM, APATHY, CALM, RAGE)");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.CARNAME:
                System.out.println("Введите новое название машины:");
                updArg.add(InputClaimer.input());
                break;
            case ElementList.CARCOOL:
                System.out.println("Машина-то крутая? (да/нет)");
                updArg.add(InputClaimer.input());
                break;
            default:
                System.out.println("Введен неверный элемент, повторите ввод.");
                this.check();
        }
    }

    private void add(List<String> arg){
        System.out.println("Введите имя: ");
        arg.add(InputClaimer.input());
        System.out.println("Введите координату X: ");
        arg.add(InputClaimer.input());
        System.out.println("Введите координату Y: ");
        arg.add(InputClaimer.input());
        System.out.println("Этот персонаж - герой? (да/нет)");
        arg.add(InputClaimer.input());
        System.out.println("У него есть зубочистка? (да/нет)");
        arg.add(InputClaimer.input());
        System.out.println("Введите скорость воздействия:");
        arg.add(InputClaimer.input());
        System.out.println("Выберите тип оружия: (AXE, PISTOL, SHOTGUN, RIFLE)");
        arg.add(InputClaimer.input());
        System.out.println("Выберите настроение: (SADNESS, GLOOM, APATHY, CALM, RAGE)");
        arg.add(InputClaimer.input());
        System.out.println("Введите название машины: ");
        arg.add(InputClaimer.input());
        System.out.println("Машина крутая? (да/нет)");
        arg.add(InputClaimer.input());
    }
}
