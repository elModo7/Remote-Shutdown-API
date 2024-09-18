package com.em7.shutdown.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Controller
public class ShutdownController {

    @GetMapping(path = "/shutdown")
    public void shutdown(Model model, HttpServletRequest request) {
        log.info("Attempting to shutdown the device.");
        Process p;
        try {
            OSValidator osValidator = new OSValidator();
            if(osValidator.isWindows()) {
                p = Runtime.getRuntime().exec("shutdown -s -f -t 0");
                p.waitFor();
                p.destroy();
            }else if(osValidator.isUnix() || osValidator.isMac()) {
                p = Runtime.getRuntime().exec("shutdown -h now");
                p.waitFor();
                p.destroy();
            }else if(osValidator.isSolaris()){
                p = Runtime.getRuntime().exec("shutdown -y -i5 -g0");
                p.waitFor();
                p.destroy();
            }

        } catch (Exception e) {
            log.error("There was an error shutting off the device");
            log.error("Error: " + e);
        }
    }
}

class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {

        System.out.println(OS);

        if (isWindows()) {
            System.out.println("This is Windows");
        } else if (isMac()) {
            System.out.println("This is MacOS");
        } else if (isUnix()) {
            System.out.println("This is Unix or Linux");
        } else if (isSolaris()) {
            System.out.println("This is Solaris");
        } else {
            System.out.println("Your OS is not supported!!");
        }
    }

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return OS.contains("sunos");
    }

    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }

}