import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12. 6. 4
 * Time: 오후 4:39
 * To change this template use File | Settings | File Templates.
 */


public class Ubackup {
    private static String appId;
    private static String apiKey;
    private static String secretKey;
    private static Map<String, String> accessToken;

//    public static void main(String... aArgs) throws FileNotFoundException {
    private static List<File> getFiles(String localDirectory) throws  FileNotFoundException{

        File startingDirectory= new File(localDirectory);
        List<File> files = Ubackup.getFileListing(startingDirectory);

        //print out all file names, in the the order of File.compareTo()
//        for(File file : files ){
//            if (file.isDirectory()){
//                System.out.println("디렉토링" + file);
//            } else {
//                System.out.println("빠일" + file);
//            }
//
//        }

        return files;
    }


    public static void main(String []args) throws JSONException, IOException {
//        String localDirectory = "C:\\devel\\php\\test3";

//        String localDirectory = "C:\\devel\\php\\test3\\.metadata\\.mylyn";

//        String folderName = "웹 폴더";
        String folderName = args[0];
        String localDirectory = args[1];

        appId = "1000000153";
        apiKey = "0a1b22e1348a6311c975c10437e9e401";
        secretKey = "307f29ef9303ea6f64181809d43ee8dc";


        String targetFolderId = "";

//        String localDirectory = "C:\\devel\\php\\test3";

        File startDirectory = new File(localDirectory);


        ApiManager manager = new ApiManager();
        manager.Call(apiKey, secretKey);

        if(manager.handler.hasAccessToken()){
            accessToken = manager.handler.getAccessToken();
            manager.handler.setAccessToken(accessToken.get("oauth_token"), accessToken.get("oauth_token_secret"));
        }

        HashMap<?, ?> x = manager.getUserInfo();

        int size = ((ArrayList) x.get("Folders")).size();
        ArrayList folders = ((ArrayList) x.get("Folders"));
        int i = 0;
        for (i = 0; i < size; i++){
            JSONObject folderItem = (JSONObject) folders.get(i);
            if(folderName.equals(folderItem.getString("folder_name"))){
                targetFolderId = folderItem.getString("folder_id");
            }
        }
        long time = System.currentTimeMillis();

        java.util.HashMap<?, ?> result = manager.createDirectory(targetFolderId, String.valueOf(time));
        String did = result.get("folder_id").toString();

        Runtime rt = Runtime.getRuntime();

        String cmd = "tar -cvfz ./_tmp.tgz " + localDirectory;

        Process prc = rt.exec(cmd);

        //콘솔에 출력하기 위한 설정
        BufferedReader br = new BufferedReader(new InputStreamReader(prc.getInputStream()));
        //콘솔에 출력
        for(String str;(str = br.readLine())!=null;){
            System.out.println(str);
        }

        if(prc.exitValue()!=0){
            System.out.println("셀이 정상종료 되지 않았습니다.");
        }

        String fid = "";
        fid = manager.createFile (did, "backup.tgz").toString();
        HashMap<?, ?> token = manager.getUploadToken(fid);
        String fileToken = token.get("file_token").toString();
        String uploadUrl = token.get("redirect_url").toString();

        FileInputStream fis = new FileInputStream("./backup.tgz");
        File file = new File("./backup.tgz");
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPut putRequest = new HttpPut(uploadUrl);
        ByteArrayEntity bae = new ByteArrayEntity(data);
        putRequest.setEntity(bae);
        HttpResponse response = httpClient.execute(putRequest);
        System.out.println("백업이 완료되었습니다.");

//        System.out.println("프로그램 종료");


//        String fid = "";
//
//        for(File file : files ){
//            ucloudFileSystem ufsItem = new ucloudFileSystem();
//            if (file.isDirectory()){
////                System.out.println("디렉" + file);
//                ufsItem.prevId = did;
//                did = manager.createDirectory(did, file.getName()).get("folder_id").toString();
//                ufsItem.isDirectory = true;
//                ufsItem.Id = did;
//            } else {
//                ufsItem.prevId = did;
//                //file.getParent()
//                ufsList.
//                fid = manager.createFile (did, file.getName()).get("file_id").toString();
//                ufsItem.isDirectory = false;
//                ufsItem.Id = fid;
//            }
//            ufsItem.FullPath = file.getPath();
//            ufsItem.Name = file.getName();
//
//            ufsList.add(ufsItem);
//        }
//
//        System.out.println(ufsList);
//        System.out.println("끝");
    }

    static public List<File> getFileListing(
            File aStartingDir
    ) throws FileNotFoundException {
        validateDirectory(aStartingDir);
        List<File> result = getFileListingNoSort(aStartingDir);
//        Collections.sort(result);
        return result;
    }

    static private List<File> getFileListingNoSort(
            File aStartingDir
    ) throws FileNotFoundException {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = aStartingDir.listFiles();
        List<File> filesDirs = Arrays.asList(filesAndDirs);
        for(File file : filesDirs) {
            result.add(file); //always add, even if directory
            if ( ! file.isFile() ) {
                //must be a directory
                //recursive call!
                List<File> deeperList = getFileListingNoSort(file);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    static private void validateDirectory (
            File aDirectory
    ) throws FileNotFoundException {
        if (aDirectory == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!aDirectory.exists()) {
            throw new FileNotFoundException("Directory does not exist: " + aDirectory);
        }
        if (!aDirectory.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + aDirectory);
        }
        if (!aDirectory.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
        }
    }

}
