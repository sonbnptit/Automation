package com.vin3s.auto.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecuteCMD {

    public String doExecuteCmd(String command, String[] params) throws InterruptedException, IOException {
        String[] commands = getCmdCommand(command, params);

        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);
        proc.waitFor();

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        // read the output from the command
        String line = null;
        while ((line = stdInput.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }


    private String[] getCmdCommand(String command, String[] params){
        if (command.contains(".")) { // command is file
            if (!new File(command).isAbsolute()) {
                command = System.getProperty("user.dir") + "/src/" + command;
            }
        }

        if(params == null) { params = new String[] { }; }

        String[] commands = new String[params.length + 3];
        commands[0] = "cmd";
        commands[1] = "/c";
        commands[2] = command;
        for (int i = 0; i < params.length; i++) {
            String cmdPr = params[i];
            if (cmdPr.contains(".")) { // cmd parameter is file
                if (!new File(cmdPr).isAbsolute()) {
                    cmdPr = System.getProperty("user.dir") + "/src/" + cmdPr;
                }
            }
            commands[i + 3] = cmdPr;
        }
        return commands;
    }
}
