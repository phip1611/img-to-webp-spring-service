# The release.nix exports all relevant modules of this repo.


{ pkgs ? import <nixpkgs> { } }:

let
  lib = pkgs.lib;
  javaToolchain = pkgs.callPackage ./java-toolchain.nix { };
  mavenProject = pkgs.callPackage ./build.nix {
    maven = javaToolchain.minimum.mavenWithJdk;
    inherit (javaToolchain) testDeps runtimeDeps;
  };
  mavenProjectLatest = pkgs.callPackage ./build.nix {
    maven = javaToolchain.latest.mavenWithJdk;
    inherit (javaToolchain) testDeps runtimeDeps;
  };
  jar = pkgs.runCommandLocal "img-to-webp-service-jar" { } ''
    # By copying here instead of using a symlink, I reduce the closure size by
    # hundreds of megabytes. This is especially beneficial when I build a docker
    # image, where the closure is copied to the image.
    cp "${mavenProject}/img-to-webp-service/target/img-to-webp-service-${mavenProject.version}.jar" $out
  '';
  script = ''
    set -euo pipefail

    export PATH="${lib.makeBinPath ([
              javaToolchain.minimum.jre
      	  ] ++ javaToolchain.runtimeDeps)
      	}:$PATH";

    echo "Starting ${mavenProject.pname} ..."
    echo
    echo "Using Java:"
    java --version
    java -jar ${jar}
  '';
  serviceScript = pkgs.writeShellScript "${mavenProject.pname}-script" script;
  serviceScriptBin = pkgs.writeShellScriptBin "${mavenProject.pname}-script-bin" script;

  # The image can be imported by using:
  # `$ docker load < ./result`
  # Afterwards, the container can be run using:
  # `$ docker run -p 127.0.0.1:8080:8080 phip1611/img-to-webp-service:latest`
  dockerImage = pkgs.dockerTools.buildImage {
    name = "phip1611/img-to-webp-service";
    tag = "latest";
    copyToRoot = pkgs.buildEnv {
      name = "image-root";
      paths = [
        serviceScriptBin
      ];
      pathsToLink = [ "/bin" "/tmp" ];
    };
    config.Cmd = [ "/bin/img-to-webp-service-script-bin" ];
  };
in
{
  inherit mavenProject mavenProjectLatest jar javaToolchain;
  inherit serviceScript serviceScriptBin dockerImage;
}
