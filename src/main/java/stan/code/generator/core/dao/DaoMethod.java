package stan.code.generator.core.dao;

import stan.code.generator.core.Response;

public class DaoMethod
{
    private String name;
    private Response request;
    private Response response;

    public DaoMethod(String n, Response rq, Response rs)
    {
        name = n;
        request = rq;
        response = rs;
    }

    public String getName()
    {
        return name;
    }
    public Response getRequests()
    {
        return request;
    }
    public Response getResponses()
    {
        return response;
    }
}