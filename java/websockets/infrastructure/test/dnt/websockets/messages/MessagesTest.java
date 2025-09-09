package dnt.websockets.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class MessagesTest
{
    @Test
    public void visitorShouldHandleAllMessages()
    {
        subclasses("dnt.websockets")
//                .stream().filter(aClass -> !aClass.getSimpleName().contains("Abstract"))
                .forEach(aClass -> {
            System.out.println(aClass);
        });

        System.out.println("-------------");

        Method[] allMethods = getAllMethods(MessageVisitor.class);
        Arrays.stream(allMethods)
                .filter(method -> method.getName().endsWith("visit"))
                .forEach(method -> {
                    Class<?> parameterType = method.getParameterTypes()[1];
                    System.out.println(parameterType);
                });
    }

    public Method[] getAllMethods(Class<?> interfaceClass)
    {
        if (!interfaceClass.isInterface())
        {
            throw new IllegalArgumentException("Provided class is not an interface");
        }
        return interfaceClass.getMethods();
    }

    public Set<Class<? extends AbstractMessage>> subclasses(String packageName)
    {
        Class<AbstractMessage> baseClass = AbstractMessage.class;

        // Verify the base class has @JsonTypeInfo
        if (!baseClass.isAnnotationPresent(JsonTypeInfo.class))
        {
            throw new IllegalArgumentException("Base class must have @JsonTypeInfo annotation");
        }

        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(baseClass);
    }
}
