# Exports the Java toolchain.

{
  jdk17_headless, # Minimum
  jdk25_headless, # Latest
  maven,
  temurin-jre-bin-17, # Minimum
  temurin-jre-bin-25, # Latest
  # Test deps
  libwebp,
  which,
}:

rec {
  # Minimum supported version.
  minimum = rec {
    jre = temurin-jre-bin-17;
    jdk_headless = jdk17_headless;
    mavenWithJdk = maven.override {
      inherit jdk_headless;
    };
  };
  # Latest stable version.
  latest = rec {
    jre = temurin-jre-bin-25;
    jdk_headless = jdk25_headless;
    mavenWithJdk = maven.override {
      inherit jdk_headless;
    };
  };
  runtimeDeps = [
    libwebp
    which
  ];
  testDeps = runtimeDeps ++ [ ];
}
