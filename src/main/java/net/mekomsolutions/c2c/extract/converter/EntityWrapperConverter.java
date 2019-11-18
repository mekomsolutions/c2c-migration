package net.mekomsolutions.c2c.extract.converter;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import net.mekomsolutions.c2c.extract.Entity.EntityWrapper;

@Converter
public class EntityWrapperConverter {

	@Converter
	public static <T> EntityWrapper<T> toBaseEntity(T data, Exchange exchange) {
		
		TypeConverter converter = exchange.getContext().getTypeConverter();
				
		String modelClass = converter.convertTo(String.class, exchange.getIn().getHeader("modelClassName"));
				
		return new EntityWrapper<T>(modelClass, data);
	}
}
