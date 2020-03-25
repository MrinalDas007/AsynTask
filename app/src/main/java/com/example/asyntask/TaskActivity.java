package com.example.asyntask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class TaskActivity extends Activity {

    ProgressBar myProgessBar;
    ProgressBar spinningProgress;
    TextView myTextView;
    Button myButton;
    ImageView myImageView;

    String myUrlString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        myImageView=findViewById(R.id.imageView);
        myProgessBar=findViewById(R.id.progressBar);
        spinningProgress=findViewById(R.id.progressBarCircle);
        myTextView=findViewById(R.id.myTextView);
        myButton=findViewById(R.id.myButton);
        spinningProgress.setVisibility(View.INVISIBLE);

    }
    public class DownloadTask extends AsyncTask<String,Integer, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {

            int count;
            String filelocation="";
            try{
                URL url=new URL(params[0]);
                URLConnection connection=url.openConnection();

                int length=connection.getContentLength();

                InputStream input=new BufferedInputStream(url.openStream(),8192);

                OutputStream output=new FileOutputStream(filelocation);

                byte data[]=new  byte[1024];

                long total=0;

                while((count=input.read(data))!=-1){
                    total+=count;

                    publishProgress((int)((total*100)/length));

                    output.write(data,0,count);
                }

                output.flush();

                output.close();
                input.close();
            }catch (Exception e){
                Log.e("Error: ",e.getMessage());

            }
            return BitmapFactory.decodeFile(filelocation);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myButton.setEnabled(false);
            myTextView.setText("Working...");
            spinningProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            myTextView.setText("Downloaded!!");
            myButton.setEnabled(true);
            spinningProgress.setVisibility(View.GONE);
            myImageView.setImageBitmap(bitmap);
            myImageView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            myProgessBar.setProgress(values[0]);
            spinningProgress.setProgress(values[0]);
            myTextView.setText(String.valueOf(values[0])+"% Complete...");
        }
    }

    public void myButtonClicked(View view){
        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute(myUrlString);
        performance();
    }

    public void performance(){
        myButton.setEnabled(false);
        myTextView.setText("Working...");
        spinningProgress.setVisibility(View.VISIBLE);

        int i=0;
        int amountWeight=1000000;
        int amountComplete=0;
        while(i<(100*amountWeight)){

            myProgessBar.setProgress(i/amountWeight);

            i++;
            amountComplete=i;
        }

    }
}
