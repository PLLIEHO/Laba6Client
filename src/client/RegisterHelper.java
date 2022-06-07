package client;

import common.CommandList;
import common.Pack;
import common.Request;
import common.Serializer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class RegisterHelper {
    public boolean register(InetAddress address, DatagramSocket socket) throws IOException {
        System.out.println("Вы уже зарегестрированы? (да/нет)");
        String answer = InputClaimer.input().toLowerCase();
        if(answer.equals("да")) {
            System.out.println("Введите логин...");
            String login = InputClaimer.input();
            System.out.println("Введите пароль...");
            String password = InputClaimer.input();
            Serializer serializer = new Serializer();
            List<String> loginArgs = new ArrayList<>();
            loginArgs.add(login);
            loginArgs.add(password);
            Client.userLogin = login;
            Client.password = password;
            Sender sender = new Sender();
            sender.send(serializer.serialize(new Request(CommandList.SIGN_IN, new Pack(loginArgs, null), Client.userLogin, Client.password)).toByteArray(), address, socket);
            return true;
        } else if(answer.equals("нет")){
            System.out.println("Введите новый логин...");
            String login = InputClaimer.input();
            System.out.println("Введите новый пароль...");
            String password = InputClaimer.input();
            Serializer serializer = new Serializer();
            List<String> loginArgs = new ArrayList<>();
            loginArgs.add(login);
            loginArgs.add(password);
            Client.userLogin = login;
            Client.password = password;
            Sender sender = new Sender();
            sender.send(serializer.serialize(new Request(CommandList.SIGN_UP, new Pack(loginArgs, null), Client.userLogin, Client.password)).toByteArray(), address, socket);
            return true;
        } else {
            System.out.println("Ответ не распознан.");
            register(address, socket);
            return false;
        }
    }
}
