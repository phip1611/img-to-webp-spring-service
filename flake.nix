# This flake builds the Img to WebP Spring web service.

{
  description = "Image To WebP Spring Service";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.11";
  };

  outputs =
    inputs@{ nixpkgs, ... }:
    let
      systems = nixpkgs.lib.systems.flakeExposed;
      forAllSystems =
        function: nixpkgs.lib.genAttrs systems (system: function nixpkgs.legacyPackages.${system});

      project =
        pkgs:
        import ./nix/release.nix {
          inherit pkgs;
        };
      javaToolchain = pkgs: (project pkgs).javaToolchain;
    in
    {
      packages = forAllSystems (pkgs: rec {
        default = serviceScriptBin;
        # Spring service as jar file.
        jar = (project pkgs).jar;
        # Shell script that starts the JAR and ensures necessary runtime
        # dependencies.
        serviceScript = (project pkgs).serviceScript;
        serviceScriptBin = (project pkgs).serviceScriptBin;
        # Docker image.
        dockerImage = (project pkgs).dockerImage;
        mavenProject = (project pkgs).mavenProject;
        mavenProjectLatest = (project pkgs).mavenProjectLatest;
      });

      devShells = forAllSystems (pkgs: rec {
        default = javaMinimum;
        javaMinimum = pkgs.mkShell {
          packages = [
            (javaToolchain pkgs).minimum.jdk_headless
            (javaToolchain pkgs).minimum.mavenWithJdk
          ]
          ++ (javaToolchain pkgs).testDeps;
        };
        # Latest stable Java version.
        javaLatest = pkgs.mkShell {
          packages = [
            (javaToolchain pkgs).latest.jdk_headless
            (javaToolchain pkgs).latest.mavenWithJdk
          ]
          ++ (javaToolchain pkgs).testDeps;
        };
      });

      formatter = forAllSystems (pkgs: pkgs.nixfmt-tree);

      nixosModules = rec {
        default = img-to-webp-service;
        img-to-webp-service = import ./nix/modules/img-to-webp-service.nix;
      };
    };

}
