package de.sample.schulung.accounts.shared.interceptors;

import jakarta.annotation.Priority;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Priority(5)
@Interceptor
@FireEvent(Object.class)
public class FireEventInterceptor {

  @Inject
  Event<Object> eventPublisher;

  private static <T> T createEventObject(InvocationContext invocation, Class<T> eventType) {
    try {
      return eventType
        .getConstructor(invocation.getMethod().getParameterTypes())
        .newInstance(invocation.getParameters());
    } catch (
      InstantiationException
      | IllegalAccessException
      | InvocationTargetException
      | NoSuchMethodException e
    ) {
      throw new RuntimeException(e);
    }
  }

  @AroundInvoke
  public Object fireEvent(InvocationContext invocation) throws Exception {
    final Optional<FireEvent> annotation = AnnotationUtils
      .findAnnotation(invocation.getMethod(), FireEvent.class);
    @SuppressWarnings("unchecked") final Optional<Class<Object>> eventType = AnnotationUtils
      .findAnnotation(invocation.getMethod(), FireEvent.class)
      .map((FireEvent publishEvent) -> (Class<Object>) publishEvent.value());
    final FireEvent.FireMode mode = annotation
      .map(FireEvent::mode)
      .orElse(FireEvent.FireMode.SYNC_AND_ASYNC);
    final Optional<Object> event = eventType
      .map(clazz -> createEventObject(invocation, clazz));
    // if something is wrong until here, we do not invoke the service's create-method
    // now, we invoke the service
    final Object result = invocation.proceed();
    // if an exception occured, the event is not fired
    // now, we fire the event
    event.ifPresent(
      e -> eventType
        .map(eventPublisher::select)
        .ifPresent(publisher -> {
          // fire synchronous events
          if (mode.isFireSync()) {
            publisher.fire(e);
          }
          // if no error occured, fire asynchronous events
          if (mode.isFireAsync()) {
            publisher.fireAsync(e);
          }
        })
    );
    // and we need to return the service's result to the invoker (the controller)
    return result;
  }
}
