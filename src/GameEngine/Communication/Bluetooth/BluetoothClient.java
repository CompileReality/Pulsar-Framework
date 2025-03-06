package GameEngine.Communication.Bluetooth;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.Arrays;

public class BluetoothClient {

    StreamConnection conn;
    PrintWriter writer;
    String ServerAddress;
    BufferedReader reader;
    public BluetoothClient(long serverUUID) throws IOException, InterruptedException {
        final RemoteDevice[] server = new RemoteDevice[1];
        final String[] url = new String[1];
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        RemoteDevice[] devices = agent.retrieveDevices(DiscoveryAgent.CACHED);
        if (devices == null) devices = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
        for (RemoteDevice device:devices){
            agent.searchServices(null, new UUID[]{new UUID(serverUUID)}, device, new DiscoveryListener() {
                @Override
                public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {

                }

                @Override
                public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
                    for (ServiceRecord record : serviceRecords){
                        url[0] = record.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,false);
                        if (url[0] != null){
                            server[0] = device;
                        }
                    }
                }

                @Override
                public void serviceSearchCompleted(int i, int i1) {

                }

                @Override
                public void inquiryCompleted(int i) {

                }
            });

            Thread.sleep(3000);
            ServerAddress= server[0].getBluetoothAddress();
            conn = (StreamConnection) Connector.open(url[0]);
            reader = new BufferedReader(new InputStreamReader(conn.openInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(conn.openOutputStream()));
        }
    }

    public BluetoothClient(String Address,long UUID) throws IOException {
        ServerAddress = Address;
        conn = (StreamConnection) Connector.open(Address);
        reader = new BufferedReader(new InputStreamReader(conn.openInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(conn.openOutputStream()));
    }

    public void SendData(byte[] data){
        String message = Arrays.toString(data);
        writer.print(message);
        writer.flush();
    }

    public byte[] receiveData() throws IOException {
        StringBuilder data = new StringBuilder();
        String message;
        while ((message = reader.readLine()) != null){
            data.append(message);
        }
        return data.toString().getBytes();
    }
}
