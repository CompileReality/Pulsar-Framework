package GameEngine.Communication.UDP;

import GameEngine.AnnoLink.Function;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

public class UDPServer {

    ArrayList<SocketAddress> ClientAddress = new ArrayList<>();
    public void Run(int PORT, Function ProcessMessage) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(PORT));

        boolean RUN = true;
        while (RUN){
            buffer.clear();

            SocketAddress clientAddress = channel.receive(buffer);
            buffer.flip();

            for (SocketAddress client: ClientAddress) {
                if (!(client.equals(clientAddress))){
                    ClientAddress.add(client);
                }
            }

            byte[] message = buffer.array();
            String messag = buffer.toString();
            message = (byte[]) ProcessMessage.execute(new Object[]{message,messag,RUN});
            buffer.clear();
            buffer.put(message);
            buffer.flip();

            channel.send(buffer,clientAddress);
        }

        channel.disconnect();


    }

    public ArrayList<SocketAddress> getClients(){
        return ClientAddress;
    }

    public void setClient(ArrayList<SocketAddress> clients){
        this.ClientAddress = clients;
    }

    public void SendData(int PORT,byte[] data, SocketAddress Client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(PORT));

        buffer.clear();
        buffer.put(data);
        buffer.flip();

        channel.send(buffer,Client);
    }

    public byte[] ReceiveData(int PORT) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(PORT));

        buffer.clear();

        SocketAddress clientAddress = channel.receive(buffer);
        buffer.flip();

        for (SocketAddress client : ClientAddress) {
            if (!(client.equals(clientAddress))) {
                ClientAddress.add(client);
            }
        }

        return buffer.array();

    }
}
