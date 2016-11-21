package stan.code.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import json.JSONParser;
import util.Files;

import stan.code.generator.core.*;
import stan.code.generator.core.server.*;
import stan.code.generator.core.dao.*;
import stan.code.generator.gens.*;

public class Generator
{
    private String destination;
    private Info info;
    private Server server;
    private ArrayList<Model> models;
    private ArrayList<Dao> dao;

    public Generator(String d, String c)
    {
        destination = d;
        String configString = Files.readFile(c);
        try
        {
            parseConfig((HashMap)new JSONParser().parse(configString));
        }
        catch(Exception e)
        {
            System.out.println(getClass().getName() + " parse config " + e.getMessage());
            return;
        }
        System.out.println(getClass().getName() + " Info:"
            + "\ndescription " + info.getDescription()
            + "\nversion " + info.getVersion()
            + "\ntitle " + info.getTitle());
        System.out.println(getClass().getName() + " Server:"
            + "\nbasePath " + server.getBasePath()
            + "\nrequests " + server.getRequests());
        System.out.println(getClass().getName() + " Models:"
            + "\nsize " + models.size());
        for(Model model : models)
        {
            System.out.println(getClass().getName() + " Model:"
                + "\nname " + model.getName()
                + "\ndata " + model.getData());
        }
    }
    private void parseConfig(HashMap config)
    {
        parseInfo((HashMap)((HashMap)config).get("info"));
        parseServer((HashMap)((HashMap)config).get("server"));
        this.dao = new ArrayList<>();
        parseModels((ArrayList)((HashMap)config).get("models"));
    }
    private void parseInfo(HashMap info)
    {
        this.info = new Info((String)info.get("description"),
            (String)info.get("version"),
            (String)info.get("title"));
    }
    private void parseServer(HashMap server)
    {
        ArrayList<ServerRequest> requests = new ArrayList<>();
        ArrayList rs = (ArrayList)server.get("requests");
        for(Object request : rs)
        {
            requests.add(parseRequest((HashMap)request));
        }
        this.server = new Server((String)server.get("basePath"), requests);
    }
    private ServerRequest parseRequest(HashMap request)
    {
        return new ServerRequest((String)request.get("url"),
            (String)request.get("type"));
    }
    private void parseModels(ArrayList models)
    {
        this.models = new ArrayList<>();
        for(Object model : models)
        {
            ArrayList<Item> data = new ArrayList<>();
            ArrayList d = (ArrayList)((HashMap)model).get("data");
            for(Object item : d)
            {
                data.add(parseItem((HashMap)item));
            }
            this.models.add(new Model((String)((HashMap)model).get("name"), data));
            addDao((String)((HashMap)model).get("name"), (ArrayList)((HashMap)model).get("dao"));
        }
    }
    private Item parseItem(HashMap item)
    {
        return new Item((String)item.get("name"),
            (String)item.get("type"));
    }
    private void addDao(String model, ArrayList d)
    {
        ArrayList<DaoMethod> methods = new ArrayList<>();
        for(Object method : d)
        {
            methods.add(parseDaoMethod((HashMap)method));
        }
        dao.add(new Dao(model, methods));
    }
    private DaoMethod parseDaoMethod(HashMap method)
    {
        return new DaoMethod((String)method.get("name"),
            parseResponse((HashMap)method.get("request")),
            parseResponse((HashMap)method.get("response")));
    }
    private Response parseResponse(HashMap r)
    {
        Response response = null;
        return response;
    }

    public void generate()
    {
        String pkg = "stan";
        String modelsPackage = "models";
        String serverPackage = "server";
        String srcPath = destination + "/src";
        String modelsPath = srcPath + "/" + pkg + "/" + modelsPackage;
        String serverPath = srcPath + "/" + pkg + "/" + serverPackage;
        new File(destination).mkdirs();
        new File(modelsPath).mkdirs();
        new File(serverPath).mkdirs();
        for(Model model : models)
        {
            Files.writeFile(JavaGen.generate(pkg + "." + modelsPackage, model), modelsPath + "/" + JavaGen.getFileName(model.getName()));
        }
        Files.writeFile(JavaGen.generate(pkg + "." + serverPackage, server), serverPath + "/" + JavaGen.getFileName("Server"));
    }
}