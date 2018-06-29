package com.chicago.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.chicago.dto.Service;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

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
            String logPath = serviceEntry.init(Service.ServiceConfig.newBuilder());
            setLogback(confFile, logPath);
            serviceEntry.initComponents();
            // Blocking call
            serviceEntry.run();
            serviceEntry.shutdown();
        } catch (Exception e)
        {
            e.printStackTrace();
            exit(-1);
        }
    }

    private static void setLogback(String confFile, String logPath) throws Exception
    {
        String [] pathArr = confFile.split("//");
        if (pathArr.length != 2)
        {
            throw new Exception("Path should start with schema file:// or zoo:// or res://");
        }

        Path base = Paths.get(pathArr[1]).getParent();
        Path logback = Paths.get(base.toString(), "logback.xml");
//        System.setProperty("log_path", logPath);
//        System.setProperty("logback.configurationFile", logback.toString());
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        loggerContext.putProperty("log_path", logPath);
        JoranConfigurator configurator = new JoranConfigurator();
        InputStream configStream = FileUtils.openInputStream(FileUtils.getFile(logback.toString()));
        configurator.setContext(loggerContext);
        configurator.doConfigure(configStream); // loads logback file
        configStream.close();
    }
}
