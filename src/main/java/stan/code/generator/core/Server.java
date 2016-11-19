package stan.code.generator.core;

import java.util.ArrayList;

public class Server
{
    private String basePath;
    private ArrayList<Request> requests;

    public Server(String b, ArrayList<Request> r)
    {
        basePath = b;
        requests = r;
    }

    public String getBasePath()
    {
        return basePath;
    }
    public ArrayList<Request> getRequests()
    {
        return requests;
    }
}