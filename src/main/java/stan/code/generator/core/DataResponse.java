package stan.code.generator.core;

import java.util.ArrayList;
import java.util.HashMap;

public class DataResponse
    extends Response
{
    private ArrayList<HashMap<String, String>> data;

    public DataResponse(String t, ArrayList<HashMap<String, String>> d)
    {
        super(t);
        data = d;
    }

    public ArrayList<HashMap<String, String>> getData()
    {
        return data;
    }
}