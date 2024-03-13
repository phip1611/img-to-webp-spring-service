# Builds the maven project.


{ lib
, maven
, testDeps
}:

let
  pname = "img-to-webp-service";
in
maven.buildMavenPackage {
  inherit pname;
  name = "${pname}-maven-project";
  version = "3.0.0";
  src = lib.sourceByRegex ../. [
    "^img-to-webp-service.*"
    "^img-to-webp-lib.*"
    "pom.xml"
  ];

  # The mavenFetch step, an hidden intermediate step of buildMavenPackage, can only fetch
  # the test dependencies when it also executes the tests. This is a limitation of maven.
  # Hence, the fetch step also executes the tests. I  can either skip all tests or run the
  # tests also in the fetch step.
  mvnFetchExtraArgs = {
    nativeBuildInputs = [ maven ] ++ testDeps;
  };

  # Hash of Maven dependencies.
  mvnHash = "sha256-7wBtBE3Y2vyp6y7wLHyyM5HJeGid60LRCMNgEU3j9J0=";

  nativeBuildInputs = [ testDeps ];

  installPhase = ''
    runHook preInstall

    mkdir $out

    # Copy "a/target" and "b/target" to $out
    find . -type d -name target | xargs -I {} cp --parents -r {} $out

    runHook postInstall
  '';
}
