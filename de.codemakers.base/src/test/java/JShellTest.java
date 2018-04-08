/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import de.codemakers.base.CJP;
import de.codemakers.base.io.SerializationUtil;
import de.codemakers.base.util.JavaScriptEngine;
import de.codemakers.base.util.JavaScriptEngineBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class JShellTest {

    public static final void main(String[] args) {
        final JavaScriptEngineBuilder javaScriptEngineBuilder = new JavaScriptEngineBuilder();
        javaScriptEngineBuilder.addImports(SerializationUtil.class);
        final JavaScriptEngine javaScriptEngine = javaScriptEngineBuilder.build();
        final Thread thread = new Thread(() -> {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line.startsWith("/")) {
                        line = line.substring(1);
                        if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("shutdown") || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                            System.out.println("Stopped the " + JavaScriptEngine.class.getSimpleName());
                            CJP.shutdown().complete();
                        } else if (line.equalsIgnoreCase("reset") || line.equalsIgnoreCase("reboot") || line.equalsIgnoreCase("restart")) {
                            javaScriptEngine.reset();
                            System.out.println("Resetted the " + JavaScriptEngine.class.getSimpleName());
                        } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                            System.out.println("Usage:\n/exit\n/help\n/reset\n/put VARIABLE DATA\n/get VARIABLE");
                        } else if (line.startsWith("put ")) {
                            line = line.substring("put ".length());
                            final String variable = line.substring(0, line.indexOf(" "));
                            String data = line.substring(variable.length() + " ".length());
                            if (data.startsWith("/eval ")) {
                                data = data.substring("/eval ".length());
                                data = javaScriptEngine.execute(data);
                            }
                            javaScriptEngine.put(variable, data);
                            System.out.println(String.format("Setted \"%s\" to \"%s\"", variable, data));
                        } else if (line.startsWith("get ")) {
                            line = line.substring("get ".length());
                            System.out.println(String.format("\"%s\" is \"%s\"", line, javaScriptEngine.get(line)));
                        } else {
                            System.err.println(String.format("Unrecognized command: %s", line));
                        }
                    } else {
                        javaScriptEngine.executeLarge(line).queue(System.out::println, System.err::println);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }); thread.start();
        //CJP.shutdown().queueAfter(5, TimeUnit.MINUTES);
    }

}
