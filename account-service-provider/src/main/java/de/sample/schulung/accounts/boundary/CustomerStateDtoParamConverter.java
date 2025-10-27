package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.boundary.CustomerDto.CustomerStateDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

@Provider
public class CustomerStateDtoParamConverter implements ParamConverterProvider {

  @Override
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
    if (!rawType.equals(CustomerStateDto.class)) {
      return null;
    }

    return new ParamConverter<>() {
      @Override
      public T fromString(String value) {
        if (value == null) return null;
        return Arrays.stream(CustomerStateDto.values())
          .filter(e -> e.getValue().equalsIgnoreCase(value)) // Hier getValue() benutzen
          .findFirst()
          .map(rawType::cast)
          .orElseThrow(() -> new BadRequestException("Invalid state: " + value));
      }

      @Override
      public String toString(T value) {
        return ((CustomerStateDto) value).getValue();
      }
    };
  }
}
