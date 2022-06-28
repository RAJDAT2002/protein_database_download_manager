
/**
 * @author Raj Datta
 *
 * Description-This program batch downloads 3d model of proteins in pdb format
 * from the Worldwide Protein Data Bank. The database updates every week and
 * the program can be used to check what new proteins from the database in
 * missing on your computer, and download those files.
 * https://ftp.wwpdb.org/pub/pdb/data/structures/divided/pdb/
 */

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class Downloader {
    static ArrayList<String> names= new ArrayList<>();

    /** To download proteins, for example, from 'a' to 'd', set nameCreator method to iterate from
     * 'a' to 'd'. However, as there are thousands of proteins under each alphabet, I do not
     * recommend doing it. */
    public static void main(String[] args) throws MalformedURLException {
        nameCreator('v');
        fileChecker();
        download();
    }

    /** To download proteins, for example, from 'v8' to vb', set j to iterate from '8' to 'b'. Keep
     * variable i and k unchanged. */
    public static void nameCreator(char letter) {
        for (char j= '8'; j <= 'b'; j++ ) {
            if (j == ':') {
                j+= 39;
            }
            for (int i= 1; i < 10; i++ ) {
                for (char k= '0'; k <= 'z'; k++ ) {
                    if (k == ':') {
                        k+= 39;
                    }
                    String name= "" + i + letter + j + k + "";
                    names.add(name);
                }
            }
        }
    }

    public static void download() throws MalformedURLException {
        for (int c= 0; c < names.size(); c++ ) {
            URL temp= new URL("https://ftp.wwpdb.org/pub/pdb/data/structures/divided/pdb/" +
                names.get(c).substring(1, 3) + "/pdb" + names.get(c) + ".ent.gz");
            File destination= new File("D:\\Protein Database\\" + names.get(c).substring(1, 2) +
                "\\" + names.get(c).substring(1, 3) +
                "\\" + "pdb" + names.get(c) + ".ent.gz");
            try {
                FileUtils.copyURLToFile(temp, destination);
                System.out.println("File " + names.get(c) + " downloaded successfully.");
            } catch (IOException e) {
                System.out.println("File " + names.get(c) + " does not exist.");
            }

        }
    }

    /** The database updates every week. So, once you have downloaded the entire database, there is
     * no need to download the same files over. This method checks what files from the database are
     * already on your folder and makes sure to download the missing ones. */
    public static void fileChecker() {
        for (int c= 0; c < names.size(); c++ ) {
            File tempFile= new File("D:\\Protein Database\\" + names.get(c).substring(1, 2) + "\\" +
                names.get(c).substring(1, 3) +
                "\\" + "pdb" + names.get(c) + ".ent.gz");
            if (tempFile.exists()) {
                names.remove(c);
                c-- ;
            }
        }
    }

    /** This methods is not used by the program but still kept. Method nameCreator makes all
     * possible names of the protein. But, for example, file 1veb exists, but file 9vez doesn't.
     * This method filters out the excess files that doesn't exist in the database. */
    public static void urlChecker() throws IOException {
        for (int c= 0; c < names.size(); c++ ) {
            URL url= new URL("https://ftp.wwpdb.org/pub/pdb/data/structures/divided/pdb/" +
                names.get(c).substring(1, 3) + "/pdb" + names.get(c) + ".ent.gz");
            URLConnection connection= url.openConnection();
            HttpURLConnection httpConnection= (HttpURLConnection) connection;
            int code= httpConnection.getResponseCode();
            if (code == 404) {
                names.remove(c);
                c-- ;
            }
        }
    }
}
