package GameEngine;

import GameEngine.AnnoLink.Function;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DebugLine{
    public enum AccessFields{
         PRIVATE,PROTECTED,PUBLIC
     }
     static class DebugObject{
         Object data;
         int FramesNumber;
         AccessFields fields;
     }

     GameFactory factory;
     Map<Integer,ArrayList<DebugObject>> logs = new HashMap<>();
     int SessionID = 0;

     String Text = "";
     JTextPane logArea;
     JTextField inputField;
     DefaultStyledDocument doc;
    int debugFileCounter = 0;

    File debugFolder;

    public  DebugLine(GameFactory GameFactory,JFrame frame) {
        factory = GameFactory;
        frame.setLayout(new BorderLayout());
        File dir = new File(factory.ResourceFolder.getAbsoluteFile()+"\\Debug Analysis\\");
        dir.mkdir();
        debugFolder = dir;

        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,15));
        doc = new DefaultStyledDocument();
        logArea.setDocument(doc);
        frame.add(new JScrollPane(logArea),BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,15));
        inputField.addActionListener(e -> {
            Input(inputField.getText());
            inputField.setText("");
        });
        frame.add(inputField,BorderLayout.SOUTH);

        if (factory.ResourceFolder != null) {
            try {
                debugFileCounter = Objects.requireNonNull(debugFolder.listFiles()).length;
            }catch (NullPointerException e){
                debugFileCounter = 0;
            }

        }
    }

    public int CreateSessionID(){
        if (SessionID != 0) {
            SessionID++;
        }
         return SessionID;
     }
    public int LogData(int SessionID,Object data,AccessFields fields){
        int ID;
        DebugObject obj = new DebugObject();
        obj.FramesNumber = GameFactory.FrameCounter;
        obj.data = data;
        obj.fields = fields;

        ArrayList<DebugObject> objects = new ArrayList<>();
        String Message = new Gson().toJson(obj);

        if (logs.containsKey(SessionID)){
            ID = SessionID;
            if (obj != logs.get(ID).get(logs.get(ID).size()-1)) {
                objects = logs.get(ID);
                objects.add(obj);
                logs.put(ID, objects);
            }
        }else{
            ID = CreateSessionID();
            objects.add(obj);
            logs.put(ID,objects);
        }
        UpdateDebugLine(Message,0);

        return ID;
    }

     public void LogException(String cause){
        UpdateDebugLine(cause,2);
        factory.status = GameFactory.EXCEPTIONAL_PAUSE;
     }

     public void LogError(String cause){
        UpdateDebugLine(cause,1);
        factory.status = GameFactory.EXCEPTIONAL_PAUSE;
     }

     private void UpdateDebugLine(String message,int Type){
         switch (Type) {
             case 0 -> appendText("[LOGGED_DATA]  \n" + message + "\n", Color.WHITE);
             case 1 -> appendText("[ERROR]  " + message + "\n", Color.RED);
             case 2 -> appendText("[EXCEPTION]  " + message + "\n", Color.YELLOW);
             case 3 -> appendText("[INPUT]  " + message + "\n", Color.CYAN);
             case 4 -> appendText("[OUTPUT] \n"+message+"\n",Color.WHITE);
         }
     }

     private void appendText(String text,Color color){
        try{
            this.Text = this.Text + text;
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet attributeSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,color);
            doc.insertString(doc.getLength(),text,attributeSet);
        } catch (BadLocationException e) {
            LogException(e.getMessage());
            e.printStackTrace();
        }
     }

     public ArrayList<Object> accessLog(int SessionID){
         ArrayList<Object> objects = new ArrayList<>();
        if (logs.get(SessionID).get(0).fields != AccessFields.PRIVATE){
            for (DebugObject obj:logs.get(SessionID)){
                objects.add(obj.data);
            }
            return objects;
        }
            return null;
     }

     public ArrayList<Object> getAllLogs(ArrayList<Integer> SessionIDs){
        ArrayList<Object> obj = new ArrayList<>();
         for (int i = 0; i < logs.size(); i++) {
             if (logs.get(i) != null) {
                 for (DebugObject objects : logs.get(i)) {
                     if (objects.fields == AccessFields.PRIVATE || (objects.fields == AccessFields.PROTECTED && !SessionIDs.contains(i))) {
                         break;
                     } else if (objects.fields == AccessFields.PUBLIC || SessionIDs.contains(i)) {
                         obj.add(objects.data);
                     }
                 }
             }
         }
         return obj;
     }

     private void ExecuteDCF(String dcf){
        String Dcf = dcf.substring(5);
        String[] DcfList = Dcf.split("/");
        int i=0;
        String item = DcfList[0];
            switch (item) {
                case "SaveInstance" :
                    SaveLogInstance();
                    break;
                case "SaveLogs" :
                    SaveLogs();
                    break;
                case "LogData" :
                    LogData(1000, DcfList[i + 1], AccessFields.valueOf(DcfList[i + 2]));
                    break;
                case "GetLogs" :
                    UpdateDebugLine(new Gson().toJson(getAllLogs(new ArrayList<>())),4);
                    break;
                case "AccessLog" :
                    UpdateDebugLine(new Gson().toJson(accessLog(Integer.parseInt(DcfList[i+1]))),4);
                    break;
                case "Pause":
                    factory.status = GameFactory.PAUSED;
                    break;
                case "Restart":
                    factory.status = GameFactory.RUNNING;
                    break;
                case "Destroy":
                    factory.destroy();
                    break;
                case "EnterDebugMode":
                    factory.debugMode = true;
                    break;
                case "ExitDebugMode":
                    factory.debugMode = false;
                    break;
                case "CustomFunction" :
                    ArrayList<Object> param = new ArrayList<>(Arrays.asList(DcfList).subList(i + 3, DcfList.length));
                    try {
                        new Function(DcfList[i + 1], DcfList[i + 2]).execute(param.toArray());
                    }catch (Exception e){
                        LogException(e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
     }

     public void SaveLogInstance(){
        File file = new File(debugFolder.getAbsoluteFile()+"\\debug_"+debugFileCounter+".txt");
         try(FileWriter writer = new FileWriter(file)){
            UpdateDebugLine("Debug Text saved in "+debugFolder.getAbsoluteFile()+"\\debug_"+debugFileCounter+".txt",4);
            writer.write(Text);
            Text = "";
            writer.close();
            debugFileCounter++;
        }catch (Exception e){
             LogException(e.getMessage());
             e.printStackTrace();
         }
     }

     public void SaveLogs() {
        File file = new File(debugFolder.getAbsoluteFile()+"\\debug_"+debugFileCounter+".json");
        try(FileWriter writer = new FileWriter(file)){
            UpdateDebugLine("Debug Logs saved in "+debugFolder.getAbsoluteFile()+"\\debug_"+debugFileCounter+".json",4);
            writer.write(new Gson().toJson(logs));
            logs = new HashMap<>();
            debugFileCounter++;
        }catch (Exception e){
            LogException(e.getMessage());
            e.printStackTrace();
        }
     }

     private void Input(String dcf){
         UpdateDebugLine(dcf, 3);
        if (dcf.contains("dl://")) {
            ExecuteDCF(dcf);
        }else {
            UpdateDebugLine("Invalid Debug Command Format (DCF) text",1);
        }
     }
}
