package GameEngine.Communication.UDP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPClient {

    String ip;
    int port;
    DatagramChannel channel;
    InetSocketAddress serverAddress;

    public UDPClient(String serverIP,int PORT) throws IOException {
        this.ip = serverIP;
        this.port = PORT;

        channel = DatagramChannel.open();
        serverAddress = new InetSocketAddress(serverIP,PORT);
    }

    public static UDPClient setServerByName(String name, int PORT) throws IOException {
       return new UDPClient(InetAddress.getByName(name).getHostAddress(),PORT);
    }

    public static UDPClient setLocalHostServer(int PORT) throws IOException {
        return new UDPClient(InetAddress.getLocalHost().getHostAddress(), PORT);
    }

    public void ChangeServer(String IP,int PORT){
        serverAddress = new InetSocketAddress(IP,PORT);
        this.ip = IP;
        this.port = PORT;
    }

    public String getIP(){
        return ip;
    }

    public int getPORT(){
        return port;
    }

    public void send(byte[] data) throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(1024);
        buff.put(data);
        buff.flip();
        channel.send(buff,serverAddress);
    }

    public byte[] receive() throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(1024);
        channel.receive(buff);
        return buff.flip().array();
    }
}
