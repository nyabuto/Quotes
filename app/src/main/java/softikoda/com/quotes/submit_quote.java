package softikoda.com.quotes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.HashMap;
import java.util.Map;


public class submit_quote extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
Spinner sp_genre,sp_author;
    EditText ed_quote;
    Button btn_submit;
    JSONArray data=null;

    String[] genre_array={},author_array={};

    String author,genre,quote;
    public submit_quote() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static submit_quote newInstance(String param1, String param2) {
        submit_quote fragment = new submit_quote();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_submit_quote, container, false);

        sp_genre = (Spinner) view.findViewById(R.id.genres);
        sp_author = (Spinner) view.findViewById(R.id.author);
        ed_quote = (EditText) view.findViewById(R.id.quote);
        btn_submit = (Button) view.findViewById(R.id.submit);


sp_author.getSelectedView();
sp_author.setEnabled(false);

        sp_genre.getSelectedView();
        sp_genre.setEnabled(false);

        getAuthors();
        getGenres();
        author="";genre="";quote="";

       btn_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!ed_quote.getText().toString().trim().equals("")){
               author=sp_author.getSelectedItem().toString().trim();
                genre=sp_genre.getSelectedItem().toString().trim();
                   quote=ed_quote.getText().toString().trim();

                   saveQuote();
               }
               else{
                   Toast.makeText(getContext().getApplicationContext(),"Enter quote name",Toast.LENGTH_SHORT).show();
               }
           }
       });



        return view;
    }


    public void getGenres(){

        String finalUrl = "http://www.softikoda.com/quotes/getGenres.php";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, finalUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String genre_data=",";
                try {
                    if (response.getInt("success") > 0) {
                        data = response.getJSONArray("genres");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject objData = data.getJSONObject(i);

                            String genre_id = objData.getString("genre_id");
                            String genre_name = objData.getString("genre_name");

genre_data+=","+genre_name;

                        }

                    }
genre_data=genre_data.replace(",,","");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
genre_array=genre_data.split(",");
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,genre_array);
                sp_genre.setAdapter(adapter);
                sp_genre.setEnabled(true);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);


    }

    public  void getAuthors(){


        String finalUrl = "http://www.softikoda.com/quotes/getAuthor.php";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, finalUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String author_data=",";
                try {
                    if (response.getInt("success") > 0) {
                        data = response.getJSONArray("authors");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject objData = data.getJSONObject(i);

                            String author_id = objData.getString("author_id");
                            String author_name = objData.getString("author_name");

                            author_data+=","+author_name;

                        }

                    }
author_data=author_data.replace(",,","");

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                author_array=author_data.split(",");

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,author_array);
                sp_author.setAdapter(adapter);

                sp_author.setEnabled(true);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);



    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void saveQuote(){
        String url = "http://www.softikoda.com/quotes/saveQuote.php";

        RequestQueue ManageMoviewQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext().getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("genre",genre);
                params.put("author",author);
                params.put("quote",quote);
                return params;
            }
        };
        ManageMoviewQueue.add(request);
    }
}
