package education.jackson.versioning.casestudy1;

import education.jackson.versioning.casestudy1.requests.UpsertCustomer;

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
        setupContext.setMixInAnnotations(UpsertCustomer.class, TestVersioning2.UpsertCustomerMixInVersion3.class);
    }
}
