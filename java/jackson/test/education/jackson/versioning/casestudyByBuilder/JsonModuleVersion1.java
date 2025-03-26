package education.jackson.versioning.casestudyByBuilder;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import education.jackson.versioning.casestudyByBuilder.converters.AnotherRequestBuilder;
import education.jackson.versioning.casestudyByBuilder.converters.AnotherRequestMixIn;
import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerBuilder;
import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerMixIn;

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
        addMixInForAnotherRequest(setupContext);
    }

    protected void addMixInForUpsertCustomer(SetupContext setupContext)
    {
        setupContext.setMixInAnnotations(UpsertCustomerBuilder.class, UpsertCustomerMixIn.class);
    }
    protected void addMixInForAnotherRequest(SetupContext setupContext)
    {
        setupContext.setMixInAnnotations(AnotherRequestBuilder.class, AnotherRequestMixIn.class);
    }
}
