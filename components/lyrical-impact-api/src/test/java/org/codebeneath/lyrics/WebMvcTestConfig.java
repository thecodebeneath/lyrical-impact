package org.codebeneath.lyrics;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Supporting @WebMvcTest controller tests to load spring context
 */
@SpringBootConfiguration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class))
public class WebMvcTestConfig {  
}
