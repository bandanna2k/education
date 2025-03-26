package education.jackson.versioning.casestudyByBuilder;

import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerBuilder;
import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerMixIn_V3;

public class JsonModuleVersion3 extends JsonModuleVersion2
{
    @Override
    protected int getMajor()
    {
        return 3;
    }

    @Override
    protected void addMixInForUpsertCustomer(SetupContext setupContext)
    {
        setupContext.setMixInAnnotations(UpsertCustomerBuilder.class, UpsertCustomerMixIn_V3.class);
    }
}
