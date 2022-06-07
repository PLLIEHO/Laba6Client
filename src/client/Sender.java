package client;



import common.Answer;
import common.Pack;
import common.Request;
import common.Serializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;


public class Sender {
    public boolean exitFlag = false;

    public void send(byte[] byteOutput, InetAddress address, DatagramSocket socket) {

        DatagramPacket toServer = new DatagramPacket(byteOutput, byteOutput.length, address, Client.PORT);
        DatagramPacket packetIn = new DatagramPacket(new byte[Serializer.SIZE], Serializer.SIZE);
        try {

                System.out.println("Пакет отправлен, ждите.");
                for(int i = 0; i<11; i++){
                    try {
                        socket.setSoTimeout(0);
                        socket.send(toServer);
                        socket.setSoTimeout(1000);
                        socket.receive(packetIn);

                        System.out.println("Пакет доставлен.");
                        Answer answer = (Answer) new Serializer().deserialize(packetIn.getData());

                        Serializer serializer = new Serializer();
                        byte[] byteApproval = serializer.serialize(new Request(null, new Pack(null, approvalCreator(toServer)), Client.userLogin)).toByteArray();
                        socket.send(new DatagramPacket(byteApproval, byteApproval.length, address, Client.PORT));
                        System.out.println("Подтверждение отправлено.");
                        System.out.println(answer.getStr());
                        loginApproval(answer, address, socket);
                        break;
                    } catch (SocketTimeoutException e){
                        if(i!=10){
                        continue;}
                        else{
                            socket.close();
                            if(exitFlag){
                                System.exit(0);
                            }
                            System.out.println("Сервер недоступен. Попробуйте ввести другой порт и IP.");
                            Client client = new Client();
                            client.start();
                        }
                    }
                }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private String approvalCreator(DatagramPacket toServer){
        return toServer.getAddress().getHostAddress();
    }

    private void loginApproval(Answer answer, InetAddress address, DatagramSocket socket) throws IOException {
        if(answer.getStr().equals("Entry denied! Password is wrong")){
            Client.userLogin = null;
            new RegisterHelper().register(address, socket);
        } else if(answer.getStr().equals("No such user detected. Register yourself firstly.")){
            Client.userLogin = null;
            new RegisterHelper().register(address, socket);
        } else if(answer.getStr().equals("This login was already taken. Try again")) {
            Client.userLogin = null;
            new RegisterHelper().register(address, socket);
        }
    }
}
