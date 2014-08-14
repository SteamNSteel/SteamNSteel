package mod.steamnsteel.utility;

import cpw.mods.fml.common.Loader;
import mod.steamnsteel.library.Reference;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @credits Buildcraft version checker code
 */
public class VersionChecker implements Runnable{

    private enum VERSION{
        CURRENT, OUTDATED, CONN_ERROR
    }

    private static VERSION versionState = VERSION.CURRENT;
    private static String version = Reference.MOD_VERSION;
    private static final String FILE_LOC = "https://gist.github.com/horfius/75b36e23389938a21183";
    private static HashMap<String, ArrayList<String>> cachedVersions = null;
    private static final String MC_VERSION = Loader.instance().getMinecraftModContainer().getVersion();

    public String getVersion(){
        return version;
    }

    public boolean isOutdated(){
        return versionState == VERSION.OUTDATED;
    }

    public String getMCVersion(){
        return MC_VERSION;
    }

    public String getLatestRelease(){
        StringBuilder latestVersion = new StringBuilder();

        String[] mcVersions = (String[])cachedVersions.keySet().toArray();
        Arrays.sort(mcVersions);
        String[] modVersions = (String[])cachedVersions.get(mcVersions[mcVersions.length - 1]).toArray();

        latestVersion.append("The latest version of Steam N' Steel is ");
        latestVersion.append(modVersions[modVersions.length - 1]);
        latestVersion.append(" for MC version ");
        latestVersion.append(mcVersions[mcVersions.length - 1]);
        latestVersion.append(".");

        return latestVersion.toString();
    }

    public String getLatestReleaseForMCVersion(String version){
        StringBuilder latestVersion = new StringBuilder();

        String[] modVersions = (String[])cachedVersions.get(version).toArray();

        latestVersion.append("The latest release for you MC version is ");
        latestVersion.append(modVersions[modVersions.length - 1]);
        latestVersion.append(".");

        return latestVersion.toString();
    }

    public Object[] getAllVersions(){
        ArrayList<String> versions = new ArrayList<String>();

        Object[] mcVersions = cachedVersions.keySet().toArray();
        for(int i = 0; i < mcVersions.length; i++){
            Object[] modVersions = cachedVersions.get(mcVersions[i]).toArray();
            for(Object s: modVersions){
                versions.add(mcVersions[i] + ":" + s);
            }
        }

        return versions.toArray();
    }

    private static void sortVersionInfo(){
        Object[] mcVersions = cachedVersions.keySet().toArray();
        for(int i = 0; i < mcVersions.length; i++){
            ArrayList<String> cachedModVersions = cachedVersions.get(mcVersions[i]);
            Object[] sortedModVersions = cachedModVersions.toArray();
            Arrays.sort(sortedModVersions);

            ArrayList<String> newModVersions = new ArrayList<String>();

            for(Object s : sortedModVersions){
                newModVersions.add((String)s);
            }

            cachedVersions.put((String)mcVersions[i], newModVersions);
        }
    }

    public static void versionCheck(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(FILE_LOC).openStream()));

            String line;
            while((line = reader.readLine()) != null) {
                if (line.indexOf("[") > 0 && line.indexOf("}") > line.indexOf("[")) {
                    ArrayList<String> spottedVersions = new ArrayList<String>();
                    while(line.indexOf("[") > 0 && line.indexOf("}") > line.indexOf("[")){
                        line = line.substring(line.indexOf("["));
                        spottedVersions.add(line.substring(0, line.indexOf("}") + 1));
                        line = line.substring(line.indexOf("}") + 1);
                    }
                    for(int i = 0; i < spottedVersions.size(); i++) {
                        String[] tokens = spottedVersions.get(i).split(":");
                        for (String s : tokens) {
                            Logger.info("%s", s);
                        }
                        String mcVersion = tokens[0].substring(1, tokens[0].length() - 1);
                        String modVersion = tokens[1].substring(tokens[1].indexOf("version=&quot;") + "version=&quot;".length());
                        modVersion = modVersion.substring(0, modVersion.indexOf("&quot;"));

                        if (cachedVersions.get(mcVersion) == null) {
                            cachedVersions.put(mcVersion, new ArrayList<String>());
                        }

                        ArrayList<String> versionsForCurrentMC = cachedVersions.get(mcVersion);
                        if (!versionsForCurrentMC.contains(modVersion)) {
                            versionsForCurrentMC.add(modVersion);
                        }
                        cachedVersions.put(mcVersion, versionsForCurrentMC);
                    }
                }
                sortVersionInfo();
            }
            reader.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        int tries = 0;
        version = null;
        cachedVersions = new HashMap<String, ArrayList<String>>();

        Logger.warning("%s", "Starting Steam N' Steel version checker.");

        try{
            while(tries < 3 && (version == null || versionState == VERSION.CONN_ERROR)){
                versionCheck();
                tries++;

                if(versionState == VERSION.CONN_ERROR){
                    Logger.warning("%s %s %s", "Steam N' Steel version check try", tries, "failed, will retry in 10 seconds.");
                    Thread.sleep(10000);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        if(versionState == VERSION.CONN_ERROR){
            Logger.severe("%s", "Steam N' Steel version checker failed.");
        }
    }
}
