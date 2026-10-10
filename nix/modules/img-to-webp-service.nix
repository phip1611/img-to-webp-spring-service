# NixOS module that exports the img-to-webp service as systemd service.

{
  config,
  lib,
  pkgs,
  ...
}:

let
  cfg = config.services.img-to-webp-service;
  project = import ../release.nix { inherit pkgs; };
in
{
  options = {
    services.img-to-webp-service = {
      enable = lib.mkEnableOption "the img-to-webp-service systemd service";
      package = lib.mkOption {
        type = lib.types.package;
        description = "The package to use.";
        default = project.serviceScriptBin;
      };
      port = lib.mkOption {
        type = lib.types.int;
        description = "Default port of the web service.";
        default = 8080;
      };
    };
  };

  config = lib.mkIf cfg.enable {
    systemd.services.img-to-webp-service = {
      enable = true;
      restartIfChanged = true;
      description = "Img to WebP Image Convert Spring Web Service";
      wantedBy = [ "default.target" ];
      serviceConfig = {
        Type = "simple";
        # https://stackoverflow.com/questions/21083170/how-to-configure-port-for-a-spring-boot-application
        Environment = [
          "SERVER_PORT=${toString cfg.port}"
        ];
        ExecStart = "${cfg.package}/bin/img-to-webp-service-script-bin";
        Restart = "always";
        # The JVM exits with 128 + SIGTERM on a regular stop.
        SuccessExitStatus = 143;
      };
    };
  };
}
