package client;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Client {
    private final DatagramSocket socket = new DatagramSocket();
    private InetAddress address;
    public static int PORT;
    public static String userLogin;

    public Client() throws UnknownHostException, SocketException {
    }

    public static void main(String[] args) throws IOException {
        try{
            Client client = new Client();
            client.start();
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Невозможно подключиться к серверу.");
            System.exit(1);
        }
    }
    public void start() throws IOException {
        System.out.println("Введите IP адрес сервера...");
        String addressStr = InputClaimer.input();
        if(!addressStr.equals("localhost")){
            try {
                long checkAddr = Long.parseLong(addressStr);
            } catch (NumberFormatException e){
                System.out.println("Адрес введен неверно. Повторите ввод.");
                start();
            }
        }
        address = InetAddress.getByName(addressStr);
        try {
            System.out.println("Введите порт...");
            String port = InputClaimer.input();
            if(Integer.parseInt(port)<65535&&Integer.parseInt(port)>0){
                Client.PORT = Integer.parseInt(port);
            } else{
                System.out.println("Порт введен неверно. Повторите ввод.");
                start();
            }
        } catch (NumberFormatException p){
            System.out.println("Порт введен неверно. Повторите ввод.");
            start();
        }
        new RegisterHelper().register(address, socket);

        CommandChecker checker = new CommandChecker(address, socket);
        checker.check();
    }
}
