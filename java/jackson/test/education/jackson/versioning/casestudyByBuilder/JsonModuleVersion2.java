package education.jackson.versioning.casestudyByBuilder;

import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerBuilder;
import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerMixIn_V2;

public class JsonModuleVersion2 extends JsonModuleVersion1
{
    @Override
    protected int getMajor()
    {
        return 2;
    }

    @Override
    protected void addMixInForUpsertCustomer(SetupContext setupContext)
    {
        setupContext.setMixInAnnotations(UpsertCustomerBuilder.class, UpsertCustomerMixIn_V2.class);
    }
}
