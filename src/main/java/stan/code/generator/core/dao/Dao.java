package stan.code.generator.core.dao;

import java.util.ArrayList;

public class Dao
{
    private String model;
    private ArrayList<DaoMethod> methods;

    public Dao(String m, ArrayList<DaoMethod> mt)
    {
        model = m;
        methods = mt;
    }

    public String getModel()
    {
        return model;
    }
    public ArrayList<DaoMethod> getMethods()
    {
        return methods;
    }
}