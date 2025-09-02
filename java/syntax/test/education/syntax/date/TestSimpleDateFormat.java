package education.syntax.date;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSimpleDateFormat
{
    public static final String FORMAT_THAT_NO_ONE_SHOULD_USE_BUT_WE_DID = "MMM d, yyyy h:mm:ss a";
    private static List<String> NEW_DATES = List.of(
            "Jan 30, 2025 12:04:38 AM",
            "Feb 21, 2025 12:04:38 AM",
            "Mar 30, 2025 12:04:38 AM",
            "Apr 30, 2025 12:04:38 AM",
            "May 30, 2025 12:04:38 AM",
            "Jun 30, 2025 12:04:38 AM",
            "Jul 30, 2025 12:04:38 AM",
            "Aug 30, 2025 12:04:38 AM",
            "Sept 1, 2025 12:04:38 AM",
            "Oct 30, 2025 12:04:38 AM",
            "Nov 30, 2025 12:04:38 AM",
            "Dec 30, 2025 12:04:38 AM"
    );
    private SimpleDateFormat sdf;

    @Before
    public void setUp()
    {
        sdf = new SimpleDateFormat(FORMAT_THAT_NO_ONE_SHOULD_USE_BUT_WE_DID);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void shouldParseDate() throws ParseException
    {
        for (String s : NEW_DATES)
        {
            final Date parse = sdf.parse(s);
            System.out.println(parse);
            Assertions.assertThat(parse).isNotNull();
        }
    }

    @Test
    public void shouldNotParseDate()
    {
        Assertions.assertThatExceptionOfType(ParseException.class)
                .isThrownBy(() -> sdf.parse("Sep 1, 2025 12:04:38 AM"));
    }



    private static final List<String> DATES = List.of(
            "Jan 30, 2025 12:04:38 AM",
            "Feb 21, 2025 12:04:38 AM",
            "Mar 30, 2025 12:04:38 AM",
            "Apr 30, 2025 12:04:38 AM",
            "May 30, 2025 12:04:38 AM",
            "Jun 30, 2025 12:04:38 AM",
            "Jul 30, 2025 12:04:38 AM",
            "Aug 30, 2025 12:04:38 AM",
            "Sep 1, 2025 12:04:38 AM",
            "Oct 30, 2025 12:04:38 AM",
            "Nov 30, 2025 12:04:38 AM",
            "Dec 30, 2025 12:04:38 AM"
    );

    @Test
    public void accountDetailsDateParserShouldParse() throws ParseException
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_THAT_NO_ONE_SHOULD_USE_BUT_WE_DID);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (final String s : DATES)
        {
            final Date parse = sdf.parse(s);
            System.out.println(parse);
            assertThat(parse).isNotNull();
        }
    }

    @Test
    public void userProfileDateParserShouldParse() throws ParseException
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_THAT_NO_ONE_SHOULD_USE_BUT_WE_DID);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (final String s : DATES)
        {
            final Date parse = sdf.parse(s);
            System.out.println(parse);
            assertThat(parse).isNotNull();
        }
    }
}
