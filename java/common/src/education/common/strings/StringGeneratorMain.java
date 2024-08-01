package education.common.strings;

import java.util.*;
import java.util.function.Consumer;

public class StringGeneratorMain
{
    private Optional<Calendar> calendar = Optional.empty();
    private int selectedMenuItem = -1;
    private List<MenuItem> menuItems;

    public static void main(String[] args) {
        StringGeneratorMain main = new StringGeneratorMain();
        main.init();
        main.start();
    }

    private void init()
    {
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(1, "Set time", mi -> calendar = Optional.of(Calendar.getInstance())));
        menuItems.add(new MenuItem(2, "Show time", mi -> System.out.println(calendar)));
        menuItems.add(new MenuItem(0, "Exit", mi -> System.out.println("Exiting")));
    }

    private void start()
    {
        while(selectedMenuItem != 0)
        {
            for(MenuItem mi : menuItems)
            {
                System.out.println(String.format("%d) %s", mi.id, mi.text));
            }

            try
            {
                final String input = new Scanner(System.in).nextLine();
                selectedMenuItem = Integer.parseInt(input);
            }
            catch(Exception ex) {}

            menuItems.stream().filter(mi -> mi.id == selectedMenuItem).forEach(mi -> mi.action.accept(null));
        }
    }

    private static class MenuItem
    {
        final int id;
        final String text;
        final Consumer action;

        private MenuItem(int id, String text, Consumer action)
        {
            this.id = id;
            this.text = text;
            this.action = action;
        }
    }
}
