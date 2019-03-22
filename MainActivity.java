package oshin.tasnuva.mysql;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<JsonDataList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<JsonDataList>();

        JsonFetch jsonFetch = new JsonFetch();
        jsonFetch.execute();


    }


    public class JsonFetch extends AsyncTask<String,String,String>{

                 HttpURLConnection httpURLConnection = null;
                 String mainfile;
        BufferedReader bufferedReader = null;
        @Override
        protected String doInBackground(String... strings) {


            try {
                URL url = new URL("https://api.myjson.com/bins/k21n6");
                httpURLConnection  = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
               bufferedReader  = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line =" ";
                while ((line=bufferedReader.readLine())!=null){

                    stringBuffer.append(line);


                }

                 mainfile = stringBuffer.toString();


                JSONArray parent = new JSONArray(mainfile);
                      int i =0;

                      while (i <= parent.length()){

                          JSONObject child = parent.getJSONObject(i);

                          String name = child.getString("user_name");
                          String img = child.getString("user_img");
                          String description = child.getString("user_description");

                         arrayList.add(new JsonDataList(name,img,description));

                             i++;
                      }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            final JsonAdapter jsonAdapter = new JsonAdapter(arrayList, getApplicationContext(), new MyClick() {
                @Override
                public void onClickMe(View view, int Position) {

                    String link = arrayList.get(Position).getUser_img();
                    Intent i = new Intent(MainActivity.this,Main2Activity.class);
                    i.putExtra("img",link);
                    startActivity(i);


                }
            });
            recyclerView.setAdapter(jsonAdapter);







        }
    }

}
