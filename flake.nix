# This flake builds the spring service.

{
  description = "Image To WebP Spring Service";

  inputs = {
    flake-parts.url = "github:hercules-ci/flake-parts";
    nixpkgs.url = "github:NixOS/nixpkgs/release-23.11";
  };

  outputs = inputs@{ flake-parts, nixpkgs, nixpkgs-unstable, ... }:
    flake-parts.lib.mkFlake { inherit inputs; } {
      # Output taken from pkgs.lib.systems.flakeExposed
      systems = [
        "x86_64-linux"
        "aarch64-linux"
        "x86_64-darwin"
        "armv6l-linux"
        "armv7l-linux"
        "i686-linux"
        "mipsel-linux"
        "aarch64-darwin"
        "armv5tel-linux"
        "powerpc64le-linux"
        "riscv64-linux"
      ];

      perSystem = { config, self', inputs', pkgs, system, ... }:
        let
          project = import ./nix/release.nix {
            inherit pkgs;
          };
          javaToolchain = project.javaToolchain;
        in
        rec {
          # Per-system attributes can be defined here. The self' and inputs'
          # module parameters provide easy access to attributes of the same
          # system.

          # Equivalent to  inputs'.nixpkgs.legacyPackages.hello;
          packages = rec {
            default = jar;
            # Spring service as jar file.
            jar = project.jar;
            # Shell script that starts the JAR and ensures necessary runtime
            # dependencies.
            serviceScript = project.serviceScript;
            serviceScriptBin = project.serviceScriptBin;
            # Docker image.
            # 
            dockerImage = project.dockerImage;
          };

          devShells = rec {
            default = javaMinimum;
            javaMinimum = pkgs.mkShell {
              packages = [
                javaToolchain.minimum.jdk
                javaToolchain.minimum.mavenWithJdk
              ] ++ javaToolchain.testDeps
              ;
            };
            # Latest stable Java version.
            javaLatest = pkgs.mkShell {
              packages = [
                javaToolchain.latest.jdk
                javaToolchain.latest.mavenWithJdk
              ] ++ javaToolchain.testDeps
              ;
            };
          };

          formatter = pkgs.nixpkgs-fmt;
        };

      flake = { };
    };
}
