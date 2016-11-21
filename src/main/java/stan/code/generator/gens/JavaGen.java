package stan.code.generator.gens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import stan.code.generator.core.*;
import stan.code.generator.core.server.*;

public class JavaGen
{
    static public String getFileName(String modelName)
    {
        return getClassName(modelName) + ".java";
    }

    static public String generate(String pcg, Model model)
    {
        String className = getClassName(model.getName());
        ArrayList<HashMap<String, String>> fields = new ArrayList<>();
        for(Item item : model.getData())
        {
            String type = getTypeField(item);
            if(type == null)
            {
                continue;
            }
            HashMap<String, String> field = new HashMap<>();
            field.put("name", toCamelCase(item.getName()));
            field.put("type", type);
            fields.add(field);
        }
        String code = "package "+pcg+";\n\n";
        code += "public class " + className + "\n";
        code += "{" + "\n";
        for(HashMap<String, String> field : fields)
        {
            code += "\tprivate " + field.get("type") + " " + field.get("name") + ";\n";
        }
        if(fields.size() == 0)
        {
            code += "}";
            return code;
        }
        code += "\n";
        code += "\tpublic " + className + "(";
        for(int i=0; i<fields.size(); i++)
        {
            code += fields.get(i).get("type") + " " + fields.get(i).get("name");
            if(i+1<fields.size())
            {
                code += ", ";
            }
        }
        code += ")\n";
        code += "\t{" + "\n";
        for(HashMap<String, String> field : fields)
        {
            code += "\t\tthis." + field.get("name") + " = " + field.get("name") + ";\n";
        }
        code += "\t}" + "\n";
        code += "\n";
        for(HashMap<String, String> field : fields)
        {
            code += "\tpublic " + field.get("type") + " " + getMethodName("get", field.get("name")) + "()\n";
            code += "\t{" + "\n";
            code += "\t\treturn " + field.get("name") + ";\n";
            code += "\t}" + "\n";
        }
        code += "}";
        return code;
    }
    static private String getTypeField(Item item)
    {
        if(item.getType().equals("integer"))
        {
            return "int";
        }
        else if(item.getType().equals("string"))
        {
            return "String";
        }
        return null;
    }

    static public String generate(String pcg, Server server)
    {
        String code = "package "+pcg+";\n\n";
        code += "public interface Server" + "\n";
        code += "{" + "\n";
        code += "\tString BASE_PATH = \"" +server.getBasePath()+ "\";\n";
        if(server.getRequests().size() == 0)
        {
            code += "}";
            return code;
        }
        code += "\n";
        Collections.sort(server.getRequests(), (a, b) -> a.getType().compareTo(b.getType()));
        int i = 0;
        while(i < server.getRequests().size())
        {
            code += "\tpublic interface " + getClassName(server.getRequests().get(i).getType()) + "\n";
            code += "\t{\n";
            String type = server.getRequests().get(i).getType();
            while(i < server.getRequests().size())
            {
                if(!type.equals(server.getRequests().get(i).getType()))
                {
                    break;
                }
                code += "\t\tString " + server.getRequests().get(i).getUrl().toUpperCase().replaceAll("-", "_").replaceAll("\\.", "_") + " = \"" +server.getRequests().get(i).getUrl()+ "\";\n";
                i++;
            }
            code += "\t}\n";
        }
        code += "}";
        return code;
    }

    static private String getClassName(String className)
    {
        return toUpperCamelCase(className);
    }

    static private String getMethodName(String prefix, String fieldName)
    {
        return prefix + toUpperCamelCase(fieldName);
    }

    static private String toUpperCamelCase(String str)
    {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return toCamelCase(String.valueOf(chars));
    }
    static private String toCamelCase(String str)
    {
        int sub = str.indexOf("_");
        while(sub >= 0)
        {
            str = str.substring(0, sub) + str.substring(sub+1);
            if(sub == str.length())
            {
                break;
            }
            char[] cs = str.toCharArray();
            cs[sub] = Character.toUpperCase(cs[sub]);
            str = String.valueOf(cs);
            sub = str.indexOf("_");
        }
        return str;
    }
}