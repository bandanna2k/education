package education.jackson.versioning.casestudyByBuilder;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import education.jackson.versioning.casestudyByBuilder.pojos.UpsertCustomerBuilder;

public class JsonModuleVersion1 extends Module
{
    @Override
    public final String getModuleName()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public final Version version()
    {
        return new Version(getMajor(), 0, 0, null, null, null);
    }

    protected int getMajor()
    {
        return 1;
    }

    @Override
    public void setupModule(SetupContext setupContext)
    {
        addMixInForUpsertCustomer(setupContext);
    }

    protected void addMixInForUpsertCustomer(SetupContext setupContext)
    {
        setupContext.setMixInAnnotations(UpsertCustomerBuilder.class, UpsertCustomerMixIn.class);
    }
}
