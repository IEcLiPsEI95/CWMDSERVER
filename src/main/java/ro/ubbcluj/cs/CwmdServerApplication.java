package ro.ubbcluj.cs;

import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ro.ubbcluj.cs.net.AuthRestController;

@SpringBootApplication
public class CwmdServerApplication
{
    
    public static void main(String[] args)
    {
        LogManager.getLogger("file").info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> o pornit <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        SpringApplication.run(CwmdServerApplication.class, args);
    }
}
