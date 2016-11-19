package stan.code.generator.core;

public class Request
{
    private String url;
    private String type;

    public Request(String u, String t)
    {
        url = u;
        type = t;
    }

    public String getUrl()
    {
        return url;
    }
    public String getType()
    {
        return type;
    }
}