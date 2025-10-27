package de.sample.schulung.accounts;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class AccountServiceProviderApplication {

  public static void main(String... args) {
    Quarkus.run(args);
  }

}
