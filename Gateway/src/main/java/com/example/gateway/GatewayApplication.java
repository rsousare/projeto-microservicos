package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder){
		return builder.routes()
				.route("area_route", r -> r.path("/areas/**")
						.uri("lb://AREA"))
				.route("people_route", r -> r.path("/people/**")
						.uri("lb://PEOPLE"))
				.route("project_route", r -> r.path("/projects/**")
						.uri("lb://PROJECT"))
				.route("ticket_route", r -> r.path("/tickets/**")
						.uri("lb://TICKET"))
				.build();
	}

	//no powerShell -> (podman run -d -p 7080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:24.0.4 start-dev)
	// (-d -> para construir de modo desconectado, senao fecha quando se fecha o powershell
	// depois no browser -> localhost://7080

}
