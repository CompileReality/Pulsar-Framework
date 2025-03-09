# 2. DebugLine
**DebugLine** is advanced Debug system having features like inter-class communication, saving logged data and instance as file, GUI based interaction.

---

### Inter-class communication:
The DebugLine's instance can be created using GameFactory's `RegistorDebugLine()` and `getDebugLine()` methods.
DebugLine has public enum for security of logged data as it can be accessed by other class.
But it will be displayed on GUI. That means even if looged data is private, it will be displayed on GUI.
The enum is :
```java
     public enum AccessFields{
         PRIVATE,PROTECTED,PUBLIC
     }
```
The PRIVATE sets logged data to private and can't be accessed by any other class, PROTECTED set's logged data to protected and only be accessed when passed correct SessionID, PUBLIC sets logged data to publicly available to all class and can be accessed using SessionID also.
>**Note:** The SessionID is ID assigned to every logged data which is returned when data is logged.

When DebugLine is initialized, a new sub window is created for displaying the UI for controlling the game and it's components. This window is created by GameFactory.
To log data, there are two ways : 
1. Manually
2. Dynamically

By using **DCF (Debug Command Format)** for logging data is Manual method and Dynamically logging data can be achieved using this method :
``` java
  public int LogData(int SessionID,Object data,AccessFields fields)
```
Here, Variable `SessionID` used only when there's already logged data of same instance but want to log updated instance or updated value without creating another SessionID for it. This allows to access the updated version of that instance without changing the SessionID. Variable `data` is data which is to be logged and `fields` sets the access level for data.
>**Note:** If new data is to be logged then the variable `SessionID` should be set to the value which is not equal to any session id of data logged till now or just set it equal to 10000. As the session id cannot reach till 10000, this ensures that it is new data and new SessionID will be assigned to it.

The returned value will be new SessionID for new data and same value as SessionID of previous data for updated data.
This logs the data and a message displayed on GUI in json having information of at wich frame it is logged and access level of data and data.
>**Note:** Every Class or object can be logged as data but it should be serializable

To access logged data we can use either :
```java
public ArrayList<Object> accessLog(int SessionID)
```
or :
```java
public ArrayList<Object> getAllLogs(ArrayList<Integer> SessionIDs)
```
The above two methods can be used to access logs. The first method is used to access specific data with it's updated version in a list. This list is then returned. But even if we used correct SessionID and data is set to private then the list returned will be null.
The second methods allow to access all log in single list, the Variable `SessionIDs` is used to include the protected data using provided ids.The order of Ids doesn't matters and all data including Updated data are included in list which is later to be returned.
This all methods will help in inter-class communication.

---

### Saving as file:
To save the text displayed by this class on GUI, we can use:
```java
  public void SaveLogInstance()
```
This method will create a file of txt type and save it under Debug Analysis folder under Resource folder which is assigned in `GameProperty` before creating instance of `GameFactory`.

To save the logged data,  we can use :
```java
   public void SaveLogs()
```
This method will save all logs in json format in same path as above.

---

### Error and exception handling:

To display the error or any exception on GUI, we can use :

For Error:
```java
public void LogError(String cause)
```
For Exception:
```java
public void LogException(String cause)
```
This method will show error and exception on GUI.
