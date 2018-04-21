package cordova.plugin.encryption;

import android.content.res.AssetManager;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class EncryptionPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            //String message = args.getString(0);
            try {
                this.coolMethod(callbackContext);
                //this.coolMethod(message, callbackContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void coolMethod(/*String message, */CallbackContext callbackContext) throws IOException, JSONException {
        AssetManager assetManager = this.webView.getContext().getAssets();
        Map<String, String> map = getDirMD5(assetManager,"www");
        JSONObject jsonObject = new JSONObject();
        for (String key:
             map.keySet()) {
            jsonObject.put(key,map.get(key));
        }


/*      
        /data/data/<package name>/files 目录下
		创建fileList.json文件
		<package name>代表应用程序的包名
		
        FileOutputStream fileOutputStream = this.webView.getContext().
                openFileOutput("fileList.json", Context.MODE_PRIVATE);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8");
        outputStreamWriter.write(jsonObject.toString());
        outputStreamWriter.flush();
        fileOutputStream.flush();
        outputStreamWriter.close();
        fileOutputStream.close();

        读取fileList.json文件
        FileInputStream fis=this.webView.getContext().openFileInput("fileList.json");
        InputStreamReader is=new InputStreamReader(fis,"UTF-8");
        //fis.available()文件可用长度
        char input[]=new char[fis.available()];
        is.read(input);
        is.close();
        fis.close();
        String readed=new String(input);

*/
        //System.out.println(jsonObject.toString());
        callbackContext.success(jsonObject.toString());
//      callbackContext.error("Expected one non-empty string argument.");
    }

    /**
     * 根据文件计算出文件的MD5
     * @param inputStream
     * @return string
     */
    private static String getFileMD5(InputStream inputStream) {
        MessageDigest digest = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((len = inputStream.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            inputStream.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());

        return bigInt.toString(16);
    }

    /**
     * 获取文件夹中的文件的MD5值
     * @param path
     * @return map
     */
    private static Map<String,String> getDirMD5(AssetManager assetManager, String path) throws IOException {
        String[] fileNames= assetManager.list(path);
        String md5 = null;
        Map<String, String> map = new HashMap<String, String>();
        if (fileNames.length > 0) {//如果是文件则长度为0
            for (String fileName:
                    fileNames) {
                map.putAll(getDirMD5(assetManager,path + "/" + fileName));
            }
        } else {
            String fileName = path;
            InputStream inputStream = assetManager.open(fileName);
            md5 = getFileMD5(inputStream);
            if (md5 != null) {
                map.put(fileName,md5);
            }
            return map;
        }
        return map;
    }
}
