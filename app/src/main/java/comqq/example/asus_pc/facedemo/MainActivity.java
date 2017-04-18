package comqq.example.asus_pc.facedemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.face.AipFace;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView txt;
    private TextView txt1;
    private Button btn;
    private Button btn1;
    private ImageView img;
    private String path;
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private FrameLayout frameLayout;
    private FaceView faceView;
    private ArrayList<String> array=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        faceView= (FaceView) findViewById(R.id.faceview);
        btn= (Button) findViewById(R.id.btn);
        btn1= (Button) findViewById(R.id.btn1);
        img= (ImageView) findViewById(R.id.img);
        txt= (TextView) findViewById(R.id.txt);
        txt1=(TextView) findViewById(R.id.txt1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AipFace client = new AipFace("9532748", "VV0bLrP5HzE47Eg6zdpmRPvw", "6ZADIa48kroXCtZjORLMYq2VFG3MBc1N");
                // 可选：设置网络连接参数
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        client.setConnectionTimeoutInMillis(2000);
                        client.setSocketTimeoutInMillis(60000);
                        final HashMap<String, String> options = new HashMap<String, String>();
                        options.put("max_face_num", "7");
                        options.put("face_fields", "expression");
                        final JSONObject response = client.match(array);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt1.setText(response.toString());
                            }
                        });
                    }
                }).start();

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null&& resultCode== Activity.RESULT_OK) {
            Uri uri=data.getData();
            String[] str={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(uri,str,null,null,null);
            cursor.moveToFirst();
            int colnmnIndex=cursor.getColumnIndex(str[0]);
            final String picturePath=cursor.getString(colnmnIndex);
            array.add(picturePath);
            Log.d("uri",picturePath);
            cursor.close();
            bitmap=BitmapFactory.decodeFile(picturePath);
            img.setImageBitmap(bitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final AipFace client = new AipFace("9532748", "VV0bLrP5HzE47Eg6zdpmRPvw", "6ZADIa48kroXCtZjORLMYq2VFG3MBc1N");
                    // 可选：设置网络连接参数
                    client.setConnectionTimeoutInMillis(2000);
                    client.setSocketTimeoutInMillis(60000);
                    final HashMap<String, String> options = new HashMap<String, String>();
                    options.put("max_face_num", "6");
                    options.put("face_fields", "expression");
                    final JSONObject response = client.detect(picturePath,options );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,response.toString()+"",Toast.LENGTH_LONG).show();
                            txt.setText(response.toString());
                            try {
                                int left =Integer.valueOf(response.getJSONArray("result").getJSONObject(0).getJSONObject("location").getString("left"));
                                int top =Integer.valueOf(response.getJSONArray("result").getJSONObject(0).getJSONObject("location").getString("top"));
                                int width =Integer.valueOf(response.getJSONArray("result").getJSONObject(0).getJSONObject("location").getString("width"));
                                int height =Integer.valueOf(response.getJSONArray("result").getJSONObject(0).getJSONObject("location").getString("height"));
                                Log.d("main","left:"+left);
                                Log.d("main","top:"+top);
                                Log.d("main","width:"+width);
                                Log.d("main","height:"+height);
                                Rect rect=new Rect(left,top,left+width,top+height);
                                faceView.drawRect(rect);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }).start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
