package com.chicago.app;

import com.chicago.dto.Service;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.internal.ServiceLocatorFactoryImpl;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

public class ServiceMain
{
    public static void main(String[] args)
    {
        String confFile;
        String instance;
        Options options = new Options();
        options.addRequiredOption("c", "conf", true, "Config file location");
        options.addRequiredOption("i", "inst", true, "Service instance");
        // parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(options, args);
            confFile = line.getOptionValue("conf");
            instance = line.getOptionValue("inst");
        } catch (ParseException exp)
        {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            HelpFormatter fmt = new HelpFormatter();
            fmt.printHelp("Help", options);
            return;
        }

        try
        {
            ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("servicelocator");
            ServiceLocatorUtilities.bind(serviceLocator, new ApplicationBinder(confFile));
            ServiceEntry serviceEntry = new ServiceEntry(confFile, instance);
            serviceEntry.init(Service.ServiceConfig.newBuilder());
            serviceEntry.initComponents();
            // Blocking call
            serviceEntry.run();
            serviceEntry.shutdown();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
