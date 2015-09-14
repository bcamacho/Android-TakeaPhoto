package com.example.brandycamacho.takeaphoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class TakePhotoActivity extends Activity {

    ImageView iv;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);


        iv = (ImageView) findViewById(R.id.imageView);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 0);

                    Bundle extras = i.getExtras();
                    Bitmap bitmap;

                    if( null == extras || null == (bitmap=(Bitmap)extras.get("data")) ) return;
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        tv = (TextView) findViewById(R.id.textView);

        if(requestCode == 0) {
            Bitmap theImage = (Bitmap) data.getExtras().get("data");
//            iv.setImageBitmap(theImage);

//            // Get the image from the sdcard
//            Bitmap bm = BitmapFactory.decodeFile("/sdcard/myimage.jpg");




        // encode side
            // turn image into byte array output stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 'compress' the jpeg
            theImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            // get byte[] array of the image
            byte[] byteArray = baos.toByteArray();
            // turn image into base64 string
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //display base64 as text to textview

        // decode side

            byte[] img;
            img = Base64.decode(encodedImage, Base64.DEFAULT);
            ByteArrayInputStream in = new ByteArrayInputStream(img);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;

            Bitmap bmp = BitmapFactory.decodeStream(in, null, options);

           // SETTING TEXT VIEW WITH ENCODED DATA IS GREAT FOR DEBUGGING YET
           // MAJOR COST ON PERFORMANCE
           // tv.setText(encodedImage);
            iv.setImageBitmap(bmp);

            //dicplay toast message with file size of image
            long imgLongCount = bmp.getByteCount();
            Toast.makeText(getBaseContext(), "Size after decoding string to image: " + humanReadableByteCount(imgLongCount, false), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.take_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
