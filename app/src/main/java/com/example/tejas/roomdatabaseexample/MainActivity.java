package com.example.tejas.roomdatabaseexample;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tejas.roomdatabaseexample.DaggerData.DaggerAppComponent;
import com.example.tejas.roomdatabaseexample.DaggerData.StorageModule;
import com.example.tejas.roomdatabaseexample.DataBase.AppDatabase;
import com.example.tejas.roomdatabaseexample.DataBase.ImagePathEntity;
import com.example.tejas.roomdatabaseexample.DataBase.UserDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    UserDao userDao;
    @Inject
    AppDatabase appDatabase;
    ContentLoadingProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;
    String imageUrl = "https://img-lumas-avensogmbh1.netdna-ssl.com/showimg_dpi01_full.jpg";
    String imageAssetId = "cam_1";
    private Context mContext;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        DaggerAppComponent.builder().storageModule(new StorageModule(this)).build().inject(this);

        Button b = (Button) findViewById(R.id.b1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();

                new Thread(new Runnable() {
                    public void run() {
                        downloadFile(imageUrl, imageAssetId);
                    }
                }).start();
            }
        });


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Voice Recorder", "PERMISSION_GRANTED");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CODE);
        }

    }

    void downloadFile(final String imageUrl, final String imageAssetId) {

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //create a new file, to save the downloaded file
//          File file = new File(SDCardRoot, "downloaded_file.png");
            final File file = getOutputFile();
            file.getParentFile().mkdirs();

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float) downloadedSize / totalSize) * 100;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            pb.setProgress((int) per, true);

                        } else {
                            pb.setProgress((int) per);
                        }
                        cur_val.setText((int) per + "%");
                    }
                });
            }
            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
//                  pb.dismiss(); // if you want close it..
                    dialog.dismiss();


                    ImagePathEntity imagePathEntity=new ImagePathEntity();
                    imagePathEntity.setImageUrl(imageUrl);
                    imagePathEntity.setLocalPath(file.getAbsolutePath());
                    imagePathEntity.setAssetId(imageAssetId);
                    appDatabase.userDao().insertAll(imagePathEntity);

//                  DatabaseInitializer.populateSync(AppDatabase.getAppDatabase(mContext), imageUrl, file.getAbsolutePath(), imageAssetId);
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e.getMessage());
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e.getMessage());
            e.printStackTrace();
        } catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    void showError(final String err) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress() {
        dialog = new Dialog(MainActivity.this);
//      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");


        TextView text = dialog.findViewById(R.id.tv1);
        text.setText("Downloading file ");
        cur_val = dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");

        dialog.show();

        pb = (ContentLoadingProgressBar) dialog.findViewById(R.id.progress_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pb.setProgress(0, true);
        } else {
            pb.setProgress(0);
        }
//      pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Samai/img_"
                + dateFormat.format(new Date())
                + ".png");
    }




}
