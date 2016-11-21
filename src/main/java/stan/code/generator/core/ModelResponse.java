package stan.code.generator.core;

public class ModelResponse<MODEL>
    extends Response
{
    private MODEL model;

    public ModelResponse(String t, MODEL m)
    {
        super(t);
        model = m;
    }

    public String getModel()
    {
        return model;
    }
}