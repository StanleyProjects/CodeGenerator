package stan.code.generator.core;

import java.util.ArrayList;

public class Model
{
    private String name;
    private ArrayList<Item> data;

    public Model(String n, ArrayList<Item> d)
    {
        name = n;
        data = d;
    }

    public String getName()
    {
        return name;
    }
    public ArrayList<Item> getData()
    {
        return data;
    }
}