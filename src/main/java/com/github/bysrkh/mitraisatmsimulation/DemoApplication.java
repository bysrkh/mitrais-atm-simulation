package com.github.bysrkh.mitraisatmsimulation;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public InputHelper inputHelper(@Lazy LineReader lineReader) {
		return new InputHelper(lineReader);
	}

	@Bean
	public OutputHelper outputHelper(@Lazy Terminal terminal) {
		return new OutputHelper(terminal);
	}

}
