package education.syntax.transientKeyword;

import java.io.Serializable;

class MyTestClass implements Serializable
{
    String A;
    String B;
    transient String C;
    transient String D;

    void setValues()
    {
        A = "A";
        B = "B";
        C = "C";
        D = "D";
    }
}
