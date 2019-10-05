package com.integration.micrometer;

import java.time.Duration;

import com.integration.micrometer.model.Order;
import com.integration.micrometer.service.BeerService;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableScheduling
public class MicrometerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrometerApplication.class, args);
		System.out.println("Application Started");
	}


	@Bean
	public MeterFilter meterFilter() {
		return new MeterFilter() {
			@Override
			public Meter.Id map(Meter.Id id) {
				if ("http.client.requests".equals(id.getName())) {
					String urlIdentifier="";
					String uri = id.getTag("uri");
					if(StringUtils.isNotEmpty(uri)) {
						String urlIdentifierArray[] = uri.split("/");
						if(urlIdentifierArray.length>0) {
							urlIdentifier = urlIdentifierArray[0];
						}
					}
					return new Meter.Id(id.getName(), Tags.of("clientName",id.getTag("clientName"),"method",id.getTag("method"),"status",id.getTag("status"),"uri",urlIdentifier) , id.getBaseUnit(), id.getDescription(), id.getType());
				}
				return id;
			}
		};
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
		return registry -> registry.config().commonTags("application", "Test-application");
	}

	private BeerService beerService;

	public MicrometerApplication(BeerService beerService) {
		this.beerService = beerService;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void orderBeers() {
		Flux.interval(Duration.ofSeconds(2))
				.map(MicrometerApplication::toOrder)
				.doOnEach(o -> beerService.orderBeer(o.get()))
				.subscribe();
	}

	private static Order toOrder(Long l) {
		Long amount = l % 5;
		String type = l % 2 == 0 ? "ale" : "light";
		return new Order(amount.intValue(), type);
	}

	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}
}
