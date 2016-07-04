package softikoda.com.quotes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Database.manageDB;
import Quote.Child;
import Quote.GetServerData;
import Quote.Parent;

public class FragmentData extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SearchView search;
    public static CustomExpandableList listAdapter;
    private ExpandableListView expandableListView;
    private ArrayList<Parent> parentList = new ArrayList<Parent>();
    ProgressDialog progressDialog;
    manageDB dbInstance;
    JSONArray AllQuotes=null;
    int genre_id=0;
    SwipeRefreshLayout swipeRefreshLayout;
    GetServerData serverData;
int loadCheker=0;
    public FragmentData() {
        // Required empty public constructor
    }

    public static FragmentData newInstance(String param1, String param2) {
        FragmentData fragment = new FragmentData();
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
     View view=inflater.inflate(R.layout.fragment_data, container, false);

        genre_id=getArguments().getInt("genre_id");

        //get reference to the ExpandableListView
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh) ;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isInternetAvailable()) {
                    serverData = new GetServerData(getContext());
                    serverData.serverQuotes(getContext());
                }
                else{
                    Toast.makeText(getContext(),"Please connect to the internet to fetch new Quotes",Toast.LENGTH_SHORT).show();
                }
                parentList.clear();
                loadServerData();
            }
        });

        //display the list
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Loading quotes ...");
        progressDialog.show();
        if(isInternetAvailable()) {
            serverData = new GetServerData(getContext());
          serverData.serverQuotes(getContext());
}
        else{
            Toast.makeText(getContext(),"Please connect to the internet to fetch new Quotes",Toast.LENGTH_SHORT).show();
        }
        loadServerData();
        return view;
    }
	
	
    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }
    public void loadServerData() {
  
        dbInstance = new manageDB(getActivity().getApplicationContext());
        if (genre_id == 0) {
            AllQuotes = dbInstance.loadAllQuotes();
        } else {
      AllQuotes = dbInstance.loadGenreData(""+genre_id);
        }


        for (int i = 0; i < AllQuotes.length(); i++) {
            try{
                JSONObject jsonObject = AllQuotes.getJSONObject(i);

                String genre_author = jsonObject.getString("title_name");
                String no_of_quotes = jsonObject.getString("no_quotes");

             JSONArray jsonData =   jsonObject.getJSONArray("data") ;
                if(jsonData.length()>0) {
                    ArrayList<Child> childList = new ArrayList<Child>();
                    for (int j = 0; j < jsonData.length(); j++) {
                        JSONObject quoteData = jsonData.getJSONObject(j);
                        String quote = quoteData.getString("quote_name");
                        String quote_id = quoteData.getString("quote_id");
                        String author = quoteData.getString("author_name");

                        Child childData = new Child(quote, quote_id, author);
                        childList.add(childData);
                    }
                    Parent parentData = new Parent(genre_author, no_of_quotes, childList);
                    parentList.add(parentData);
                }


            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        progressDialog.dismiss();
        displayList();
    }


    public void displayList() {

        //create the adapter by passing your ArrayList data
        listAdapter = new CustomExpandableList(getActivity().getApplicationContext(), parentList);
        //attach the adapter to the list
        expandableListView.setAdapter(listAdapter);
        expandableListView.setGroupIndicator(getActivity().getApplicationContext().getResources().getDrawable(
                R.drawable.group_indicator));
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Child child = (Child) listAdapter.getChild(groupPosition,childPosition);

String fullQuote=child.getQuote();
                String allQuote[]=fullQuote.split(". ");

                String quote =fullQuote.replace(allQuote[0]+". ","") ;


                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
//                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, quote+"\n By Legend Quotes softikoda.com");
                try {
                    getActivity().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                   ex.printStackTrace();
                }


                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = listAdapter.getGroupCount();
                for (int i = 0; i < count; i++) {
                    if(i!=groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        swipeRefreshLayout.setRefreshing(false);
        onClose();
    }



    @Override
    public boolean onClose() {
        listAdapter.filterData("");
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        listAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.filterData(query);
        expandAll();
        return false;
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
    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();
    }
}
