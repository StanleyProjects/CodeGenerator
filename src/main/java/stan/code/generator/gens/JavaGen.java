package stan.code.generator.gens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import stan.code.generator.core.*;
import stan.code.generator.core.server.*;
import stan.code.generator.core.dao.*;

public class JavaGen
{
    public ModelGen getModelGen(Model m)
    {
        return new ModelGen(m);
    }
    public DaoGen getDaoGen(ArrayList<Dao> d)
    {
        return new DaoGen(d);
    }

    public String getFileName(String file)
    {
        return getClassName(file) + ".java";
    }

    private ArrayList<HashMap<String, String>> getFields(ArrayList<Item> items)
    {
        ArrayList<HashMap<String, String>> fields = new ArrayList<>();
        for(Item item : items)
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
        return fields;
    }
    private String getTypeField(Item item)
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

    public String generate(String pcg, Server server)
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

    private String getClassName(String className)
    {
        return toUpperCamelCase(className);
    }

    private String getMethodName(String prefix, String fieldName)
    {
        return prefix + toUpperCamelCase(fieldName);
    }

    private String toUpperCamelCase(String str)
    {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return toCamelCase(String.valueOf(chars));
    }
    private String toCamelCase(String str)
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

    public class ModelGen
    {
        private Model model;
        private ArrayList<HashMap<String, String>> fields;

        private ModelGen(Model m)
        {
            model = m;
            fields = getFields(model.getData());
        }

        public String getEntityName()
        {
            return getClassName(model.getName());
        }
        public String getModelName()
        {
            return getClassName(model.getName()) + "Model";
        }

        public String generateInterface(String pcg)
        {
            String className = getModelName();
            String code = "package "+pcg+";\n\n";
            code += "public interface " + className + "\n";
            code += "{" + "\n";
            if(fields.size() == 0)
            {
                code += "}";
                return code;
            }
            for(HashMap<String, String> field : fields)
            {
                code += "\t" + field.get("type") + " " + getMethodName("get", field.get("name")) + "();\n";
            }
            code += "}";
            return code;
        }
        public String generateClass(String pcg)
        {
            String className = getEntityName();
            String code = "package "+pcg+";\n\n";
            code += "public class " + className + "\n";
            code += "\timplements " +getModelName()+ "\n";
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
    }

    public class DaoGen
    {
        private ArrayList<Dao> dao;

        private DaoGen(ArrayList<Dao> d)
        {
            dao = d;
        }

        public String generateModels(String pcg, ArrayList<Model> models)
        {
            String code = "package "+pcg+";\n\n";
            code += "public interface Models" + "\n";
            code += "{" + "\n";
            for(Dao methods : dao)
            {
                JavaGen.ModelGen modelGen = null;
                for(Model model : models)
                {
                    if(methods.getModel().equals(model.getName()))
                    {
                        modelGen = new ModelGen(model);
                        break;
                    }
                }
                if(modelGen == null)
                {
                    continue;
                }
                code += "\tpublic interface " +modelGen.getEntityName()+ "s\n";
                code += "\t{" + "\n";
                for(DaoMethod daoMethod : methods.getMethods())
                {
                    code += "\t\t";
                    if(daoMethod.getResponse() == null)
                    {
                        code += "void";
                    }
                    code += " " + daoMethod.getName() + "(";
                    if(daoMethod.getRequest() != null)
                    {
                        
                    }
                    code += ");\n";
                }
                code += "\t}" + "\n";
            }
            code += "}";
            return code;
        }
    }
}