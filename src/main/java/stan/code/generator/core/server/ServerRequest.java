package stan.code.generator.core.server;

public class ServerRequest
{
    private String url;
    private String type;

    public ServerRequest(String u, String t)
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