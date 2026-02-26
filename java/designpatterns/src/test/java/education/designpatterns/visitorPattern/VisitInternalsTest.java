package education.designpatterns.visitorPattern;

import org.junit.jupiter.api.Test;
import education.designpatterns.differentPackage.Account;
import education.designpatterns.differentPackage.AccountVisitor;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(cpv.getBalance()).isEqualTo(10);
    }
}
