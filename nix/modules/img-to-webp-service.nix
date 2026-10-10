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
      wantedBy = [ "multi-user.target" ];
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

        # Hardening
        DynamicUser = true;
        UMask = "0077";

        # Binding to privileged ports requires a capability that is only
        # effective in the initial user namespace.
        AmbientCapabilities = lib.optional (cfg.port < 1024) "CAP_NET_BIND_SERVICE";
        CapabilityBoundingSet = if cfg.port < 1024 then "CAP_NET_BIND_SERVICE" else "";
        PrivateUsers = cfg.port >= 1024;

        PrivateDevices = true;
        ProtectHome = true;
        ProtectProc = "invisible";
        ProcSubset = "pid";
        # Only the JRE, which, and cwebp are executed, all from the Nix store.
        NoExecPaths = [ "/" ];
        ExecPaths = [ "/nix/store" ];
      };
    };
  };
}
