package stan.code.generator.core;

public abstract class Response
{
    private String type;

    public Response(String t)
    {
        type = t;
    }

    public String getType()
    {
        return type;
    }
}