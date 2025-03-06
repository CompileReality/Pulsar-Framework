package GameEngine.Communication.Bluetooth;

import GameEngine.AnnoLink.Function;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BluetoothHost {

    UUID id;
    String connectionURL;
    StreamConnectionNotifier notifier;
    ArrayList<ClientThread> client = new ArrayList<>();
    int numberOfClient = 0;
    public boolean StopSearchingClients = false;
    public BluetoothHost(String UUID,String Name) throws IOException {
        id = new UUID(UUID, true);
        connectionURL = "btspp://localhost:" + UUID + ";name=" + Name;
        notifier = (StreamConnectionNotifier) Connector.open(connectionURL);
    }

    public ArrayList<ClientThread> getClient() {
        return client;
    }

    public int getNumberOfClient(){
        return numberOfClient;
    }

    public void Run(Function Response)  {
        new Thread(() -> {
            while (!StopSearchingClients) {
                try{
                    StreamConnection connection = null;
                    connection = notifier.acceptAndOpen();
                    int ClientID = numberOfClient + 1;
                    numberOfClient++;
                    String address = RemoteDevice.getRemoteDevice(connection).getBluetoothAddress();
                    ClientThread clientThread = new ClientThread(connection, address, ClientID, Response);
                    client.add(ClientID, clientThread);
                    new Thread(clientThread).start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    static class ClientThread implements Runnable {

        private final StreamConnection conn;
        public final String address;
        public final int ClientID;
        private final Function response;
        private final BufferedReader read;
        private final PrintWriter writer;

        public ClientThread(StreamConnection conn,String address,int ClientID,Function response) throws IOException {
            this.address = address;
            this.ClientID = ClientID;
            this.conn = conn;
            this.response = response;


            read = new BufferedReader(new InputStreamReader(conn.openInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(conn.openOutputStream()));

        }

        public void sendData(byte[] message){
            writer.print(Arrays.toString(message));
        }
        /*
        public byte[] receiveData() throws IOException {
            StringBuilder builder = new StringBuilder();
            String message;
            while ((message = read.readLine()) != null){
                builder.append(message);
            }
            return builder.toString().getBytes();
        }
        */

        @Override
        public void run() {
            try{
                boolean RUN = true;
                while (RUN) {
                    StringBuilder builder = new StringBuilder();
                    String message;
                    while ((message = read.readLine()) != null) {
                        builder.append(message);
                    }

                    message = builder.toString();

                    message = Arrays.toString(((byte[]) response.execute(new Object[]{message.getBytes(), address, ClientID, RUN})));
                    writer.print(message);
                }

                read.close();
                writer.close();
                conn.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
