import com.kt.openplatform.sdk.KTOpenApiHandler;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12. 6. 4
 * Time: 오후 4:51
 * To change this template use File | Settings | File Templates.
 */
public class ApiManager {
    public KTOpenApiHandler handler = null;
    public void Call(String key, String secret){
        handler = KTOpenApiHandler.createHandler(key, secret);
        if(handler == null){
            System.out.println("API키 관련 오류");
            return;
        }
    }

    public HashMap<?, ?> getUserInfo(){
        String apiId = "1.0.UCLOUD.BASIC.GETUSERINFO";
        java.util.HashMap<?, ?> result = this.handler.call(apiId, null, null, true);
        return result;

//        String api_id = "1.0...UCLOUD.BASIC.GETUSERINFO";
//        HashMap<String,String> params = new HashMap<String,String>();
//
//        HashMap<String,String> xauthParams = new HashMap<String,String>(); // => 추가
//        xauthParams.put("username", "yuiop1631"); // => 추가
//        xauthParams.put("password", "xptmxm3"); // => 추가
//
//        // get user information
//        HashMap<String,?> result = this.handler.call( api_id, params, xauthParams, this, false ); // => 변경
//        String rc = String.valueOf( result.get( uCloudFileHandler.RESULT_CODE ) );

    }

    public HashMap<?, ?> createDirectory(String directoryId, String directoryName){
        String apiId = "1.0.UCLOUD.BASIC.CREATEFOLDER";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("folder_id", directoryId);
        params.put("folder_name", directoryName);
        java.util.HashMap<?, ?> result = this.handler.call(apiId, params, null, true);
        return result;
    }

    public HashMap<?, ?> createFile(String directoryId, String fileName){
        String apiId = "1.0.UCLOUD.BASIC.CREATEFILE";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("folder_id", directoryId);
        params.put("file_name", fileName);
        params.put("mediaType", fileName);
        java.util.HashMap<?, ?> result = this.handler.call(apiId, params, null, true);
        return result;
    }

    public HashMap<?, ?> getDirectoryInfo(String directoryId){
        String apiId = "1.0.UCLOUD.BASIC.GETFOLDERINFO";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("folder_id", directoryId);
        java.util.HashMap<?, ?> result = this.handler.call(apiId, params, null, true);
        return result;
    }

    public HashMap<?, ?> getUploadToken(String fileId){
        String apiId = "1.0.UCLOUD.BASIC.CREATEFILETOKEN";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("folder_id", fileId);
        params.put("transfer_mode", "UP");
        java.util.HashMap<?, ?> result = this.handler.call(apiId, params, null, true);
        return result;
    }
}
