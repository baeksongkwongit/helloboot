package tobyspring.helloboot;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
public class HellobootApplication {


	public static void main(String[] args) {
		GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.registerBean(HelloController.class);
		applicationContext.registerBean(SimpleHelloService.class);
		applicationContext.refresh();

		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			 servletContext.addServlet("frontcontroller", new HttpServlet() {
				 @Override
				 protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
					 //인증 , 보안, 다국어, 공통 기능
					 if(req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())){
						 String name = req.getParameter("name");

						 HelloController helloController = applicationContext.getBean(HelloController.class);

						 String hello = helloController.hello(name);
						 resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
						 resp.getWriter().print(hello);
					 } else{
						 resp.setStatus(HttpStatus.NOT_FOUND.value());
					 }
				 }
			 }).addMapping("/*");
		});
		webServer.start();
	}

}
