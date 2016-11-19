package stan.code.generator;

public class Main
{
    static public void main(String[] args)
    {
    	boolean d = false;
    	String destination = null;
    	boolean c = false;
    	String config = null;
    	for(int i=0; i<args.length; i++)
    	{
    		if(args[i].equals("-d"))
    		{
    			i++;
    			if(i < args.length)
    			{
    				destination = args[i];
    				d = true;
    			}
    		}
    		else if(args[i].equals("-c"))
    		{
    			i++;
    			if(i < args.length)
    			{
    				config = args[i];
    				c = true;
    			}
    		}
    	}
    	if(!d || !c)
    	{
			System.out.println("need destination and config");
			return;
    	}
    	System.out.println("destination " +destination+  "\nconfig " + config);
    	new Generator(destination, config).generate();
    }
}