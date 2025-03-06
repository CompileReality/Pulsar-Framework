package GameEngine.AnnoLink;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class Function {

    public String KEY;
    private transient final Object className;
    private boolean StringClass = false;

    public Function(String key,Object Class){
        if (!(Class instanceof String)) {
            this.className = Class;
        }else {
            this.className = (String) Class;
            this.StringClass = true;
        }
        this.KEY = key;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(((Function) obj).KEY, this.KEY) && Objects.equals(((Function) obj).className, this.className);
    }

    public Object execute(Object[] param)  {
        Object result = null;
        if (StringClass) {
            try {
                for (Method method : Class.forName((String) className).getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Executable.class)) {
                        Executable arg = method.getAnnotation(Executable.class);
                        if (Objects.equals(arg.key(), this.KEY)) {
                            try {
                                result = method.invoke( Class.forName((String) className).getDeclaredConstructor().newInstance(), param);
                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                                     InstantiationException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            for(Method method:(className).getClass().getDeclaredMethods()){
                if (method.isAnnotationPresent(Executable.class)){
                    Executable arg = method.getAnnotation(Executable.class);
                    if (Objects.equals(arg.key(), this.KEY)){
                        try {
                            result = method.invoke((className),param);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return result;
    }

}
