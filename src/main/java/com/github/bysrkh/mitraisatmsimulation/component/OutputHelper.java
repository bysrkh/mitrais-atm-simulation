package com.github.bysrkh.mitraisatmsimulation.component;


import org.jline.terminal.Terminal;

public class OutputHelper {
    private Terminal terminal;

    public OutputHelper(Terminal terminal) {
        this.terminal = terminal;
    }

    public void print(String message) {
        String toPrint = message;
        terminal.writer().println(toPrint);
        terminal.flush();
    }
}
