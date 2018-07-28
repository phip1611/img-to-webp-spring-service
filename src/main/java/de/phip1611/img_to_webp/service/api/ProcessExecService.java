package de.phip1611.img_to_webp.service.api;

import java.io.File;

public interface ProcessExecService {

    ProcessExecResult exec(String command, String workingDirectory);

    ProcessExecResult exec(String command, File workingDirectory);

}