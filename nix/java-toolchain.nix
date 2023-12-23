# Exports the Java toolchain.

{ jdk11
, jdk21
, maven
, temurin-jre-bin-11
  # Test deps
, libwebp
, which
}:

rec {
  # Minimum supported version.
  minimum = rec {
    jre = temurin-jre-bin-11;
    jdk = jdk11;
    mavenWithJdk = maven.override {
      inherit jdk;
    };
  };
  # Latest stable version.
  latest = rec {
    jdk = jdk21;
    mavenWithJdk = maven.override {
      inherit jdk;
    };
  };
  runtimeDeps = [
    libwebp
    which
  ];
  testDeps = [
    libwebp
    which
  ];
}
