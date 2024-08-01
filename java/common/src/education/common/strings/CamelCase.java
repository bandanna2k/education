package education.common.strings;

public abstract class CamelCase
{
    public static String toCamelCase(final String input)
    {
        boolean shouldConvertNextCharToLower = true;

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++)
        {
            char currentChar = input.charAt(i);
            if (currentChar == ' ')
            {
                shouldConvertNextCharToLower = false;
            }
            else if (shouldConvertNextCharToLower)
            {
                builder.append(Character.toLowerCase(currentChar));
            }
            else
            {
                builder.append(Character.toUpperCase(currentChar));
                shouldConvertNextCharToLower = true;
            }
        }
        return builder.toString();
    }

    public static String XtoCamelCase(final String input)
    {
        final String[] words = input.split("[\\W_]+");

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            if (i == 0)
            {
                word = word.isEmpty() ? word : word.toLowerCase();
            }
            else
            {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }
}
