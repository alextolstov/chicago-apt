package com.chicago.common.core;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.protobuf.*;
import com.google.protobuf.GeneratedMessageV3.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigParser
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigParser.class);

    private StringBuffer _fullText = new StringBuffer();
    private StringBuffer _originalText = new StringBuffer();
    private HashMap<String, String> _vars = new HashMap<String, String>() ;

    public <E, T extends Builder<T>> E parse(String path, Builder<T> builder) throws Exception
    {
        StringBuffer fullText = new StringBuffer();
        if (include(path, true, fullText))
        {
            _fullText = fullText;
            TextFormat.merge(fullText, builder);
            return (E)builder.build();
        }
        return null;
    }

    public void saveParsed(String outPath)
    {
        try(  PrintWriter out = new PrintWriter( outPath ))
        {
            out.println(_fullText);
        }
        catch (FileNotFoundException e)
        {
            LOG.error(e.getStackTrace().toString());
        }
    }

    public void saveOriginal(String outPath)
    {
        try(PrintWriter out = new PrintWriter(outPath))
        {
            out.println(_originalText );
        }
        catch (FileNotFoundException e)
        {
            LOG.error(e.getStackTrace().toString());
        }
    }

    private boolean include(String fileName, boolean isFullPath, StringBuffer fullText) throws Exception
    {
        AbstractConfigAccessor acc = ConfigAccessorFactory.createConfigAccessor(fileName);
        int lineNum = 0;
        String line;

        while((line = acc.getNextLine()) != null)
        {
            if (isFullPath)
            {
                _originalText.append(line + '\n');
            }

            lineNum++;
            if (line.startsWith("@include"))
            {
                String[] parts = line.split("\\s+");
                String inclPath = String.format("%s/%s", acc.getCfgLocation(), parts[1].trim());
                if (!include(inclPath, false, fullText))
                {
                    // log error fileName and lineNum
                    return false;
                }
                continue;
            }

            String newLine;
            if ((newLine = processString(line)) != null)
            {
                // log error fileName and lineNum
                fullText.append(newLine + '\n');
            }
        }
        return true;
    }

    private String processString(String line)
    {
        Pattern reg = Pattern.compile("(\\$\\(([^)]+)\\))");
        Matcher m = reg.matcher(line);
        while (m.find())
        {
            String varName = m.group(2);
            String rep = m.group(1);
            boolean isPresent = false;
            String val = "";

            if (_vars.containsKey(varName))
            {
                val = _vars.get(varName);
                isPresent = true;
            }
            else if (varName.startsWith("env."))
            {
                String envStr = System.getenv(varName.substring(4));
                if (envStr != null)
                {
                    isPresent = true;
                    val = envStr;
                }
            }

            if (!isPresent)
            {
                return null;
            }
            line = line.replaceAll(rep, val);
        }
        return line;
    }
}
