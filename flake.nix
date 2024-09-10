# This flake builds the Img to WebP Spring web service.

{
  description = "Image To WebP Spring Service";

  inputs = {
    flake-parts.url = "github:hercules-ci/flake-parts";
    flake-parts.inputs.nixpkgs-lib.follows = "nixpkgs";
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.11";
  };

  outputs = inputs@{ flake-parts, nixpkgs, ... }:
    flake-parts.lib.mkFlake { inherit inputs; } {
      systems = nixpkgs.lib.systems.flakeExposed;

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
            default = serviceScriptBin;
            # Spring service as jar file.
            jar = project.jar;
            # Shell script that starts the JAR and ensures necessary runtime
            # dependencies.
            serviceScript = project.serviceScript;
            serviceScriptBin = project.serviceScriptBin;
            # Docker image.
            dockerImage = project.dockerImage;
            mavenProject = project.mavenProject;
            mavenProjectLatest = project.mavenProjectLatest;
          };

          devShells = rec {
            default = javaMinimum;
            javaMinimum = pkgs.mkShell {
              packages = [
                javaToolchain.minimum.jdk_headless
                javaToolchain.minimum.mavenWithJdk
              ] ++ javaToolchain.testDeps
              ;
            };
            # Latest stable Java version.
            javaLatest = pkgs.mkShell {
              packages = [
                javaToolchain.latest.jdk_headless
                javaToolchain.latest.mavenWithJdk
              ] ++ javaToolchain.testDeps
              ;
            };
          };

          formatter = pkgs.nixpkgs-fmt;
        };

      flake = {
        nixosModules = rec {
          default = img-to-webp-service;
          img-to-webp-service = import ./nix/modules/img-to-webp-service.nix;
        };
      };
    };
}
