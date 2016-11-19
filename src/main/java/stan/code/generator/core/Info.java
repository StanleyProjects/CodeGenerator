package stan.code.generator.core;

public class Info
{
    private String description;
    private String version;
    private String title;

    public Info(String d, String v, String t)
    {
        description = d;
        version = v;
        title = t;
    }

    public String getDescription()
    {
        return description;
    }
    public String getVersion()
    {
        return version;
    }
    public String getTitle()
    {
        return title;
    }
}