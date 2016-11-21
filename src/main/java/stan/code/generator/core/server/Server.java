package stan.code.generator.core.server;

import java.util.ArrayList;

public class Server
{
    private String basePath;
    private ArrayList<ServerRequest> requests;

    public Server(String b, ArrayList<ServerRequest> r)
    {
        basePath = b;
        requests = r;
    }

    public String getBasePath()
    {
        return basePath;
    }
    public ArrayList<ServerRequest> getRequests()
    {
        return requests;
    }
}