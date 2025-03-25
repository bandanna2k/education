package education.jackson.versioning.casestudy1;

import education.jackson.versioning.casestudy1.requests.UpsertCustomer;

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
        setupContext.setMixInAnnotations(UpsertCustomer.class, TestVersioning2.UpsertCustomerMixInVersion2.class);
    }
}
