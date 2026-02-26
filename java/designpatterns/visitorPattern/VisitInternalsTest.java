package education.designpatterns.visitorPattern;

import education.designpatterns.visitorPattern.differentPackage.Account;
import education.designpatterns.visitorPattern.differentPackage.AccountVisitor;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class VisitInternalsTest
{
    @Test
    public void testLedger()
    {
        final Account account = new Account();
        final AccountVisitor cpv = new AccountVisitor();

        account.accept(cpv);
        // [David] I have no access to account.getBalance();

        System.out.println(cpv.getBalance());
        assertThat(cpv.getBalance(),is(equalTo(10)));
    }
}
