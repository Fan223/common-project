package fan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

@SpringBootApplication
@MapperScan("fan.**.dao")
public class CommonWebApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch("系统启动耗时: ");
        stopWatch.start("系统启动");

        SpringApplication.run(CommonWebApplication.class, args);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("系统启动耗时: " + stopWatch.getTotalTimeSeconds() + "秒");
    }

}
