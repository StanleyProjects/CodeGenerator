package stan.code.generator.core;

public class Item
{
    private String name;
    private String type;

    public Item(String n, String t)
    {
        name = n;
        type = t;
    }

    public String getName()
    {
        return name;
    }
    public String getType()
    {
        return type;
    }
}